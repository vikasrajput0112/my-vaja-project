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
            <h2>Welcome to my-vaja-project</h2>
            <ul>
                <li><a href="/contact">Contact Us</a></li>
                <li><a href="/about">About Us</a></li>
            </ul>
        """;
    }

    // Contact page
    @GetMapping("/contact")
    public String contactPage() {
        return "contact";
    }

    // About Us page
    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }
}
