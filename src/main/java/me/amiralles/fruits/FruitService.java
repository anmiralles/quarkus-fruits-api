package me.amiralles.fruits;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.quarkus.infinispan.client.Remote;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpServerRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import me.amiralles.fruits.cache.FruitCache;
import me.amiralles.fruits.cache.FruitsCache;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.infinispan.client.hotrod.RemoteCache;
import java.net.URI;
import java.util.List;

@ApplicationScoped
public class FruitService {

    @Inject
    @Remote("fruit")
    RemoteCache<String, FruitCache> fruitCache;

    @Inject
    @Remote("fruits")
    RemoteCache<String, FruitsCache> fruitsCache;

    @RestClient
    FruitClient fruitClient;

    @Inject
    FruitMapper mapper;

    @Context
    UriInfo uriInfo;

    @Context
    HttpServerRequest request;

    @WithSpan("FruitService.getFruits")
    public FruitsCache getFruits() {
        FruitsCache fruitsFromCache = fruitsCache.get("fruits");

        if (fruitsFromCache == null || fruitsFromCache.getLstFruitsCache().isEmpty()) {
            List<Fruit> fruitsFromDb = fruitClient.getFruits().await().indefinitely();
            fruitsFromCache = mapper.toDomainList(fruitsFromDb);
            fruitsCache.put("fruits", fruitsFromCache);
        }

        return fruitsFromCache;
    }

    @WithSpan("FruitService.getFruit")
    public FruitCache getFruit(Long id) throws WebApplicationException {
        FruitCache fruitFromCache = fruitCache.get(String.valueOf(id));

        if (fruitFromCache == null) {
            Fruit fruitFromDb = fruitClient.getFruit(id).await().indefinitely();

            if(fruitFromDb != null ) {
                fruitFromCache = mapper.toDomain(fruitFromDb);
                fruitCache.put(String.valueOf(id), mapper.toDomain(fruitFromDb));
            } else {
                throw new WebApplicationException("Fruit with id of " + id + " does not exist.", 422);
            }
        }

        return fruitFromCache;
    }

    @WithSpan("FruitService.createFruit")
    public Response createFruit(Fruit fruit) {
        fruitsCache.remove("fruits");
        Uni<Response> fruitResponse = fruitClient.createFruit(fruit);
        return Response
                .created(buildUri(fruitResponse.await().indefinitely().readEntity(Fruit.class)))
                .build();
    }

    @WithSpan("FruitService.updateFruit")
    public Uni<Response> updateFruit(Long id, Fruit fruit) {
        fruitCache.remove(String.valueOf(id));
        return fruitClient.updateFruit(id, fruit);
    }

    @WithSpan("FruitService.deleteFruit")
    public Uni<Response> delete(Long id) {
        fruitCache.remove(String.valueOf(id));
        return fruitClient.deleteFruit(id);
    }

    private URI buildUri(Fruit fruit) {
        return uriInfo.getBaseUriBuilder()
                .host(request.localAddress().host())
                .port(request.localAddress().port())
                .path(FruitResource.class)
                .path(FruitResource.class, "getFruit")
                .build(fruit.id);
    }
}
