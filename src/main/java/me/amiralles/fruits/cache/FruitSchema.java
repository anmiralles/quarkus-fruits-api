package me.amiralles.fruits.cache;

import org.infinispan.protostream.GeneratedSchema;
import org.infinispan.protostream.annotations.AutoProtoSchemaBuilder;

@AutoProtoSchemaBuilder(includeClasses = { FruitCache.class }, schemaPackageName = "fruit")
public interface FruitSchema extends GeneratedSchema {
}
