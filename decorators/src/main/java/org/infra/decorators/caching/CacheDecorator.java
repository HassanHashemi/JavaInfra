package org.infra.decorators.caching;

import org.infra.cqrs.context.HandlerContext;
import org.infra.cqrs.query.Query;
import org.infra.cqrs.query.QueryHandler;
import org.infra.cqrs.query.QueryHandlerDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CacheDecorator<TQuery extends Query<TResult>, TResult> implements QueryHandlerDecorator<TQuery, TResult> {
    private final Logger logger = LoggerFactory.getLogger(CacheDecorator.class);
    private final QueryHandler<Query<TResult>, TResult> innerHandler;

    @Autowired
    private CacheSerializer serializer;

    @Autowired
    private DistributedCache cache;

    public CacheDecorator(QueryHandler<Query<TResult>, TResult> innerHandler) {
        this.innerHandler = innerHandler;
    }

    private String serializeResult(Object freshResult) {
        return this.serializer.serialize(freshResult);
    }

    @Override
    public TResult handle(TQuery query, HandlerContext context) {
        if (!(query instanceof CacheableQuery cacheableQuery)) {
            return this.innerHandler.handle(query, context);
        }

        context.stopPipeline();
        var cacheValue = cache.get(cacheableQuery.getKey());
        if (cacheValue == null) {
            logger.info("cache key {} got missed", cacheableQuery.getKey());
            var freshResult = this.innerHandler.handle((Query<TResult>) cacheableQuery, context);
            cache.set(cacheableQuery.getKey(), serializeResult(freshResult), cacheableQuery.slidingExpiration());

            return freshResult;
        } else {
            logger.info("cache key {} got hit", cacheableQuery.getKey());
            var resultClass = GenericTypeResolver.resolveTypeArguments(cacheableQuery.getClass(), Query.class)[0];
            return this.serializer.deserialize(cacheValue, (Class<TResult>) resultClass);
        }
    }
}

