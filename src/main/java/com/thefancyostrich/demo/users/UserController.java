package com.thefancyostrich.demo.users;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
/**
 * !!! All requests in this controller are by default permitAll !!!.
 */
public class UserController {

    @Autowired
    UserDetailsServiceImplementation userService;

    /**
     * To register a user the body should be of the form
     * 
     * {
     * username: <username>,
     * password: <password>
     * }
     * 
     * @param body
     */
    @PostMapping("/register")
    public void registerUser(@RequestBody JsonNode body) {
        if (body.has("username") && body.has("password")) {
            String username = body.get("username").textValue();
            String password = body.get("password").textValue();
            userService.registerUser(username, password);
        } else {
            throw new IllegalStateException("Invalid request.");
        }
    }

    /**
     * TODO: implement
     * To login a user the body should be of the form
     * 
     * {
     * username: <username>,
     * password: <password>
     * }
     * 
     * @param body
     */
    @PostMapping("/login")
    public void login(@RequestBody JsonNode body) {
        if (body.has("username") && body.has("password")) {
            String username = body.get("username").textValue();
            String password = body.get("password").textValue();
            username += password;
            password += username;
        } else {
            throw new IllegalStateException("Invalid request.");
        }
    }

    @GetMapping("/test")
    @PreAuthorize("hasAuthority('user:modify')")
    public String test() {
        return "Test";
    }

    @GetMapping("/get/all")
    @PreAuthorize("hasAuthority('user:read')")
    public List<User> getAll() {
        return userService.getAllUsers();
    }

}
