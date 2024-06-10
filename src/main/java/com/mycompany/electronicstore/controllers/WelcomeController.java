package com.mycompany.electronicstore.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class WelcomeController {

    @GetMapping("/welcomeUser")
    public String welcomeUser(){
        return "Welcome to ElectronicStore";
    }
}
