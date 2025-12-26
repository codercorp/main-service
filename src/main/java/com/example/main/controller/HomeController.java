package com.example.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Forward root (/) to static index.html in src/main/resources/static/index.html
    @GetMapping("/")
    public String index() {
        return "forward:index.html";
    }
}
