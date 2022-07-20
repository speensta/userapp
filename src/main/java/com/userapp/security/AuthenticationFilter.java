package com.userapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.userapp.dto.UserDto;
import com.userapp.service.UserDetailService;
import com.userapp.util.BeanUtil;
import com.userapp.vo.RequestLogin;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private UserDetailService userDetailService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private Environment env;

    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                UserDetailService userDetailService,
                                Environment env) {
        super(authenticationManager);
        this.userDetailService = userDetailService;
        this.env = env;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        RequestLogin requestLogin = null;

        try {
            requestLogin = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);

            BCryptPasswordEncoder bCryptPasswordEncoder = (BCryptPasswordEncoder) BeanUtil.getBean(BCryptPasswordEncoder.class);

            UserDetails userDetails = userDetailService.loadUserByUsername(requestLogin.getEmail());

            if(!bCryptPasswordEncoder.matches(requestLogin.getPassword(), userDetails.getPassword())) {
                log.error("사용자를 찾을 수 없습니다.");
                SecurityContextHolder.getContext().setAuthentication(null);
                response.sendError(HttpStatus.UNAUTHORIZED.value());
                return super.attemptAuthentication(request, response);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(requestLogin.getEmail(),
                requestLogin.getPassword(), new ArrayList<>()));

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {


            String username = ((User) authResult.getPrincipal()).getUsername();

            UserDto userDto = userDetailService.loadUserDetailByEmail(username);

            String secret = env.getProperty("token.secret");
            Key key = Keys.hmacShaKeyFor(secret.getBytes());

            String token = Jwts.builder()
                    .setSubject(userDto.getUserid())
                    .setExpiration(new Date(System.currentTimeMillis() +
                            Long.parseLong(env.getProperty("token.expiration_time"))))
                    .signWith(key, SignatureAlgorithm.HS512)
                    .compact();

            response.setHeader("token", token);
            response.setHeader("userId", userDto.getUserid());

    }


}
