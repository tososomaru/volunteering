package com.tososomaru.volunteering.auth.access;

import com.tososomaru.volunteering.auth.service.AppUserDetailsService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Getter
@Component
public class RefreshTokenProvider extends JwtTokenProvider{

    @Value("${auth.cookie.refresh}")
    private String name;

    @Value("${auth.cookie.expiration-refresh}")
    private Integer expiration;

    protected RefreshTokenProvider(AppUserDetailsService userDetailsService) {
        super(userDetailsService);
    }

    @Override
    public String getPath() {
        return cookiePath;
    }

    @Override
    public String createToken(String userName, String role) {
        return buildJwt(userName, role, iat -> new Date(iat.getTime() + expiration));
    }
}
