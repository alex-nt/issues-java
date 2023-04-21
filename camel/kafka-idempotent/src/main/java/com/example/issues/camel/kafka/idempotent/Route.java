package com.example.issues.camel.kafka.idempotent;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConfiguration;
import org.apache.camel.component.kafka.springboot.KafkaComponentConfiguration;
import org.apache.camel.processor.idempotent.kafka.KafkaIdempotentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;

@Component
public class Route extends RouteBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(Route.class);
    private static final int SIZE = 100;
    private static final String PROPERTY = "UsedToCheck";
    private final KafkaIdempotentRepository kafkaIdempotentRepository;

    public Route(final KafkaComponentConfiguration config) {
        this.kafkaIdempotentRepository = new KafkaIdempotentRepository(
                "idempotent-db-inserts", config.getBrokers(), SIZE, 100);
    }

    @Override
    public void configure() {
        for(int i = 0; i< 20; i++){
            from("timer:foo-" + i + "?delay=1&repeatCount=10000")
                    .routeId("timer-" + i)
                    .process(exchange -> exchange.setProperty(PROPERTY, UUID.randomUUID()))
                    .to("direct:deduplicate");
        }

        from("direct:deduplicate")
                .routeId("deduplicate")
                .idempotentConsumer(exchangeProperty(PROPERTY), kafkaIdempotentRepository)
                    .choice()
                        .when(exchange -> getSize() > SIZE)
                            .process(exchange -> LOGGER.warn("Actual size {}", getSize()))
                            .setBody(exchange -> getSize())
                            .to("kafka:error")
                        .end()
                    .end()
                .end();
    }

    private int getSize() {
        try {
            Field f = kafkaIdempotentRepository.getClass().getDeclaredField("cache");
            f.setAccessible(true);
            Map<String, Object> cache = (Map<String, Object>) f.get(kafkaIdempotentRepository);
            if (null != cache) {
                return cache.size();
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}
