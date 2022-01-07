package com.example.jwtpro.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto { // 로그인 dto

    @NotNull
    @Size(min = 3, max = 50) // 발리데이션 dto
    private String username;

    @NotNull
    @Size(min = 3, max = 100)
    private String password;
}
