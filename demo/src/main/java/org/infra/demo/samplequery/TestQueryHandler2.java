package org.infra.demo.samplequery;

import org.infra.cqrs.query.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class TestQueryHandler2 implements QueryHandler<TestQuery2, String> {

    @Override
    public String handle(TestQuery2 query) {
        return "Hellow world??";
    }
}
