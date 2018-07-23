package com.codebigbear.kafka.consumer;

import com.codebigbear.avro.Review;
import com.codebigbear.kafka.exceptions.CustomException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class Receiver {

    private static final Logger LOG = LoggerFactory.getLogger(Receiver.class);

    //https://stackoverflow.com/questions/47427948/how-to-acknowledge-current-offset-in-spring-kafka-for-manual-commit

    @KafkaListener(topics = "${app.topic.receive}")
    public void receive(ConsumerRecord<?, ?> consumerRecord, Acknowledgment acknowledgment,@Header(KafkaHeaders.OFFSET) Long offset,
                        @Header(KafkaHeaders.CONSUMER) KafkaConsumer<String, String> consumer,
                        @Header(KafkaHeaders.TIMESTAMP_TYPE) String timestampType,
                        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partitionId,
                        //@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String messageKey,
                        @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp
                        //, @Header("X-Custom-Header") String customHeader
                        ) {

        String message = consumerRecord.value().toString();

        /*
        Printing the received message to the console.
         */
        LOG.info("Received message: ");
        LOG.info(message);


            try {

                handleMessage(message);
                acknowledgment.acknowledge();


            } catch (CustomException e) {
                e.printStackTrace();
                LOG.error("Exception caught. Not committing offset to Kafka.");

            }

    }

    /**
     * Function to handle the message received from Kafka.
     *
     * @param message The String message
     */
    private void handleMessage(String message) throws CustomException {

        /*
        Printing a random message.
         */
        LOG.info("Busy handling message!");

        /*
        Processing the message => calculating the length of the String.
         */
        int messageLength = message.length();
        LOG.info("Message length: " + messageLength);


    }


    }

