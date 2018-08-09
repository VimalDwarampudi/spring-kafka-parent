package com.codebigbear.kstreams.config;

import com.codebigbear.avro.Review;
import com.codebigbear.avro.Statistics;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.Consumed;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.kstream.internals.WindowedDeserializer;
import org.apache.kafka.streams.kstream.internals.WindowedSerializer;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableKafka
@EnableKafkaStreams
public  class KafkaStreamsConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.schema-registry}")
    private String schemaRegistryUrl;

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public StreamsConfig kStreamsConfigs() {
        Map<String, Object> config = new HashMap<>();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "codebigbear");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        // we disable the cache to demonstrate all the "steps" involved in the transformation - not recommended in prod
        config.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "0");
        // Exactly once processing!!
        config.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE);
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAvroSerde.class);
        config.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        return new StreamsConfig(config);
    }

    @Bean
    public KStream<?, ?> kStream(StreamsBuilder kStreamBuilder) throws IOException {
        // Not used - Start
        StringSerializer stringSerializer = new StringSerializer();
        StringDeserializer stringDeserializer = new StringDeserializer();
        Serde<String> serdeString = Serdes.serdeFrom(stringSerializer,stringDeserializer);
        WindowedSerializer<String> windowedSerializer = new WindowedSerializer<>(stringSerializer);
        WindowedDeserializer<String> windowedDeserializer = new WindowedDeserializer<>(stringDeserializer);
        Serde<Windowed<String>> windowedSerde = Serdes.serdeFrom(windowedSerializer,windowedDeserializer);
// Not used - End
        // define a few serdes that will be useful to us later
        SpecificAvroSerde<Review> reviewSpecificAvroSerde = new SpecificAvroSerde<>();
        reviewSpecificAvroSerde.configure(Collections.singletonMap(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl), false);
        SpecificAvroSerde<Statistics> statisticsSpecificAvroSerde = new SpecificAvroSerde<>();
        statisticsSpecificAvroSerde.configure(Collections.singletonMap(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl), false);
        Serdes.LongSerde longSerde = new Serdes.LongSerde();
        Serdes.StringSerde stringSerde = new Serdes.StringSerde();
        long windowSizeMs = TimeUnit.MINUTES.toMillis(15);
        long tumblingWindowSizeMs = TimeUnit.MINUTES.toMillis(3);
        long advanceMs = TimeUnit.MINUTES.toMillis(1);
        TimeWindows hoppingWindow = TimeWindows.of(windowSizeMs).advanceBy(advanceMs);
        TimeWindows tumblingWindow = TimeWindows.of(tumblingWindowSizeMs);


        KStream<String, Review> validReviews = kStreamBuilder.stream( "kstreaminput" , Consumed.with(stringSerde, reviewSpecificAvroSerde)).mapValues(val-> changeMessage(val))
                .selectKey(((key, review) -> review.getId().toString()));
        validReviews.to(stringSerde,reviewSpecificAvroSerde,"kstreamoutput");
        KTable<Windowed<String>,Statistics> statisticsKTable = validReviews.groupByKey()
                .aggregate(this::emptyStats, this::statistics,tumblingWindow,statisticsSpecificAvroSerde);
        KStream<String, Statistics> statisticsKStream = statisticsKTable
                .toStream().
                //filter((window,stats) -> keepCurrentWindow(window,advanceMs)).
                        selectKey((k,v) -> k.key());
        statisticsKStream.to(stringSerde,statisticsSpecificAvroSerde,"statsoutput");
        return statisticsKStream;
    }


    private Statistics statistics(String courseId, Review newReview, Statistics currentStats) {
        Statistics.Builder statBuilder = Statistics.newBuilder(currentStats);
        String reviewRating = newReview.getId().toString();
        // increase or decrease?
        Integer incOrDec = (reviewRating.contains("-")) ? -1 : 1;
        Long newCount = statBuilder.getCount() + incOrDec;
        statBuilder.setCount(newCount);
        statBuilder.setLastCountTime(latest(statBuilder.getLastCountTime(),newReview.getCreated()));
        return statBuilder.build();
    }
    private DateTime latest(DateTime a, DateTime b)
    {
        return a.isAfter(b) ? a : b;
    }
    private boolean keepCurrentWindow(Windowed<String> window, long advanceMs) {
        long now = System.currentTimeMillis();
        return window.window().end() > now &&
                window.window().end() < now + advanceMs;
    }
    private Boolean isReviewExpired(Review review, Long maxTime) {
        return review.getCreated().getMillis() + maxTime < System.currentTimeMillis();
    }
    private Statistics emptyStats() {
        return Statistics.newBuilder().setLastCountTime(new DateTime(0L)).build();
    }
    private Review changeMessage(Review oldMessage) {
        Review newMessage = new Review();
        newMessage.setId(oldMessage.getId());
        newMessage.setCreated(oldMessage.getCreated());
        newMessage.setTitle(oldMessage.getTitle() + "-Vimal");
        return newMessage;
    }




}