package me.amiralles.fruits;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.smallrye.mutiny.CompositeException;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.eclipse.microprofile.openapi.annotations.enums.SchemaType.ARRAY;

@ApplicationScoped
@Path("/fruits")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class FruitResource {

    private static final Logger LOGGER = Logger.getLogger(FruitResource.class.getName());

    @RestClient
    FruitClient fruitClient;

    @Inject
    FruitRedisService redisService;

    @GET
    @Operation(summary = "Returns all fruits")
    @APIResponse(
            responseCode = "200",
            description = "Gets all fruits, or empty list if none",
            content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Fruit .class, type = ARRAY))
    )
    @WithSpan("FruitResource.get")
    public Uni<List<Fruit>> get() {
        return fruitClient.get();
    }

    @GET
    @Path("{id}")
    @Operation(summary = "Returns a fruit for a given identifier")
    @APIResponse(
            responseCode = "200",
            description = "Gets a fruit for a given id",
            content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Fruit.class))
    )
    @APIResponse(
            responseCode = "422",
            description = "The fruit is not found for a given identifier"
    )
    public Uni<Response> getSingle(Long id) {

        return this.redisService.getFruit(String.valueOf(id))
                .onItem().ifNotNull().transform(cacheEntity -> {
                        LOGGER.info("got from cache");
                        return Response.ok(cacheEntity).build();
                    })
                .onItem().ifNull().switchTo(() -> {
                    LOGGER.info("got from database");
                    return fruitClient.getSingle(id)
                            .onItem().ifNotNull().invoke(fruit -> this.redisService.setFruit(String.valueOf(id), fruit))
                            .onItem().transform(fruit -> Response.ok(fruit).build());
                });

        //return fruitClient.getSingle(id);
    }

    @POST
    @Operation(summary = "Creates a fruit")
    @APIResponse(
            responseCode = "201",
            description = "The fruit was created"
    )
    @APIResponse(
            responseCode = "422",
            description = "Invalid fruit passed in (or no request body found)"
    )
    public Uni<Response> create(Fruit fruit) {
        if (fruit == null || fruit.id != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        return fruitClient.create(fruit);
    }

    @PUT
    @Path("{id}")
    public Uni<Response> update(Long id, Fruit fruit) {
        if (fruit == null || fruit.name == null) {
            throw new WebApplicationException("Fruit name was not set on request.", 422);
        }

        return fruitClient.update(id, fruit);
    }

    @DELETE
    @Path("{id}")
    public Uni<Response> delete(Long id) {
        return fruitClient.delete(id);
    }

    @Provider
    public static class ErrorMapper implements ExceptionMapper<Exception> {

        @Inject
        ObjectMapper objectMapper;

        @Override
        public Response toResponse(Exception exception) {
            LOGGER.error("Failed to handle request", exception);

            Throwable throwable = exception;

            int code = 500;
            if (throwable instanceof WebApplicationException) {
                code = ((WebApplicationException) exception).getResponse().getStatus();
            }

            // This is a Mutiny exception and it happens, for example, when we try to insert a new
            // fruit but the name is already in the database
            if (throwable instanceof CompositeException) {
                throwable = ((CompositeException) throwable).getCause();
            }

            ObjectNode exceptionJson = objectMapper.createObjectNode();
            exceptionJson.put("exceptionType", throwable.getClass().getName());
            exceptionJson.put("code", code);

            if (exception.getMessage() != null) {
                exceptionJson.put("error", throwable.getMessage());
            }

            return Response.status(code)
                    .entity(exceptionJson)
                    .build();
        }

    }
}
