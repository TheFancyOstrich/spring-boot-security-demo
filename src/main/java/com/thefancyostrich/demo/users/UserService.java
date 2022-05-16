package com.thefancyostrich.demo.users;

import java.util.List;

import com.thefancyostrich.demo.security.jwt.JwtTokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String registerUser(String username, String password) {
        if (!userRepository.findByUsername(username).isEmpty()) {
            // TODO: Create custom exception
            throw new IllegalStateException(String.format("Username %s is not avalible", username));
        }
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(username, encodedPassword, UserRole.USER);
        userRepository.save(user);
        return jwtTokenProvider.createToken(username, user.getRole());
    }

    public String loginUser(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return jwtTokenProvider.createToken(username, userRepository.findByUsername(username).get().getRole());
        } catch (AuthenticationException e) {
            throw new IllegalStateException("IMPLEMENT CUSTOM HTTP EXCEPTIONS!");
        }

    }

    /**
     * Promote existing user to moderator.
     * Possible endpoint should be locked behind admin.
     * 
     * @param username
     */
    public void promoteUserToModerator(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", username)));
        if (user.getRole() == UserRole.ADMIN) {
            throw new IllegalStateException("Cannot demote ADMIN");
        }
        user.setRole(UserRole.MODERATOR);
        userRepository.save(user);

    }

    public void createAdmin(String username, String password) {
        for (User user : userRepository.findAll()) {
            if (user.getRole() == UserRole.ADMIN) {
                throw new IllegalStateException("This app is only big enough for one ADMIN.");
            }
        }
        userRepository.save(new User(username, passwordEncoder.encode(password), UserRole.ADMIN));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
