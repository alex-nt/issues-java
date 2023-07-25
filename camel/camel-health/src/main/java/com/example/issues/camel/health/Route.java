package com.example.issues.camel.health;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpMethods;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Route extends RouteBuilder {
    @Value("${server.port}")
    private int port;

    @Override
    public void configure() {
        from("timer:hello?delay=1&repeatCount=10000")
                .routeId("timer-hello")
                .log("Hello Timer ${exchangeProperty.CamelTimerCounter}")
                .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.GET))
                .to(String.format("http://localhost:%d/actuator/health", port))
                .log("HEALTH ${body}")
                .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.GET))
                .to(String.format("http://localhost:%d/actuator/health/liveness", port))
                .log("LIVENESS ${body}")
                .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.GET))
                .to(String.format("http://localhost:%d/actuator/health/readiness", port))
                .log("READINESS ${body}");

        from("kafka:hello")
                .routeId("kafka")
                .log("Hello Kafka");
    }
}
