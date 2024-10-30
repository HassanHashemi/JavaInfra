package org.infra.demo.samplequery;

import org.infra.cqrs.query.DecoratorContext;
import org.infra.cqrs.query.Query;
import org.infra.cqrs.query.QueryHandler;
import org.infra.cqrs.query.QueryHandlerDecorator;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Component
@Scope(SCOPE_PROTOTYPE)
public class TestDecorator implements QueryHandlerDecorator {
    private final QueryHandler innerHandler;


    public TestDecorator(QueryHandler<TestQuery, ?> innerHandler) {
        this.innerHandler = innerHandler;
    }


    @Override
    public Object handle(Query query, DecoratorContext context) {
        System.out.println("1");

        return this.innerHandler.handle(query);
    }

    @Override
    public int priority() {
        return 0;
    }
}


