package org.infra.demo.samplecommand;

import org.infra.cqrs.command.CommandHandler;
import org.springframework.stereotype.Component;

@Component
public class SampleCommandHandler implements CommandHandler<SampleCommand, String> {
    @Override
    public String handle(SampleCommand command) {
        return "Sample command";
    }
}
