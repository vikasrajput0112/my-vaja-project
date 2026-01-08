package com.example.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
@Controller
public class MyVajaProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyVajaProjectApplication.class, args);
    }

    // Home page
    @GetMapping("/")
    @ResponseBody
    public String home() {
        return """
            <h2>Hello from my-vaja-project (Spring Boot)</h2>
            <a href="/contact">Go to Contact Page</a>
        """;
    }

    // Contact page (HTML)
    @GetMapping("/contact")
    public String contactPage() {
        return "contact"; // maps to contact.html
    }
}
