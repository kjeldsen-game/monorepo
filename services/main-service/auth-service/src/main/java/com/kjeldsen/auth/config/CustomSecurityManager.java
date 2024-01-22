package com.kjeldsen.auth.config;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.stereotype.Component;

@Component
public class CustomSecurityManager extends DefaultWebSecurityManager {

    public CustomSecurityManager(Realm realm) {
        super(realm);
        CookieRememberMeManager rmm = new CookieRememberMeManager();
        // TODO set key in env variable in production
        rmm.setCipherKey("kPH+bIxk5D2deZiIxcaaaA==".getBytes());
        setRememberMeManager(rmm);
    }
}
