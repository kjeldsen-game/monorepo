package com.kjeldsen.auth.usecases;

import com.kjeldsen.auth.persistence.repositories.read.UserViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceUseCase implements UserDetailsService {

    private final UserViewRepository userViewRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userViewRepository.findByUsernameIgnoreCase(username).orElseThrow();
    }
}
