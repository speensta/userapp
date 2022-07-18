package com.userapp.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestUser {
    private String userid;
    private String email;
    private String username;
    private String password;
    private String encryptedPassword;
}
