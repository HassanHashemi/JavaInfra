package org.infra.decorators.caching;

import org.infra.cqrs.command.Command;
import org.infra.cqrs.command.CommandHandler;
import org.infra.cqrs.command.CommandHandlerDecorator;
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

    @Autowired
    private CacheSerializer serializer;

    @Autowired
    private DistributedCache cache;

    public CacheDecorator(DistributedCache cache, CacheSerializer serializer) {
        this.cache = cache;
        this.serializer = serializer;
    }

    @Override
    public TResult handle(QueryHandler<TQuery, TResult> innerHandler, TQuery query, HandlerContext context) {
        if (!(query instanceof CacheableQuery cacheableQuery)) {
            return innerHandler.handle(query, context);
        }

        context.stopPipeline();
        var cacheValue = cache.get(cacheableQuery.getKey());
        if (cacheValue == null) {
            logger.info("cache key {} got missed", cacheableQuery.getKey());

            var freshResult = innerHandler.handle(query, context);
            cache.set(
                    cacheableQuery.getKey(),
                    this.serializer.serialize(freshResult),
                    cacheableQuery.timeOut());

            return freshResult;
        } else {
            logger.info("cache key {} got hit", cacheableQuery.getKey());
            var resultClass = GenericTypeResolver.resolveTypeArguments(cacheableQuery.getClass(), Query.class)[0];

            return this.serializer.deserialize(cacheValue, (Class<TResult>) resultClass);
        }
    }
}

