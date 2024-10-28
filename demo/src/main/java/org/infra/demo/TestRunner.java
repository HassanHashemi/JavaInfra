package org.infra.demo;

import org.infra.cqrs.command.CommandProcessor;
import org.infra.cqrs.query.QueryProcessor;
import org.infra.demo.samplecommand.SampleCommand;
import org.infra.demo.samplequery.TestQuery;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TestRunner implements CommandLineRunner {
    private final QueryProcessor queryProcessor;
    private final CommandProcessor commandProcessor;

    public TestRunner(QueryProcessor queryProcessor, CommandProcessor commandProcessor) {
        this.queryProcessor = queryProcessor;
        this.commandProcessor = commandProcessor;
    }

    @Override
    public void run(String... args) throws Exception {
        var result = this.queryProcessor.execute(new TestQuery());
        var commandResult = this.commandProcessor.execute(new SampleCommand());

        System.out.println(commandResult);
    }
}

