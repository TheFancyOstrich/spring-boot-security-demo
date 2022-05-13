package com.thefancyostrich.demo.users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", username)));

        return user;
    }

    public void registerUser(String username, String password) {
        if (!userRepository.findByUsername(username).isEmpty()) {
            // TODO: Create custom exception
            throw new IllegalStateException(String.format("Username %s is not avalible", username));
        }
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(username, encodedPassword, UserRole.USER);
        userRepository.save(user);
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
