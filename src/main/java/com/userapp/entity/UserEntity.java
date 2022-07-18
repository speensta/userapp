package com.userapp.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Entity
@Table(name="users")
public class UserEntity {
    @GeneratedValue
    @Id
    private Long id;
    @Column(nullable = false, length = 50, unique = true)
    private String email;
    @Column(nullable = false, length = 50)
    private String username;
    @Column(nullable = false, length = 50, unique = true)
    private String userid;
    @CreatedBy
    private LocalDateTime regdate;
    @LastModifiedBy
    private LocalDateTime moddate;
    private String encryptedPassword;
}
