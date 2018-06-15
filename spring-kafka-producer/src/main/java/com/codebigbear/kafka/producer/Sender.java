package com.codebigbear.kafka.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import com.codebigbear.avro.Review;
import org.springframework.stereotype.Service;

@Service
public class Sender {

    private static final Logger LOGGER = LoggerFactory.getLogger(Sender.class);

    @Value("${app.topic.send}")
    private String sendTopic;

    @Autowired
    private KafkaTemplate<String, Review> kafkaTemplate;

    public void send(Review review) {
        LOGGER.info("sending user='{}'", review.toString());
        kafkaTemplate.send(sendTopic, review);
    }
}