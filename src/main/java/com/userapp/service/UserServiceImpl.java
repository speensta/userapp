package com.userapp.service;

import com.userapp.config.Resilience4jConfig;
import com.userapp.dto.UserDto;
import com.userapp.entity.UserEntity;
import com.userapp.exception.FeignErrorDecoder;
import com.userapp.exception.NotFoundException;
import com.userapp.repository.UserRepository;
import com.userapp.vo.RequestUser;
import com.userapp.vo.ResponseOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final Environment env;
    private final OrderRestApiClient orderRestApiClient;
    private final CircuitBreakerFactory circuitBreakerFactory;

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

        UserDto userDto = userRepository.findUserByUserid(requestUser.getUserid())
                .map(m -> modelMapper.map(m, UserDto.class)).orElseThrow(() ->
                        new UsernameNotFoundException(requestUser.getUserid()));

        String url = String.format(env.getProperty("order_service.url"), requestUser.getUserid());

//        ResponseEntity<List<ResponseOrder>> responseEntity = restTemplate.exchange(url,
//                HttpMethod.GET, null, new ParameterizedTypeReference<List<ResponseOrder>>() {
//                });

//        List<ResponseOrder> responseList = orderRestApiClient.getOrders(requestUser.getUserid());
        log.info("start order msa call");
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        List<ResponseOrder> responseList = circuitBreaker.run(() ->
                orderRestApiClient.getOrders(requestUser.getUserid()), throwable -> new ArrayList());
        log.info("end order msa call");

        userDto.setOrders(responseList);

        return userDto;
    }

    @Override
    public UserDto createUser(RequestUser requestUser) {
        requestUser.setUserid(UUID.randomUUID().toString());
        UserEntity userEntity = modelMapper.map(requestUser, UserEntity.class);

        userEntity.setEncryptedPassword(passwordEncoder.encode(requestUser.getPassword()));

        userRepository.save(userEntity);

        return modelMapper.map(userEntity, UserDto.class);

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
