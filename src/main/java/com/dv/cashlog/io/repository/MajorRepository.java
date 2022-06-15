package com.dv.cashlog.io.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dv.cashlog.io.entity.MajorEntity;

public interface MajorRepository extends JpaRepository<MajorEntity, Long> {
    MajorEntity findByName(String name);
}
