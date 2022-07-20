package com.userapp.service;

import com.userapp.dto.UserDto;
import com.userapp.entity.UserEntity;
import com.userapp.exception.NotFoundException;
import com.userapp.repository.UserRepository;
import com.userapp.vo.RequestUser;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserDto> findAllUser() {
        List<UserDto> list = new ArrayList<>();
        userRepository.findAll().forEach(m -> {
            list.add(modelMapper.map(m, UserDto.class));
        });
        return list;
    }

    @Override
    public UserDto loadUserByUserId(RequestUser requestUser) {
        return userRepository.findUserByUserid(requestUser.getUserid())
                .map(m -> modelMapper.map(m, UserDto.class)).orElseThrow(() ->
                        new UsernameNotFoundException(requestUser.getUserid()));
    }

    @Override
    public void createUser(RequestUser requestUser) {
        requestUser.setUserid(UUID.randomUUID().toString());
        UserEntity userEntity = modelMapper.map(requestUser, UserEntity.class);

        userEntity.setEncryptedPassword(passwordEncoder.encode(requestUser.getPassword()));

        userRepository.save(userEntity);

    }

    @Override
    public void updateUser(RequestUser requestUser) {
        UserEntity userEntity = userRepository.findUserByUserid(requestUser.getUserid())
                .orElseThrow(() -> new NotFoundException("잘못된요청 입니다."));

        userEntity.setEmail(requestUser.getEmail());
        userEntity.setUsername(requestUser.getUsername());
        userEntity.setEncryptedPassword(requestUser.getEncryptedPassword());
    }
}
