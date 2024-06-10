package com.poly.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.entity.Wallet;

public interface WalletDAO extends JpaRepository<Wallet, Long>{

}
