package org.infra.demo.samplequery;

import org.infra.decorators.caching.CacheableQuery;

public class TestQuery implements CacheableQuery<TestQueryResult> {
    @Override
    public String getKey() {
        return "1234567";
    }
}

