package me.amiralles.fruits.cache;

import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

import java.util.Objects;

public class FruitCache {
    private final String name;
    private final Long itemId;

    @ProtoFactory
    public FruitCache(String name, Long itemId) {
        this.name = Objects.requireNonNull(name);
        this.itemId = Objects.requireNonNull(itemId);
    }

    @ProtoField(number = 1)
    public String getName() {
        return name;
    }

    @ProtoField(number = 2)
    public Long getItemId() {
        return itemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FruitCache that = (FruitCache) o;
        return Objects.equals(name, that.name) && Objects.equals(itemId, that.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, itemId);
    }

    @Override
    public String toString() {
        return "FruitCache{" +
                "name='" + name + '\'' +
                ", itemId=" + itemId +
                '}';
    }
}
