package com.dv.cashlog.io.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dv.cashlog.io.entity.BankEntity;

@Repository
public interface BankRepository extends JpaRepository<BankEntity, Long> {
    BankEntity findByName(String name);
}
