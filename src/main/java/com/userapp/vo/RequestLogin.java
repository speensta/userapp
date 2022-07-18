package com.userapp.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class RequestLogin {
    @NotNull(message = "Email is null")
    @Size(min = 2, max = 150, message = "Email less or too large then 8 charactors")
    @Email
    private String email;
    @NotNull(message = "Password  is null")
    @Size(min = 8, max = 12, message = "Password less or too large then 8 charactors")
    private String password;

}
