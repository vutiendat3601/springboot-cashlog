package com.dv.cashlog.io.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dv.cashlog.io.entity.RoleEntity;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    @Query(value = "SELECT * FROM roles WHERE roles.name = :name AND is_deleted = 0 LIMIT 1", nativeQuery = true)
    RoleEntity findByName(@Param("name") String name);
}
