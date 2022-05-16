package com.thefancyostrich.demo.security;

import com.thefancyostrich.demo.exceptions.ToHttpException;
import com.thefancyostrich.demo.users.User;
import com.thefancyostrich.demo.users.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserFinderService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserFinderService(UserRepository repository) {
        this.userRepository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new ToHttpException(String.format("User %s not found", username), HttpStatus.NOT_FOUND));

        return user;
    }

}
