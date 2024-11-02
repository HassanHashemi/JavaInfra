package org.infra.cqrs.context;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class DefaultContextFactory implements ContextFactory {
    private final ApplicationContext context;

    public DefaultContextFactory(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public HandlerContext get() {
        return this.context.getBean(HandlerContext.class);
    }
}
