package me.amiralles.fruits;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import me.amiralles.fruits.cache.FruitCache;
import me.amiralles.fruits.cache.FruitsCache;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.logging.Logger;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.eclipse.microprofile.openapi.annotations.enums.SchemaType.ARRAY;

@ApplicationScoped
@Path("/fruits")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class FruitResource {

    private static final Logger LOGGER = Logger.getLogger(FruitResource.class.getName());

    @Inject
    FruitService fruitService;

    @GET
    @Operation(summary = "Returns all fruits")
    @APIResponse(
            responseCode = "200",
            description = "Gets all fruits, or empty list if none",
            content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Fruit .class, type = ARRAY))
    )
    @WithSpan("FruitResource.getFruits")
    public FruitsCache getFruits() {
        LOGGER.info("Getting fruits");
        return fruitService.getFruits();
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
    @WithSpan("FruitResource.getFruit")
    public FruitCache getFruit(Long id) {
        LOGGER.info("Getting fruit for id: " + id);
        return fruitService.getFruit(id);
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
    @WithSpan("FruitResource.createFruit")
    public Response createFruit(Fruit fruit) {
        if (fruit == null || fruit.id != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }
        LOGGER.info("Creating fruit: " + fruit);
        return fruitService.createFruit(fruit);
    }

    @PUT
    @Path("{id}")
    @Operation(summary = "Updates a fruit")
    @APIResponse(
            responseCode = "200",
            description = "The fruit was updated"
    )
    @APIResponse(
            responseCode = "422",
            description = "Invalid fruit passed in (or no request body found)"
    )
    @WithSpan("FruitResource.updateFruit")
    public Uni<Response> updateFruit(Long id, Fruit fruit) {
        if (fruit == null || fruit.name == null) {
            throw new WebApplicationException("Fruit name was not set on request.", 422);
        }
        LOGGER.info("Updating fruit for id:: " + fruit.id);
        return fruitService.updateFruit(id, fruit);
    }

    @DELETE
    @Path("{id}")
    @Operation(summary = "Updates a fruit")
    @APIResponse(
            responseCode = "204",
            description = "No Content"
    )
    @WithSpan("FruitResource.deleteFruit")
    public Uni<Response> delete(@PathParam("id") Long id) {
        return fruitService.delete(id);
    }

}
