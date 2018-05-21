package com.krly.project.batterymanagement.batteryserver;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.producer.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 2018/5/21.
 */
public class MessageSender {
    private static volatile MessageSender INSTANCE;
    private MessageSender() {

    }

    public static MessageSender getInstance() {
        if (INSTANCE == null) {
            synchronized (MessageSender.class) {
                INSTANCE = new MessageSender();
            }
        }
        return INSTANCE;
    }

    //===================================================================================
    private Producer<String, String> producer = null;

    //===================================================================================
    public void start()  {
        Properties props = new Properties();
        props.put("bootstrap.servers", "127.0.0.1:9092");
        props.put("request.required.acks", "0");
        props.put("producer.type", "async");

        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("message.send.max.retries", "3");
        props.put("batch.num.messages", "200");
        props.put("send.buffer.bytes", "102400");

        //
        producer = new KafkaProducer<>(props);
    }

    public void send(String value) {
        ProducerRecord<String, String> msg = new ProducerRecord<>("battery", value);
        producer.send(msg);
    }

    public void sendSync(String value) throws Exception {
        ProducerRecord<String, String> msg = new ProducerRecord<String, String>("battery", value);
        Future<RecordMetadata> future = producer.send(msg);
        future.get();
    }

    void test() throws Exception {
        ProducerRecord<String, String> msg = new ProducerRecord<>("battery", "hello");
        Future<RecordMetadata> future = producer.send(msg);
        RecordMetadata recordMetadata = future.get();
    }

    //===================================================================================
    public static void main(String[] args) throws Exception {
        MessageSender.getInstance().start();
        MessageSender.getInstance().test();

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("op", "return");
        paramMap.put("id", 123456);
        paramMap.put("status", 0x04);
        paramMap.put("batteryId", 332211);
        paramMap.put("slotId", 4);
        paramMap.put("timestamp", "lalala");

        String jsonString = JSON.toJSONString(paramMap);
        MessageSender.getInstance().send(jsonString);
    }
}
