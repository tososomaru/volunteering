package com.tososomaru.volunteering.auth.access;

import com.tososomaru.volunteering.auth.service.AppUserDetailsService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;

@Getter
@Component
public class AuthTokenProvider extends JwtTokenProvider{

    @Value("${auth.cookie.auth}")
    private String name;

    @Value("${auth.cookie.expiration-auth}")
    private Integer expiration;

    protected AuthTokenProvider(AppUserDetailsService userDetailsService) {
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

    public String resolveToken(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(name))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
