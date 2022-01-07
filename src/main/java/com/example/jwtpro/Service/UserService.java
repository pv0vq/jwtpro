package com.example.jwtpro.Service;

import com.example.jwtpro.dto.UserDto;
import com.example.jwtpro.entity.HooAuthority;
import com.example.jwtpro.entity.HooUser;
import com.example.jwtpro.exception.DuplicateMemberException;
import com.example.jwtpro.jwt.SecurityUtil;
import com.example.jwtpro.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserDto signup(UserDto userDto) { //회원가입 서비스
        if (userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) { //유저정보 확인
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        HooAuthority authority = HooAuthority.builder() // 없으면 유저권한을 만듬
                .authorityName("ROLE_USER")
                .build();

        HooUser user = HooUser.builder() // 유정정보를 만들어서
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        return UserDto.from(userRepository.save(user)); // dto에 실어서 userRepository를 이용해서db에 저장
    }

    @Transactional(readOnly = true)
    public UserDto getUserWithAuthorities(String username) { //username을 기준으로 정보를 가져옴
        return UserDto.from(userRepository.findOneWithAuthoritiesByUsername(username).orElse(null));
    }

    @Transactional(readOnly = true)
    public UserDto getMyUserWithAuthorities() { // SecurityContext에 저장된 username의 정보만 가져옴
        return UserDto.from(SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername).orElse(null));
    }
}
