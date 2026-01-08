package com.example.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class MyVajaProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyVajaProjectApplication.class, args);
    }

    @GetMapping("/")
    public String home() {
        return "Hello from my-vaja-project (Spring Boot)!";
    }
}
