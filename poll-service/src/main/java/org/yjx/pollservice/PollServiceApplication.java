package org.yjx.pollservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("org.yjx.pollservice.mapper")
@SpringBootApplication
public class PollServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PollServiceApplication.class, args);
    }

}
