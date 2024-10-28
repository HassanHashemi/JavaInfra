package org.infra.demo.samplequery;

import org.infra.cqrs.query.Query;
import org.infra.cqrs.query.QueryHandlerType;

@QueryHandlerType(handlerClass = TestQueryHandler.class)
public class TestQuery implements Query {

}

