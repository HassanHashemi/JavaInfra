package org.infra.cqrs.query;

import org.infra.cqrs.context.HandlerContext;

public interface QueryHandler<TQuery extends Query<TResult>, TResult> {
    TResult handle(TQuery query, HandlerContext context);
}

