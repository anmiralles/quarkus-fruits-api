package me.amiralles.fruits.cache;

import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

import java.util.Objects;

public record FruitCache(Long id, String name, Long itemId) {
    @ProtoFactory
    public FruitCache(Long id, String name, Long itemId) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.itemId = Objects.requireNonNull(itemId);
    }

    @Override
    @ProtoField(number = 1)
    public Long id() {
        return id;
    }

    @Override
    @ProtoField(number = 2)
    public String name() {
        return name;
    }

    @Override
    @ProtoField(number = 3)
    public Long itemId() {
        return itemId;
    }

    @Override
    public String toString() {
        return "FruitCache{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", itemId=" + itemId +
                '}';
    }
}
