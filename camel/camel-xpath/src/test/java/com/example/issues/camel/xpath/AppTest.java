package com.example.issues.camel.xpath;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class AppTest {
    @Autowired
    private CamelContext context;

    @Test
    public void sendXml() {
        final ConsumerTemplate consumer = context.createConsumerTemplate();
        final ProducerTemplate producer = context.createProducerTemplate();

        producer.sendBody("direct:hello", "<a>5</a>");
        final Integer count = consumer.receiveBody("direct:goodbye",
                Duration.ofSeconds(5).toMillis(), Integer.class);
        assertNotNull(count);
    }
}
