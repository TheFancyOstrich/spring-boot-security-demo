package com.thefancyostrich.demo;

import com.thefancyostrich.demo.users.UserDetailsServiceImplementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class UserLoader implements ApplicationRunner {
    /**
     * Add some users. Should not be used in production.
     */
    @Autowired
    UserDetailsServiceImplementation userService;

    @Override
    public void run(ApplicationArguments args) {
        userService.createAdmin("Admin", "admin");
        userService.registerUser("User1", "user1");
        userService.registerUser("User2", "user2");
        userService.registerUser("Mod", "mod");
        userService.promoteUserToModerator("Mod");
    }
}
