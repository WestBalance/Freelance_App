package com.tasklink;

import org.springframework.boot.SpringApplication;
import com.tasklink.patterns.structural.StripeConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(StripeConfig.class)
public class TaskLinkApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaskLinkApplication.class, args);
    }
}

