package org.infra.cqrs.query;

import org.infra.cqrs.context.HandlerContext;

public interface QueryHandlerDecorator<TQuery extends Query<TResult>, TResult> {
    TResult handle(TQuery query, HandlerContext context);
    default int priority() { return 0; }

    class Comparator implements java.util.Comparator<QueryHandlerDecorator> {

        @Override
        public int compare(QueryHandlerDecorator o1, QueryHandlerDecorator o2) {
            if (o1.priority() < o2.priority()) return -1;
            else if (o1.priority() == o2.priority()) return 0;
            else return Integer.MAX_VALUE;
        }
    }
}