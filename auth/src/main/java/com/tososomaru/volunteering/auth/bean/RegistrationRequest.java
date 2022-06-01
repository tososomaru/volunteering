package com.tososomaru.volunteering.auth.bean;

public record RegistrationRequest(String name, String email, String password, boolean checked) {

}
