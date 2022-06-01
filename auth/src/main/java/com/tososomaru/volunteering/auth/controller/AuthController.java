package com.tososomaru.volunteering.auth.controller;

import com.tososomaru.volunteering.auth.access.AuthTokenProvider;
import com.tososomaru.volunteering.auth.access.RefreshTokenProvider;
import com.tososomaru.volunteering.auth.access.TokenProvider;
import com.tososomaru.volunteering.auth.bean.ErrorResponse;
import com.tososomaru.volunteering.auth.bean.LoginRequest;
import com.tososomaru.volunteering.auth.bean.RegistrationRequest;
import com.tososomaru.volunteering.auth.bean.UserResponse;
import com.tososomaru.volunteering.auth.entity.AppUser;
import com.tososomaru.volunteering.auth.service.LoginService;
import com.tososomaru.volunteering.auth.service.RegistrationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@ResponseBody
@RequestMapping(path = "api/v1/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

    private final RegistrationService registrationService;

    private final LoginService loginService;
    private final AuthTokenProvider authTokenProvider;
    private final RefreshTokenProvider refreshTokenProvider;

    @PostMapping(path = "registration")
    public ResponseEntity<?> registration(@RequestBody RegistrationRequest request, HttpServletResponse response) {
        return buildResponse(
                registrationService.register(request), response
        );
    }

    @PostMapping(path = "login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        return buildResponse(
                loginService.login(loginRequest.email(), loginRequest.password()), response
        );
    }

    private ResponseEntity<?> buildResponse(AppUser user, HttpServletResponse response) {
        try {
            setCookie(user, response, authTokenProvider);
            setCookie(user, response, refreshTokenProvider);
            return buildUserResponse(user);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            clearCookie(response, authTokenProvider);
            clearCookie(response, refreshTokenProvider);
            return buildErrorResponse(e.getLocalizedMessage());
        }
    }

    @GetMapping(path = "current")
    public ResponseEntity<?> current() {
        try {
            AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return buildUserResponse(appUser);
        } catch (NullPointerException e) {
            log.error(e.getLocalizedMessage());
            return buildUserResponse(new AppUser());
        }
    }

    @GetMapping(path = "logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        clearCookie(response, authTokenProvider);
        clearCookie(response, refreshTokenProvider);
        SecurityContextHolder.clearContext();
        return buildUserResponse(new AppUser());
    }

    private void setCookie(AppUser user, HttpServletResponse response, TokenProvider tokenProvider) {
        String token = tokenProvider.createToken(user.getName(), user.getRole().toString());
        Cookie cookie = new Cookie(tokenProvider.getName(), token);
        cookie.setPath(tokenProvider.getPath());
        cookie.setHttpOnly(true);
        cookie.setMaxAge(tokenProvider.getExpiration());
        response.addCookie(cookie);
    }

    private void clearCookie(HttpServletResponse response, TokenProvider tokenProvider) {
        Cookie cookie = new Cookie(tokenProvider.getName(), "-");
        cookie.setPath(tokenProvider.getPath());

        response.addCookie(cookie);
    }

    private ResponseEntity<?> buildUserResponse(AppUser appUser) {
        return ResponseEntity.ok(UserResponse.Mapper.map(appUser));
    }

    private ResponseEntity<?> buildErrorResponse(String message) {
        return ResponseEntity.ok(new ErrorResponse(message));
    }

}
