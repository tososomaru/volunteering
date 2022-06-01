package com.tososomaru.volunteering.auth.access;

import lombok.AllArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.savedrequest.RequestCacheAwareFilter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class JwtTokenConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final JwtTokenFilter jwtTokenFilter;

    @Override
    public void configure(HttpSecurity security) throws Exception {
        security.addFilterBefore(jwtTokenFilter, RequestCacheAwareFilter.class);
    }
}
