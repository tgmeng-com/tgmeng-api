package com.tgmeng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.tgmeng")
public class TgmengApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TgmengApiApplication.class, args);
    }

}
