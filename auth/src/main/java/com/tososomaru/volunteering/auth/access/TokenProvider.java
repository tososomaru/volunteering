package com.tososomaru.volunteering.auth.access;

public interface TokenProvider {

    Integer getExpiration();

    String getName();

    String getPath();

    String createToken(String userName, String role);
}
