package org.infra.demo;

import jakarta.annotation.Resource;
import org.infra.cqrs.query.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Component
@Scope(SCOPE_PROTOTYPE)
public class CacheDecorator<TQuery extends Query<TResult>, TResult> implements QueryHandlerDecorator<TQuery, TResult> {
    private final QueryHandler<Query<TResult>, TResult> innerHandler;
    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> keyValue;

    @Autowired
    private CacheSerializer serializer;

    public CacheDecorator(QueryHandler<Query<TResult>, TResult> innerHandler) {
        this.innerHandler = innerHandler;
    }

    private String serializeResult(Object freshResult) {
        return this.serializer.serialize(freshResult);
    }

    @Override
    public TResult handle(TQuery query, DecoratorContext context) {
        if (!(query instanceof CacheableQuery cacheableQuery)) {
            return this.innerHandler.handle(query);
        }

        var cacheValue = keyValue.get(cacheableQuery.getKey());
        if (cacheValue == null) {
            var freshResult = this.innerHandler.handle(cacheableQuery);
            keyValue.set(cacheableQuery.getKey(), serializeResult(freshResult));

            return (TResult)freshResult;
        } else {
            var resultClass = GenericTypeResolver.resolveTypeArguments(cacheableQuery.getClass(), Query.class)[0];
            return this.serializer.deserialize(cacheValue, (Class<TResult>) resultClass);
        }
    }
}

