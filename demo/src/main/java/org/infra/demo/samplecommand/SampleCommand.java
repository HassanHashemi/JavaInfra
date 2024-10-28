package org.infra.demo.samplecommand;

import org.infra.cqrs.command.Command;
import org.infra.cqrs.command.CommandHandlerType;

@CommandHandlerType(handlerClass = SampleCommandHandler.class)
public class SampleCommand implements Command {
}

