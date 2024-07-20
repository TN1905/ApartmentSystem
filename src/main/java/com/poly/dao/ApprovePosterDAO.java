package com.poly.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.poly.entity.Account;
import com.poly.entity.ApprovePoster;
import com.poly.entity.RentApartment;

public interface ApprovePosterDAO extends JpaRepository<ApprovePoster,Long>{
	@Query(value = "SELECT * FROM approveposter WHERE status = 'false'", nativeQuery = true)
    List<ApprovePoster> findApprovePoster(); 
	
	@Query(value = "SELECT * FROM approveposter WHERE account_id = :id", nativeQuery = true)
	ApprovePoster checkApproveByAccount(Long id); 
}
