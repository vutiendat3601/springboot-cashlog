package com.dv.cashlog.io.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dv.cashlog.io.entity.MajorEntity;

@Repository
public interface MajorRepository extends JpaRepository<MajorEntity, Long> {
    MajorEntity findByName(String name);
}
