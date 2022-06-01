package com.tososomaru.volunteering.auth.entity;

import com.tososomaru.volunteering.auth.bean.RegistrationRequest;

public class AppUserMapper {
    public static AppUser map(RegistrationRequest request) {
        return new AppUser(request.name(), request.email(), request.password());
    }
}
