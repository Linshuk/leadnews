package jmu.lsk.kafka;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest(classes = KafkaApplication.class)
@RunWith(SpringRunner.class)
public class KafkaServiceTest {
    @Autowired
    KafkaService kafkaService;

    @Test
    public void sendMessage(){
        kafkaService.send("hello world");
        kafkaService.send("hello itcast");
    }
}