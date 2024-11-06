package org.infra.demo;

import org.infra.cqrs.command.CommandProcessor;
import org.infra.cqrs.query.QueryProcessor;
import org.infra.demo.samplecommand.SampleCommand;
import org.infra.demo.samplequery.TestQuery;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
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
//        var result = this.queryProcessor.execute(new TestQuery());
//
//        System.out.println(result.getName());
        var commandResult = this.commandProcessor.execute(new SampleCommand());

        System.out.println(commandResult);
    }
}

