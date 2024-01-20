package com.kjeldsen.auth;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.env.BasicIniEnvironment;
import org.apache.shiro.env.Environment;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.util.Factory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();

        // logged-in users with the 'admin' role
        chainDefinition.addPathDefinition("/admin/**", "authc, roles[admin]");

        // logged-in users with the 'document:read' permission
        chainDefinition.addPathDefinition("/docs/**", "authc, perms[document:read]");

        // all users can access the auth and home pages and static assets
        // chainDefinition.addPathDefinition("/", "anon");
        // chainDefinition.addPathDefinition("/auth/**", "anon");
        // chainDefinition.addPathDefinition("/static/**", "anon");
        // chainDefinition.addPathDefinition("/swagger-ui/**", "anon");
        // chainDefinition.addPathDefinition("/v2/**", "anon");

        // TODO disable endpoints based on role - keeping everything open for now until frontend is ready
        chainDefinition.addPathDefinition("/**", "anon");
        return chainDefinition;
    }

    @Bean
    public PasswordService passwordService() {
        return new DefaultPasswordService();
    }

    @Bean
    public Realm realm() {
        return new CustomRealm();
    }

    @Bean
    public SecurityManager securityManager() {
        return new CustomSecurityManager(realm());
    }
}
