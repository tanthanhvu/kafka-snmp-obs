package com.vhc.kafka.consumer;

import conf.KafkaConstant;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.vhc.model.AlarmType;

import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;

public class ConsumerCreator {
    public static <T> Consumer<Long, T> createConsumer(Class<T> targetType) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConsumerConfig.KAFKA_CONFIG.getProperty(KafkaConsumerSetting.KAFKA_BROKERS));
        props.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConsumerConfig.KAFKA_CONFIG.getProperty(KafkaConsumerSetting.MAIN_GROUP_ID_CONFIG));
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, KafkaConsumerConfig.KAFKA_CONFIG.getProperty(KafkaConsumerSetting.MAX_POLL_RECORDS));
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, KafkaConsumerSetting.OFFSET_RESET_EARLIER);
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, KafkaConsumerConfig.KAFKA_CONFIG.getProperty(KafkaConsumerSetting.HEARTBEAT_INTERVAL_MS));
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, KafkaConsumerConfig.KAFKA_CONFIG.getProperty(KafkaConsumerSetting.SESSION_TIMEOUT_MS));
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, KafkaConsumerConfig.KAFKA_CONFIG.getProperty(KafkaConsumerSetting.MAX_POLL_INTERVAL_MS));

        return new KafkaConsumer<>(props, new LongDeserializer(), new JsonDeserializer<>(targetType));
    }
    
    public static <T> Consumer<Long, T> createConsumerTest(Class<T> targetType) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConsumerConfig.KAFKA_CONFIG.getProperty(KafkaConsumerSetting.KAFKA_BROKERS));
        props.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConsumerConfig.KAFKA_CONFIG.getProperty(KafkaConsumerSetting.TEST_GROUP_ID_CONFIG));
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, KafkaConsumerConfig.KAFKA_CONFIG.getProperty(KafkaConsumerSetting.MAX_POLL_RECORDS));
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, KafkaConsumerSetting.OFFSET_RESET_EARLIER);
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, KafkaConsumerConfig.KAFKA_CONFIG.getProperty(KafkaConsumerSetting.HEARTBEAT_INTERVAL_MS));
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, KafkaConsumerConfig.KAFKA_CONFIG.getProperty(KafkaConsumerSetting.SESSION_TIMEOUT_MS));

        return new KafkaConsumer<>(props, new LongDeserializer(), new JsonDeserializer<>(targetType));
    }
}
