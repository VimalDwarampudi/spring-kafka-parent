package com.codebigbear.kafka.producer;

import com.codebigbear.avro.Review;
import org.joda.time.DateTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
public class SpringKafkaApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringKafkaApplication.class, args);
    }


    @Autowired
    private Sender sender;

    @Override
    public void run(String... strings) throws Exception {

        Review review1 = new Review();
        review1.setId(Long.valueOf(5952700));
        review1.setTitle("kafka");
        review1.setCreated(DateTime.now());

        Review review2 = new Review();
        review2.setId(Long.valueOf(5952700));
        review2.setTitle("producer");
        review2.setCreated(DateTime.now());

        Review review3 = new Review();
        review3.setId(Long.valueOf(5952700));
        review3.setTitle("stream");
        review3.setCreated(DateTime.now());

        sender.send(review1);
        //sender.send(review2);
       //sender.send(review3);


    }
}