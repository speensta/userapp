package com.userapp.service;

import com.userapp.dto.UserDto;
import com.userapp.vo.RequestUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService {

    List<UserDto> findAllUser();
    UserDto loadUserByUserId(RequestUser requestUser);
    void createUser(RequestUser requestUser);
    void updateUser(RequestUser requestUser);


}