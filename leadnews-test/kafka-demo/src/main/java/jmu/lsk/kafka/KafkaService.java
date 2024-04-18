package jmu.lsk.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {
    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    public void send(String message) {
        for(int i =0;i<10;i++){
            kafkaTemplate.send("itcast-topic-input", message);
        }

    }

}
