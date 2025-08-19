package com.security.project.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class Accountcontroller {
    @GetMapping("/myAccount")
    public String getAccountDetails() {
        return "Account details from DB";
    }
}
