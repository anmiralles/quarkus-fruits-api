package me.amiralles.fruits.cache;

import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

import java.util.List;
import java.util.Objects;

public class FruitsCache {
    private final List<FruitCache> lstFruitsCache;

    @ProtoFactory
    public FruitsCache(List<FruitCache> lstFruitsCache) {
        this.lstFruitsCache = lstFruitsCache;
    }

    @ProtoField(number = 1)
    public List<FruitCache> getLstFruitsCache() {
        return lstFruitsCache;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FruitsCache that = (FruitsCache) o;
        return Objects.equals(lstFruitsCache, that.lstFruitsCache);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lstFruitsCache);
    }

    @Override
    public String toString() {
        return "FruitsCache{" +
                "lstFruitCaches=" + lstFruitsCache +
                '}';
    }
}
