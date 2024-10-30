package org.infra.cqrs.query;

public interface QueryHandlerDecorator<TQuery extends Query, TResult> {
    TResult handle(TQuery query, DecoratorContext context);
    default int priority() { return 0; }
}