package com.archu.homebudgetmanager.service;

import com.archu.homebudgetmanager.model.User;
import com.archu.homebudgetmanager.model.UserDetailsImpl;
import com.archu.homebudgetmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);


        if (user == null) {
            throw new UsernameNotFoundException("User not found.");
        }

        return new UserDetailsImpl(user);
    }
}
