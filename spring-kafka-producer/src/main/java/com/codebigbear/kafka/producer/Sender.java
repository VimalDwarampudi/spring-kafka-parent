package com.codebigbear.kafka.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import com.codebigbear.avro.Review;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class Sender {

    private static final Logger LOGGER = LoggerFactory.getLogger(Sender.class);

    @Value("${app.topic.send}")
    private String sendTopic;

    @Autowired
    private KafkaTemplate<String, Review> kafkaTemplate;

    public void send(Review review) {

        Message<Review> message = MessageBuilder
                .withPayload(review)
                .setHeader(KafkaHeaders.TOPIC, sendTopic)
                .setHeader(KafkaHeaders.MESSAGE_KEY, "999")
                .setHeader(KafkaHeaders.PARTITION_ID, 0)
                .setHeader("X-Custom-Header", "Sending Custom Header with Spring Kafka")
                .build();

        LOGGER.info("sending message='{}' to topic='{}'", review.toString(), sendTopic);

        kafkaTemplate.send(sendTopic, review);
    }
}