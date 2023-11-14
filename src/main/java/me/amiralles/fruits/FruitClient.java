package me.amiralles.fruits;

import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/fruits")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@RegisterRestClient(configKey = "fruits-data")
public interface FruitClient {

    @GET
    @WithSpan(kind = SpanKind.CLIENT, value = "FruitClient.getFruits")
    Uni<List<Fruit>> getFruits();

    @GET
    @Path("{id}")
    @WithSpan(kind = SpanKind.CLIENT, value = "FruitClient.getFruit")
    Uni<Fruit> getFruit(@PathParam("id") Long id);

    @POST
    @WithSpan(kind = SpanKind.CLIENT, value = "FruitClient.create")
    Uni<Response> create(Fruit fruit);

    @PUT
    @Path("{id}")
    Uni<Response> update(Long id, Fruit fruit);

    @DELETE
    @Path("{id}")
    Uni<Response> delete(@PathParam("id") Long id);
}
