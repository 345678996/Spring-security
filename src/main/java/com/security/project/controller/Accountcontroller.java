package com.security.project.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Accountcontroller {
    @GetMapping("/myAccount")
    public String getAccountDetails() {
        return "Account details from DB";
    }
}
