package com.krly.project.batterymanagement.batteryserver;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 2018/5/21.
 */
public class MyProducer {
    private static volatile MyProducer INSTANCE;
    private MyProducer() {

    }

    public static MyProducer getInstance() {
        if (INSTANCE == null) {
            synchronized (MyProducer.class) {
                INSTANCE = new MyProducer();
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
        producer = new KafkaProducer<String, String>(props);
    }

    public void send(String value) {
        ProducerRecord<String, String> msg = new ProducerRecord<String, String>("battery", value);
        producer.send(msg);
    }

    public void sendSync(String value) throws Exception {
        ProducerRecord<String, String> msg = new ProducerRecord<String, String>("battery", value);
        Future<RecordMetadata> future = producer.send(msg);
        future.get();
    }

    public void test() throws Exception {
        ProducerRecord<String, String> msg = new ProducerRecord<String, String>("battery", "hello");
        Future<RecordMetadata> future = producer.send(msg);
        RecordMetadata recordMetadata = future.get();
    }

    //===================================================================================
    public static void main(String[] args) throws Exception {
        MyProducer.getInstance().start();
        MyProducer.getInstance().test();
    }
}
