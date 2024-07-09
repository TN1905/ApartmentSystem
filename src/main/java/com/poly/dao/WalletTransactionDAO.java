package com.poly.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.poly.entity.Wallet;
import com.poly.entity.WalletTransaction;


public interface WalletTransactionDAO extends JpaRepository<WalletTransaction, Long>{
	@Query(value = "SELECT * FROM wallet_transaction WHERE wallet_id = :id", nativeQuery = true)
    List<WalletTransaction> findTransactionByWallet(Long id); 
}
