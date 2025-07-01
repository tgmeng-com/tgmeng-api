package com.tgmeng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(scanBasePackages = "com.tgmeng")
@EnableAspectJAutoProxy
public class TgmengApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TgmengApiApplication.class, args);
    }

}
