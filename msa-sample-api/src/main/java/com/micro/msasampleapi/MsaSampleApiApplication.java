package com.micro.msasampleapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@ComponentScan(basePackages = {"com.micro.msaframework.registry","com.micro.msasampleapi"})
public class MsaSampleApiApplication {

    private Logger logger = LoggerFactory.getLogger(MsaSampleApiApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MsaSampleApiApplication.class, args);
    }


    @RequestMapping(name="HelloService", method = RequestMethod.GET, path = "/hello")
    public String hello() {
        logger.info("request start...");
        return "Hello";
    }
}
