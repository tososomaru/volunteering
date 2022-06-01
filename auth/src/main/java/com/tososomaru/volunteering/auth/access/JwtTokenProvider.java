package com.tososomaru.volunteering.auth.access;

import com.tososomaru.volunteering.auth.entity.AppUser;
import com.tososomaru.volunteering.auth.service.AppUserDetailsService;
import io.jsonwebtoken.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.function.UnaryOperator;

@Getter
@Slf4j
public abstract class JwtTokenProvider implements TokenProvider {

    @Getter(AccessLevel.NONE)
    private final AppUserDetailsService userDetailsService;

    @Getter(AccessLevel.NONE)
    @Value("${auth.cookie.secret}")
    private String secretKey;

    @Value("${auth.cookie.path}")
    protected String cookiePath;

    protected JwtTokenProvider(AppUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    public void init(){
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    protected String buildJwt(String userName, String role, UnaryOperator<Date> function) {

        Claims claims = Jwts.claims().setSubject(userName);
        claims.put("role", role);
        Date iat = new Date();
        Date exp = function.apply(iat);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(iat)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            return getClaimsFromToken(token)
                    .getExpiration()
                    .after(new Date());
        } catch (ExpiredJwtException eje) {
            log.error(eje.getLocalizedMessage());
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        AppUser appUser = userDetailsService.loadUserByUsername(getUserName(token));
        return new UsernamePasswordAuthenticationToken(appUser, appUser.getPassword(), appUser.getAuthorities());
    }

    private String getUserName(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }
}
