package jmu.lsk.kafka;

import org.apache.kafka.common.record.Record;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KafkaConsumer {

    @KafkaListener(topics = "itcast-topic-out",groupId = "kafka-demo")
    public void receive(String record ){
        System.out.println(record);
    }
}
