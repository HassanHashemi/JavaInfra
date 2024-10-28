package org.infra.demo.samplequery;

import org.infra.cqrs.query.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class TestQueryHandler implements QueryHandler<TestQuery, String> {

    @Override
    public String handle(TestQuery query) {
        return "Hellow world??";
    }
}
