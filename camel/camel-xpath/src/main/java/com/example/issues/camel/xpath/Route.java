package com.example.issues.camel.xpath;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Route extends RouteBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(Route.class);

    @Override
    public void configure() {
        from("direct:hello")
                .routeId("xpath-route")
                .setBody(xpath("//hello/id", Integer.class))
                .to("direct:goodbye");
    }
}