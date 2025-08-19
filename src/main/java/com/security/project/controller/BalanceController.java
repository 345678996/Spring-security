package com.security.project.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class BalanceController {
    @GetMapping("/myBalance")
    public String getBalanceDetails() {
        return "Balance details from DB";
    }
}
