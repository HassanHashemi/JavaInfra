//package org.infra.demo.samplequery;
//
//import org.infra.cqrs.context.HandlerContext;
//import org.infra.cqrs.query.Query;
//import org.infra.cqrs.query.QueryHandler;
//import org.infra.cqrs.query.QueryHandlerDecorator;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;
//
//@Component
//@Scope(SCOPE_PROTOTYPE)
//public class TestDecorator2 implements QueryHandlerDecorator {
//    private final QueryHandler innerHandler;
//
//    public TestDecorator2(QueryHandler<Query, ?> innerHandler) {
//        this.innerHandler = innerHandler;
//    }
//
//    @Override
//    public Object handle(Query query, HandlerContext context) {
//        System.out.println("2");
//        return this.innerHandler.handle(query);
//    }
//
//    @Override
//    public int priority() {
//        return 0;
//    }
//}
