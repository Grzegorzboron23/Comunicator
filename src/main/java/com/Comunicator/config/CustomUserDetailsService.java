package com.Comunicator.config;

import com.Comunicator.model.User;
import com.Comunicator.respository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;



    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {

        User user = userRepository.findByName(name);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with name: " + name);
        }

        return new org.springframework.security.core.userdetails.User(
                name,
                user.getPassword(),
                List.of()
        );
    }
}