package com.infosys.controller;

import com.infosys.service.UserService;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class LoginController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String passwordHash) {
        return userService.login(username, passwordHash).toString();

    }

    @PostMapping("/signup")
    public String createUser(@RequestParam String newUsername, @RequestParam String newPasswordHash) {
        return userService.createUser(newUsername, newPasswordHash).toString();
    }

    @PostMapping("/logout")
    public String logout(@RequestParam String username) {
        return userService.logout(username).toString();
    }
}
