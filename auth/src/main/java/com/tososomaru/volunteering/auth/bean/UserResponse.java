package com.tososomaru.volunteering.auth.bean;

import com.tososomaru.volunteering.auth.entity.AppUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UserResponse extends BaseResponse {
    private String name;
    private String email;
    private boolean enabled;

    public static class Mapper {
        public static UserResponse map(AppUser user) {
            return new UserResponse(user.getName(), user.getEmail(), user.isEnabled());
        }
    }
}
