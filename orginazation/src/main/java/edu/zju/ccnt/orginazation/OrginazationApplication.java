package edu.zju.ccnt.orginazation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.mybatis.spring.annotation.MapperScan;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("edu.zju.ccnt.orginazation.dao")
public class OrginazationApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrginazationApplication.class, args);
    }

}

