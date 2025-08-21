package com.security.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.security.project.model.Accounts;
import com.security.project.repository.AccountsRepository;

@RestController
public class Accountcontroller {

    @Autowired
    private AccountsRepository accountsRepository;

    @GetMapping("/myAccount")
    public Accounts getAccountDetails(@RequestParam Long id) {
        Accounts accounts = accountsRepository.findByCustomerId(id);
        if(accounts != null) {
            return accounts;
        } else {
            return null;
        }
    }
}
