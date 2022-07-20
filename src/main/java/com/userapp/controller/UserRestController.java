package com.userapp.controller;

import com.userapp.dto.UserDto;
import com.userapp.service.UserService;
import com.userapp.vo.Greeting;
import com.userapp.vo.RequestUser;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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

    @GetMapping("/heath-check")
    public String check(HttpServletRequest request) {
        return String.format("health-check : {}", request.getServerPort());
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
    public UserDto findOne(@RequestParam(required = true) String userid) {
        return userService.loadUserByUserId(RequestUser.builder()
                .userid(userid)
                .build());
    }

    @PostMapping("/user")
    public ResponseEntity createUser(@RequestBody RequestUser requestUser) {
        UserDto userDto = modelMapper.map(requestUser, UserDto.class);
        userService.createUser(requestUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @PutMapping("/user")
    public ResponseEntity updateUser(@RequestBody RequestUser requestUser) {
        UserDto userDto = modelMapper.map(requestUser, UserDto.class);
        userService.updateUser(requestUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }
}
