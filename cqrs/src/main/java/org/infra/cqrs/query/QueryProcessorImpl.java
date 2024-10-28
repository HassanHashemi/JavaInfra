package org.infra.cqrs.query;

import org.infra.cqrs.command.CommandHandlerType;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Service
@Scope(SCOPE_PROTOTYPE)
public class QueryProcessorImpl implements QueryProcessor {
    private final ApplicationContext context;

    public QueryProcessorImpl(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public <TQuery extends Query, TResult> TResult execute(TQuery query) {
        var handlerAnnotation = query.getClass().getAnnotation(QueryHandlerType.class);
        if (handlerAnnotation == null)
            throw new RuntimeException("Handler annotation was not found");

        var handler = (QueryHandler<TQuery, TResult>) this.context.getBean(handlerAnnotation.handlerClass());

        return handler.handle(query);
    }
}
