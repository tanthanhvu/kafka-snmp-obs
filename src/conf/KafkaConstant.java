package conf;

public interface KafkaConstant {
	
    public static String KAFKA_BROKERS = "192.168.0.117:9092";

    public static Integer MESSAGE_COUNT=1000;

    public static String CLIENT_ID="client1";

    public static String MAIN_GROUP_ID_CONFIG="mainGroup";
    
    public static String TEST_GROUP_ID_CONFIG="testConnectGroup";

    public static Integer MAX_NO_MESSAGE_FOUND_COUNT=100;

    public static String OFFSET_RESET_LATEST="latest";

    public static String OFFSET_RESET_EARLIER="earliest";

    public static Integer MAX_POLL_RECORDS=100;

    public static Integer MAX_COMMIT_RETRIES=6;
    
    public static Integer HEARTBEAT_INTERVAL_MS = 3000;
    
    public static Integer SESSION_TIMEOUT_MS = 45000;
    
    public static Integer MAX_POLL_INTERVAL_MS = 30000;
}