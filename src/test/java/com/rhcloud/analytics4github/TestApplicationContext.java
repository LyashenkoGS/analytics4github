package com.rhcloud.analytics4github;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * A lightweight version of the application for test purposes only
 */
@SpringBootApplication
//Exclude swagger
public class TestApplicationContext {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}