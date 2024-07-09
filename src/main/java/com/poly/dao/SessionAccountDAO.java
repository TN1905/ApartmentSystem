package com.poly.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.poly.entity.Account;
import com.poly.entity.AccountSession;
import com.poly.entity.RentApartment;

public interface SessionAccountDAO extends JpaRepository<AccountSession,Integer> {
	@Query(value = "SELECT * FROM sessions WHERE account_id = :id", nativeQuery = true)
    AccountSession findSessionByAccountId(@Param("id") long id);

	@Query(value = "UPDATE sessions SET session_token = :session, expires = :expires WHERE account_id = :accountId", nativeQuery = true)
	AccountSession updateSession(Long accountId, String session, Long expires); 
	
	
}
