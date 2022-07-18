package com.userapp.repository;

import com.userapp.entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findUserByUserid(String userid);
    UserEntity findUserByEmail(String email);
}
