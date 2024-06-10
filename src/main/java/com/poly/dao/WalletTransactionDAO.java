package com.poly.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.entity.WalletTransaction;


public interface WalletTransactionDAO extends JpaRepository<WalletTransaction, Long>{

}
