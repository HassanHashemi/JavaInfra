package org.infra.demo.samplequery;

import lombok.Data;
import org.infra.cqrs.query.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class TestQueryHandler implements QueryHandler<TestQuery, TestQueryResult> {

    @Override
    public TestQueryResult handle(TestQuery query) {

        var result = new TestQueryResult();
        result.setName("Hassan");

        return result;
    }
}

