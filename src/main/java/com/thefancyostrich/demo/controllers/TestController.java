package com.thefancyostrich.demo.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('test:admin')")
    public String admin() {
        return "Hi admin.";
    }

    @GetMapping("/mod")
    @PreAuthorize("hasAuthority('test:moderator')")
    public String mod() {
        return "Hi mod";
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('test:user')")
    public String user() {
        return "Hi user!";
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello!";
    }

    @GetMapping("/ann")
    @PreAuthorize("hasAuthority('test:moderator')") // hasRole/anyRolw or hasAuthority/hasAnyAuthority
    public String ann() {
        return "Hello mod or admin";
    }
}
