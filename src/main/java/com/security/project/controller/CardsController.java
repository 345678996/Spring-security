package com.security.project.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class CardsController {
    @GetMapping("/myCards")
    public String getCardsDetails() {
        return "Cards details from DB";
    }
}
