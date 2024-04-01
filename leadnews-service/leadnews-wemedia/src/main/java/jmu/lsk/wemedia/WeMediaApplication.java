package jmu.lsk.wemedia;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("jmu.lsk.wemedia.mapper")
public class WeMediaApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeMediaApplication.class,args);
    }
}