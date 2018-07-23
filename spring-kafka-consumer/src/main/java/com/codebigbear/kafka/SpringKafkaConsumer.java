package com.codebigbear.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.File;

@SpringBootApplication
@Configuration
public class SpringKafkaConsumer {

    public static void main(String[] args) {


                try{

                    /*File jassFile = ResourceUtils.getFile("classpath: jaasConfig");*/

                 /*   System.setProperty("java.security.krb5.conf");
                    System.setProperty("java.security.auth.login.conf");
                    System.setProperty("javax.net.ssl.keyStore");
                    System.setProperty("javax.net.ssl.keyStorePassword");
                    System.setProperty("javax.net.ssl.trustStore");
                    System.setProperty("javax.net.ssl.trustStorePassword");*/


                }
               catch (Exception e) {

               }






        SpringApplication.run(SpringKafkaConsumer.class, args);
    }
}
