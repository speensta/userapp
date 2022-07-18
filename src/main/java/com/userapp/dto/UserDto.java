package com.userapp.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class UserDto {
    @NotNull(message = "이메일을 입혁하세요.")
    private String email;
    private String username;
    @NotNull(message = "아이디를 입혁하세요.")
    private String userid;
    private LocalDateTime regdate;
    @NotNull(message = "패스워드를 입혁하세요.")
    private String password;
    private String encryptedPassword;

}
