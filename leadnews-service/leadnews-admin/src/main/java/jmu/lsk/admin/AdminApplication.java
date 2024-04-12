package jmu.lsk.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient//集成注册中心
@MapperScan("jmu.lsk.admin.mapper")

public class AdminApplication {
        public static void main(String[] args) {
            SpringApplication.run(AdminApplication.class, args);
        }
}
