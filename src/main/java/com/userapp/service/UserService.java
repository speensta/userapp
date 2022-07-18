package com.userapp.service;

import com.userapp.dto.UserDto;
import com.userapp.vo.RequestUser;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {
    List<UserDto> findAllUser();
    UserDto loadUserByUserId(RequestUser requestUser);
    void createUser(RequestUser requestUser);
    void updateUser(RequestUser requestUser);

}