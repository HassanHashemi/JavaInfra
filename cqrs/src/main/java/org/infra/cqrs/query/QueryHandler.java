package org.infra.cqrs.query;

public interface QueryHandler<TQuery extends Query, TResult> {
    TResult handle(TQuery query);
}
