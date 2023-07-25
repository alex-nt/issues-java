package com.example.issues.camel.kafka.idempotent;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertNull;

//@SpringBootTest
//public class AppTest {
//    @Autowired
//    private CamelContext context;
//
//    @Test
//    public void loadMessages() {
//        final ConsumerTemplate consumer = context.createConsumerTemplate();
//        final Integer body = consumer.receiveBody("kafka:error",
//                Duration.ofMinutes(10).toMillis(), Integer.class);
//        assertNull(body);
//    }
//}
