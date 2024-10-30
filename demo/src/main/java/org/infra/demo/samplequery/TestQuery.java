package org.infra.demo.samplequery;

import org.infra.cqrs.query.CacheableQuery;


public class TestQuery implements CacheableQuery<TestQueryResult> {
    @Override
    public String getKey() {
        return "123456";
    }
}

