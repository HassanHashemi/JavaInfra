package org.infra.demo.samplequery;

import org.infra.cqrs.context.HandlerContext;
import org.infra.cqrs.query.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class TestQueryHandler implements QueryHandler<TestQuery, TestQueryResult> {

    @Override
    public TestQueryResult handle(TestQuery query, HandlerContext context) {

        var result = new TestQueryResult();
        result.setName("Hassan");

        return result;
    }
}

