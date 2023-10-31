package me.amiralles.fruits;

import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.hash.ReactiveHashCommands;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FruitRedisService {

    private static final String MY_KEY = "fruits";

    private final ReactiveHashCommands<String, String, Fruit> reactiveCommands;

    public FruitRedisService(ReactiveRedisDataSource rds) {
        reactiveCommands = rds.hash(Fruit.class);
    }

    public void setFruit(String field, Fruit value) {
        reactiveCommands.hset(MY_KEY, field, value);
    }

    public Uni<Fruit> getFruit(String field) {
        return reactiveCommands.hget(MY_KEY, field);
    }
}
