package com.dv.cashlog.io.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dv.cashlog.io.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUserCode(String userCode);
    UserEntity findByEmail(String email);
}
