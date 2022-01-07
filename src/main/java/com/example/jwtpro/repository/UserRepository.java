package com.example.jwtpro.repository;

import com.example.jwtpro.entity.HooUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<HooUser, Long>  {
    @EntityGraph(attributePaths = "authorities") //해당쿼리수행시 razy조회말고 eager조회로 받아옴
    Optional<HooUser> findOneWithAuthoritiesByUsername(String username);// username을 기준으로 유저정보와 권한정보를 가져옴
}
