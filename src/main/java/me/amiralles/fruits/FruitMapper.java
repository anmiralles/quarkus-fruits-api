package me.amiralles.fruits;

import jakarta.enterprise.context.ApplicationScoped;
import me.amiralles.fruits.cache.FruitCache;
import me.amiralles.fruits.cache.FruitsCache;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class FruitMapper {

    FruitCache toDomain(Fruit entity) {
        return new FruitCache(entity.id, entity.name, entity.itemId);
    }

    FruitsCache toDomainList(List<Fruit> fruits) {
        return new FruitsCache(fruits
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList()));
    }

}
