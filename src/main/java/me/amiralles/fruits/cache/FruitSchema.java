package me.amiralles.fruits.cache;

import org.infinispan.protostream.GeneratedSchema;
import org.infinispan.protostream.annotations.ProtoSchema;

@ProtoSchema(includeClasses = { FruitCache.class }, schemaPackageName = "fruit")
public interface FruitSchema extends GeneratedSchema {
}
