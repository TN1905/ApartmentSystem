package com.poly.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.poly.entity.Account;
import com.poly.entity.Wallet;

public interface WalletDAO extends JpaRepository<Wallet, Long>{
	@Query(value = "SELECT * FROM wallet WHERE account_id = :id", nativeQuery = true)
    Wallet findByAccount(@Param("id") Long id); 
}
