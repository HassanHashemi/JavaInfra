package org.infra.demo.samplequery;

import org.infra.cqrs.context.HandlerContext;
import org.infra.cqrs.query.Query;
import org.infra.cqrs.query.QueryHandler;
import org.infra.cqrs.query.QueryHandlerDecorator;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Component
@Scope(SCOPE_PROTOTYPE)
public class TestDecorator<TQuery extends Query<TResult>, TResult> implements QueryHandlerDecorator<TQuery, TResult> {
    @Override
    public TResult handle(QueryHandler<TQuery, TResult> innerHandler, TQuery query, HandlerContext context) {
        System.out.println("1");

        return innerHandler.handle(query, context);
    }
}