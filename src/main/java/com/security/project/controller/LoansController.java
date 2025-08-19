package com.security.project.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class LoansController {
    @GetMapping("/myLoans")
    public String getLoansDetails() {
        return "Loans details from DB";
    }
}
