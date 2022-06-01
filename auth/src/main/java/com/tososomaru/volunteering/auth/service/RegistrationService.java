package com.tososomaru.volunteering.auth.service;

import com.tososomaru.volunteering.auth.bean.RegistrationRequest;
import com.tososomaru.volunteering.auth.entity.AppUser;
import com.tososomaru.volunteering.auth.entity.AppUserMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserDetailsService appUserDetailsService;

    public AppUser register(RegistrationRequest request) {
        if (request.email().isEmpty()) {
            throw new BadCredentialsException("Email is not corrected");
        }

        return appUserDetailsService.signUpUser(AppUserMapper.map(request));
    }

}
