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
	
	@Query(value = "select sum(amount) from wallet_transaction where transaction_type = 'RENTED'", nativeQuery = true)
    Double sumTransactionRented(); 
	
	@Query(value = "select sum(amount) from wallet_transaction where transaction_type = 'COMMISSION'", nativeQuery = true)
    Double sumTransactionCommission(); 
	
	@Query(value = "SELECT COUNT(DISTINCT wallet_id) FROM wallet_transaction", nativeQuery = true)
    int countUserRented(); 
	
	@Query(value = "select COALESCE(sum(amount), 0) from wallet_transaction where month(local_date_time) = :month AND transaction_type = 'RENTED'", nativeQuery = true)
	Double getPriceRentedByMonth(int month);
	
	@Query(value = "select COALESCE(sum(amount), 0) from wallet_transaction where month(local_date_time) = :month AND transaction_type = 'COMMISSION'", nativeQuery = true)
	Double getPriceCommissionByMonth(int month);
	
	@Query(value = "select COALESCE(sum(amount), 0) from wallet_transaction where month(local_date_time) = :month AND transaction_type = 'DEPOSITE'", nativeQuery = true)
	Double getPriceDepositeByMonth(int month);
	
	@Query(value = "select COALESCE(sum(amount), 0) from wallet_transaction where month(local_date_time) = :month AND transaction_type = 'WITHDRAW'", nativeQuery = true)
	Double getPriceWithdrawByMonth(int month);
	
	
	
	
}
