package org.infra.cqrs.query;

public interface QueryProcessor {
    <TQuery extends Query<TResult>, TResult> TResult execute(TQuery query);
}
