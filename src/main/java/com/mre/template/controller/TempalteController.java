package com.mre.template.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Parameter;

@RestController
public class TempalteController {

    @GetMapping(value = "/hello")
    public String helloRequest() {
        return "hi";
    }

    @PostMapping(value = "/fuck")
    public String setValue() {
        return "fuck you";
    }

}
