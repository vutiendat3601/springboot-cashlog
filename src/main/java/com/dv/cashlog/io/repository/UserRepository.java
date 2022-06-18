package com.dv.cashlog.io.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dv.cashlog.io.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query(value = "SELECT * FROM users WHERE users.user_code = :userCode AND is_deleted = 0 LIMIT 1", nativeQuery = true)
    UserEntity findByUserCode(@Param("userCode") String userCode);

    @Query(value = "SELECT * FROM users WHERE users.email = :email AND is_deleted = 0 LIMIT 1", nativeQuery = true)
    UserEntity findByEmail(String email);
}
