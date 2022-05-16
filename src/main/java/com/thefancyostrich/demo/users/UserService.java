package com.thefancyostrich.demo.users;

import java.util.List;

import com.thefancyostrich.demo.exceptions.ToHttpException;
import com.thefancyostrich.demo.security.jwt.JwtTokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
            throw new ToHttpException(String.format("Username %s is not avalible", username), HttpStatus.BAD_REQUEST);
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
            throw new ToHttpException("Invalid username or password", HttpStatus.UNAUTHORIZED);
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
            throw new ToHttpException("Cannot demote ADMIN", HttpStatus.BAD_REQUEST);
        }
        user.setRole(UserRole.MODERATOR);
        userRepository.save(user);

    }

    public void createAdmin(String username, String password) {
        for (User user : userRepository.findAll()) {
            if (user.getRole() == UserRole.ADMIN) {
                throw new ToHttpException("This app is only big enough for one ADMIN.", HttpStatus.BAD_REQUEST);
            }
        }
        userRepository.save(new User(username, passwordEncoder.encode(password), UserRole.ADMIN));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
