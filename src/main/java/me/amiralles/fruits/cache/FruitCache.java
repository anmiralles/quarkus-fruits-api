package me.amiralles.fruits.cache;

import org.infinispan.protostream.annotations.Proto;

@Proto
public record FruitCache(Long id, String name, Long itemId) {
}
