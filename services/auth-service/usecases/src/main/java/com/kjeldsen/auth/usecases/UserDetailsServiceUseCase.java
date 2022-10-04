package com.kjeldsen.auth.usecases;

import com.kjeldsen.auth.persistence.SignUpReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceUseCase implements UserDetailsService {

    private final SignUpReadRepository signUpReadRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return signUpReadRepository.findByUsernameIgnoreCase(username)
            .orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", username)));
    }
}
