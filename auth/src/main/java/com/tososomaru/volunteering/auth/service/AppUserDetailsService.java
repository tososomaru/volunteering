package com.tososomaru.volunteering.auth.service;

import com.tososomaru.volunteering.auth.access.PasswordConfig;
import com.tososomaru.volunteering.auth.bean.RegistrationRequest;
import com.tososomaru.volunteering.auth.entity.AppUser;
import com.tososomaru.volunteering.auth.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordConfig passwordConfig;

    @Override
    public AppUser loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public AppUser signUpUser(AppUser appUser) {
        boolean userExists = userRepository.findByEmail(appUser.getEmail()).isPresent();
        if (userExists) {
            throw new IllegalStateException("Email is already taken");
        }
        String encodedPassword = passwordConfig.passwordEncoder().encode(appUser.getPassword());

        appUser.setPassword(encodedPassword);
        appUser.setEnabled(true);
        return userRepository.save(appUser);
    }

    public AppUser retrieveFromCache(String email) {
        return (AppUser) new CachingUserDetailsService(this).loadUserByUsername(email);
    }
}
