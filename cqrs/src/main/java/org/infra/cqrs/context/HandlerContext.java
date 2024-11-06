package org.infra.cqrs.context;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Component
@Scope(SCOPE_PROTOTYPE)
public final class HandlerContext {
    private final Map<String, Object> headers = new HashMap<>();
    private boolean moveNext = true;

    public boolean moveNext() {
        return this.moveNext;
    }

    public void stopPipeline() {
        this.moveNext = false;
    }

    public void addHeader(String key, Object value) {
        if (key == null || key.isEmpty())
            throw new IllegalArgumentException("invalid key");

        if (value == null)
            throw new IllegalArgumentException("value cant be null");

        this.headers.put(key, value);
    }

    public <T> T getHeader(String key, Class<T> cls) {
        if (key == null || key.isEmpty())
            throw new IllegalArgumentException("invalid key");

        var value = this.headers.get(key);
        if (!cls.isInstance(value))
            throw new ClassCastException("incompatible value to <T>");

        return cls.cast(value);
    }
}
