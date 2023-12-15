package com.vhc.kafka.consumer;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ConsumerGroupDescription;
import org.apache.kafka.clients.admin.ConsumerGroupListing;
import org.apache.kafka.clients.admin.ListConsumerGroupsResult;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.ConsumerGroupState;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.BrokerNotAvailableException;
import org.apache.kafka.common.errors.DisconnectException;
import org.apache.kafka.common.errors.InvalidTopicException;
import org.apache.kafka.common.errors.TimeoutException;
import org.apache.log4j.Logger;

import com.vhc.model.StructAlarm;
import com.vhc.snmp.SnmpObserver;


public class ConsumerService extends Thread{

    private static Consumer<Long, StructAlarm> consumer = null;
    
    private static Consumer<Long, StructAlarm> consumerTest = null;
    
    private static BlockingQueue<StructAlarm> tempDataQueue = null;
    
    private static boolean _subcribeDataFromKafka = true;

    public ConsumerService() {
    	this.consumer = ConsumerCreator.createConsumer(StructAlarm.class);
    	tempDataQueue = new LinkedBlockingQueue<>();
    }

    public static void consumeMessages(String topicName) throws InterruptedException {
        try {
        consumer.subscribe(Collections.singletonList(topicName));
        } catch (InvalidTopicException e) {
            _subcribeDataFromKafka = false;
        }

        while (_subcribeDataFromKafka) {
        	
        	ConsumerRecords<Long, StructAlarm> consumerRecords = null ;
        	
            Map<TopicPartition, OffsetAndMetadata> offsetsToCommit = new HashMap<>();

            try {
            	consumerRecords = consumer.poll(Duration.ofMillis(1000));
            	
            } catch (TimeoutException e) {
                _subcribeDataFromKafka = false;
            } catch (BrokerNotAvailableException e) {
                _subcribeDataFromKafka = false;
            }  catch (DisconnectException e) {
                _subcribeDataFromKafka = false;
            } catch (Exception e) {
                _subcribeDataFromKafka = false;
            }
            
            	if(consumerRecords.count()>0) {
            	consumerRecords.forEach(record -> {
	            	System.out.println("Record Key: " + record.key() +
	            			   ", Record Value: " + record.value() +
	                    	   ", Record Partition: " + record.partition() +
	                    	   ", Record Offset: " + record.offset());
	            	}
	      			);


            	consumerRecords.forEach(record -> {
            		StructAlarm structAlarm = record.value();
            		tempDataQueue.add(structAlarm);
            	});
            	}
            
            	try {
                    consumer.commitAsync();
               	} catch (Exception e) {
                		commitWithRetry(consumer, e.getMessage());
                }
            	
                while (!tempDataQueue.isEmpty()) {
                    StructAlarm structAlarm = tempDataQueue.poll();
                    SnmpObserver._mAlarmQueueFromSocket.add(structAlarm);
                }


        }
        consumer.close();
    }

    public static void commitWithRetry(Consumer<Long, StructAlarm> consumer, String errorMsg) {
        int retryCount = 0;
        boolean commitSucceeded = false;

        while (!commitSucceeded && retryCount < 5) {
            try {
                consumer.commitAsync();
                commitSucceeded = true;
            } catch (Exception e) {

                System.err.println("Commit failed. Retrying... (Attempt " + (retryCount + 1) + ")");
                e.printStackTrace();

                retryCount++;

                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        if (!commitSucceeded) {
        	while (!tempDataQueue.isEmpty()) {
        		tempDataQueue.poll();  // Loại bỏ phần tử khỏi hàng đợi
        	}
            System.err.println("Reached maximum commit retries. Commit failed." + errorMsg);
        }
    }
    
    public static boolean isConsumerConnected() {
        try {
        	consumerTest = ConsumerCreator.createConsumerTest(StructAlarm.class);
        	consumerTest.poll(Duration.ofMillis(1000));
        	consumerTest.close();
        	return true;
        } catch (TimeoutException e) {
        	consumerTest.close();
            return false;
        } catch (BrokerNotAvailableException e) {
        	consumerTest.close();
            return false;
        }  catch (DisconnectException e) {
        	consumerTest.close();
            return false;
        } catch (Exception e) {
        	consumerTest.close();
            return false;
        }
    }
    
    public static boolean isKafkaServerRunning()
    {
    	boolean _f = true;
    	
    	try
    	{
    		_f = verifyKafkaConnection(KafkaConsumerConfig.KAFKA_CONFIG.getProperty(KafkaConsumerSetting.KAFKA_BROKERS));
    	}
    	catch(Exception e)
    	{
    		_f = false;
    		System.out.println("Error: " + e.getMessage());
    	}
    	return _f;
    }
    // Verify Kafka's running status
    //
    private static boolean verifyKafkaConnection(String bootstrap) throws ExecutionException, InterruptedException
    {
    	AdminClient _client;
    	
    	boolean _f = true;
    	
        Properties props = new Properties();
        
        props.put("bootstrap.servers", bootstrap);
//        
//        props.put("request.timeout.ms", 3000);
//        
//        props.put("connections.max.idle.ms", 5000);
        
// test cho server lag      
      props.put("request.timeout.ms", 30000);
      props.put("connections.max.idle.ms", 30000);


        _client = AdminClient.create(props);
        
        Collection<Node> nodes = _client.describeCluster().nodes().get();
        
        _f = nodes != null && nodes.size() > 0;
        
        return _f;
    }
    
    public static boolean isConsumerServiceRunning() {
    	return _subcribeDataFromKafka;
    }
    
    @Override 
    public void run() {
    	try {
    		_subcribeDataFromKafka = true;
			consumeMessages(KafkaConsumerConfig.KAFKA_CONFIG.getProperty(KafkaConsumerSetting.TOPIC_NAME));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			restart();
			e.printStackTrace();
		}
    }
    
    public static void restart() {
    	consumer = ConsumerCreator.createConsumer(StructAlarm.class);
    	try {
			consumeMessages(KafkaConsumerConfig.KAFKA_CONFIG.getProperty(KafkaConsumerSetting.TOPIC_NAME));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    



	
}
