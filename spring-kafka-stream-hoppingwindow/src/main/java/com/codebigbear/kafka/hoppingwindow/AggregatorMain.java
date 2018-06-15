package com.codebigbear.kafka.hoppingwindow;
import com.typesafe.config.ConfigFactory;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.kstream.internals.WindowedDeserializer;
import org.apache.kafka.streams.kstream.internals.WindowedSerializer;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import com.codebigbear.avro.Review;
import com.codebigbear.avro.Statistics;
public class AggregatorMain {
    private Logger log = LoggerFactory.getLogger(AggregatorMain.class.getSimpleName());
    private AppConfig appConfig;
    public static void main(String[] args) {
        AggregatorMain aggregatorMain = new AggregatorMain();
        aggregatorMain.start();
    }
    private AggregatorMain() {
        appConfig = new AppConfig(ConfigFactory.load());
    }
    private void start() {
        Properties config = getKafkaStreamsConfig();
        KafkaStreams streams = createTopology(config);
        streams.cleanUp();
        streams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
    private Properties getKafkaStreamsConfig() {
        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, appConfig.getApplicationId());
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, appConfig.getBootstrapServers());
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        // we disable the cache to demonstrate all the "steps" involved in the transformation - not recommended in prod
        config.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "0");
        // Exactly once processing!!
        config.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE);
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAvroSerde.class);
        config.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, appConfig.getSchemaRegistryUrl());
        return config;
    }
    private KafkaStreams createTopology(Properties config) {
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
        reviewSpecificAvroSerde.configure(Collections.singletonMap(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081"), false);
        SpecificAvroSerde<Statistics> statisticsSpecificAvroSerde = new SpecificAvroSerde<>();
        statisticsSpecificAvroSerde.configure(Collections.singletonMap(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081"), false);
        Serdes.LongSerde longSerde = new Serdes.LongSerde();
        Serdes.StringSerde stringSerde = new Serdes.StringSerde();

        long windowSizeMs = TimeUnit.MINUTES.toMillis(5);
        long advanceMs = TimeUnit.MINUTES.toMillis(1);
        TimeWindows hoppingWindow = TimeWindows.of(windowSizeMs).advanceBy(advanceMs);

        TimeWindows tumblingWindow = TimeWindows.of(windowSizeMs);
        KStreamBuilder builder = new KStreamBuilder();

        KStream<String, Review> validReviews = builder.stream( stringSerde, reviewSpecificAvroSerde,  "kstreaminput")
                .selectKey(((key, review) -> review.getId().toString()));

        KTable<Windowed<String>,Statistics> statisticsKTable = validReviews.groupByKey()
                .aggregate(this::emptyStats, this::statistics,hoppingWindow,statisticsSpecificAvroSerde);


        KStream<String, Statistics> statisticsKStream = statisticsKTable
                .toStream().
                        filter((window,stats) -> keepCurrentWindow(window,advanceMs)).
                        selectKey((k,v) -> k.key());
        statisticsKStream.to(stringSerde,statisticsSpecificAvroSerde,"statsoutput");
        return new KafkaStreams(builder, config);
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
}