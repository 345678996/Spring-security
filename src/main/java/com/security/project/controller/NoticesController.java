package com.security.project.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class NoticesController {
    @GetMapping("/notices")
    public String getNotices() {
        return "Notices from DB";
    }
}
