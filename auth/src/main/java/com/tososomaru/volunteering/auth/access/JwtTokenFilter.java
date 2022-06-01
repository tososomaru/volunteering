package com.tososomaru.volunteering.auth.access;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@Component
@AllArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {

    private final AuthTokenProvider authTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = authTokenProvider.resolveToken((HttpServletRequest) request);
        try {
            if (token != null && authTokenProvider.validateToken(token)) {
                Authentication authentication = authTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            SecurityContextHolder.clearContext();
        }

        chain.doFilter(request, response);

    }
}
