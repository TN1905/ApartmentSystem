package com.poly.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.poly.entity.Account;

public interface AccountDAO extends JpaRepository<Account,Long>{
	@Query("SELECT o FROM Account o WHERE o.username = ?1")
	Account findByUsername(String username); 
	
	@Query("SELECT o FROM Account o WHERE o.email = ?1")
	Account findByEmail(String email); 
	

}
