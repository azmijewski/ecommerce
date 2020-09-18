package com.zmijewski.ecommerce.service;

import com.zmijewski.ecommerce.model.Role;
import com.zmijewski.ecommerce.model.User;
import com.zmijewski.ecommerce.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findActivatedUserByEmail(s)
                .map(this::mapUserToUserDetail)
                .orElseThrow(() -> new UsernameNotFoundException("Could not find active user with email: " + s));
    }

    private UserDetails mapUserToUserDetail(User user) {
        Role role = user.getRole();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getName());
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(), Collections.singletonList(authority));

    }
}
