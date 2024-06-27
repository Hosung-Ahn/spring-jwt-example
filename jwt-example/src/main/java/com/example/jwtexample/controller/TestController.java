package com.example.jwtexample.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/user")
    private String user() {
        return "성공";
    }

    @GetMapping("/admin")
    private String admin() {
        return "성공";
    }
}
