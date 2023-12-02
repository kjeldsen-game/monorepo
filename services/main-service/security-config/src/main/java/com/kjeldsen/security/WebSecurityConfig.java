package com.kjeldsen.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

//@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WebSecurityConfig {

   /* private final UserDetailsServiceUseCase userDetailsServiceUseCase;
    private final CustomPasswordEncoder customPasswordEncoder;

    @Bean
    protected AuthenticationManager authenticationManagerBean(AuthenticationManagerBuilder auth) throws Exception {
        return auth
            .userDetailsService(userDetailsServiceUseCase)
            .passwordEncoder(customPasswordEncoder)
            .and().build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors()
            .and()
            .csrf()
            .disable()
            .authorizeHttpRequests((auth) -> auth.anyRequest().permitAll());
        return http.build();
    }*/

}
