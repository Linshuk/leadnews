package jmu.lsk.wemedia.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class WeMediaGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(WeMediaGatewayApplication.class,args);
    }
}
