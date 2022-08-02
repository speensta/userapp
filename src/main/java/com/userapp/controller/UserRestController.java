package com.userapp.controller;

import com.userapp.dto.UserDto;
import com.userapp.service.UserService;
import com.userapp.vo.Greeting;
import com.userapp.vo.RequestUser;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
//@RequestMapping("/user-service")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    private final ModelMapper modelMapper;

    private final Environment env;

    @GetMapping("/heath-check")
    public String check(HttpServletRequest request) {
        return String.format("health-check token.secret = {}", env.getProperty("token.secret")
                + "health-check token.expiration_time = {}", env.getProperty("token.expiration_time")
                + "health-check local.server.port = {}", env.getProperty("local.server.port")
                + "health-check server.port = {}", env.getProperty("server.port"));
    }

    @GetMapping("/welcome")
    public String welcome() {
        Greeting greeting = new Greeting();
        return greeting.getMessage();
    }

    @GetMapping("/user/list")
    public List<UserDto> list() {
        return userService.findAllUser();
    }

    @GetMapping("/user/{userid}")
    public UserDto findOne(@PathVariable(required = false) String userid) {
        return userService.loadUserByUserId(RequestUser.builder()
                .userid(userid)
                .build());
    }

    @GetMapping("/{userid}/user")
    public UserDto findOne2(@PathVariable(required = true) String userid) {
        return userService.loadUserByUserId(RequestUser.builder()
                .userid(userid)
                .build());
    }

    @PostMapping("/user")
    public ResponseEntity createUser(@RequestBody RequestUser requestUser) {
        UserDto resultUserDto = userService.createUser(requestUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultUserDto);
    }

    @PutMapping("/user")
    public ResponseEntity updateUser(@RequestBody RequestUser requestUser) {
        UserDto userDto = modelMapper.map(requestUser, UserDto.class);
        userService.updateUser(requestUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }
}
