package org.infra.decorators.caching;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class DefaultJsonSerializer implements CacheSerializer {
    private final ObjectMapper serializer;

    public DefaultJsonSerializer() {
        this.serializer = new ObjectMapper();
    }

    @Override
    public String serialize(Object object) {
        if (object == null)
            throw new IllegalArgumentException("object cant be null");

        try {
            return this.serializer.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T deserialize(String json, Class<T> targetClass) {
        if (json == null || json.isEmpty())
            throw new IllegalArgumentException("json");

        try {
            return this.serializer.readValue(json, targetClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
