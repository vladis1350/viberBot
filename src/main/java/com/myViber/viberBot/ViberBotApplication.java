package com.myViber.viberBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class ViberBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(ViberBotApplication.class, args);
    }

}
