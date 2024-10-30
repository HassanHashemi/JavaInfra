package org.infra.cqrs.query;

public interface CacheSerializer {
    String serialize(Object object);

    <T> T deserialize(String json, Class<T> targetClass);
}
