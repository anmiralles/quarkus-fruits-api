package me.amiralles.fruits;

import jakarta.enterprise.context.ApplicationScoped;
import me.amiralles.fruits.cache.FruitCache;

@ApplicationScoped
public class FruitMapper {

    FruitCache toDomain(Fruit entity) {
        return new FruitCache(entity.name, entity.itemId);
    }

}
