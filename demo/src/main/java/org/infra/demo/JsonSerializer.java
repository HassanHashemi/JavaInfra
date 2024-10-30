package org.infra.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.infra.cqrs.query.CacheSerializer;
import org.springframework.stereotype.Component;

@Component
public class JsonSerializer implements CacheSerializer {
    private final ObjectMapper serializer;

    public JsonSerializer() {
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
        try {
            return this.serializer.readValue(json, targetClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
