package com.vhc.kafka.consumer;

public class KafkaConsumerSetting {
	//Config kafka consumer
    public static final String KAFKA_BROKERS = "kafkaBrokers";
    public static final String MESSAGE_COUNT="messageCount";
    public static final String CLIENT_ID="clientId";
    public static final String MAIN_GROUP_ID_CONFIG="mainGroupIdConfig";
    public static final String TEST_GROUP_ID_CONFIG="testGroupIdConfig";
    public static final String OFFSET_RESET_LATEST="latest";
    public static final String OFFSET_RESET_EARLIER="earliest";
    public static final String MAX_POLL_RECORDS="maxPollRecords";
    public static final String MAX_COMMIT_RETRIES="maxCommitRetries";
    public static final String HEARTBEAT_INTERVAL_MS = "heartbeatIntervalMs";
    public static final String SESSION_TIMEOUT_MS = "sessionTimeoutMs";
    public static final String MAX_POLL_INTERVAL_MS = "maxPollIntervalMs";
    public static final String TOPIC_NAME = "topicName";
}
