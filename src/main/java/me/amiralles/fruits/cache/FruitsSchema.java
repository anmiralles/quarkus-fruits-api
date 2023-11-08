package me.amiralles.fruits.cache;

import org.infinispan.protostream.GeneratedSchema;
import org.infinispan.protostream.annotations.AutoProtoSchemaBuilder;

@AutoProtoSchemaBuilder(includeClasses = { FruitsCache.class, FruitCache.class }, schemaPackageName = "fruits")
public interface FruitsSchema extends GeneratedSchema {
}
