package com.kjeldsen.auth.config;

import com.kjeldsen.auth.User;
import com.kjeldsen.auth.UserRepository;
import java.util.Optional;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    UserRepository userRepository;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String email = (String) principals.getPrimaryPrincipal();
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UnknownAccountException("No account found for user [" + email + "]");
        }
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRoles(user.get().getRoles());
        authorizationInfo.setStringPermissions(user.get().getPermissions());
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
        throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String email = upToken.getUsername();
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UnknownAccountException("No account found for user [" + email + "]");
        }
        return
            new SimpleAuthenticationInfo(
                user.get().getEmail(), user.get().getPassword(), getName());
    }
}