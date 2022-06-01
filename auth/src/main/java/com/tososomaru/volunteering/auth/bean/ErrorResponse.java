package com.tososomaru.volunteering.auth.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ErrorResponse extends BaseResponse{
    private String message;

    public ErrorResponse(String message) {
        this.ok = false;
        this.message = message;
    }
}
