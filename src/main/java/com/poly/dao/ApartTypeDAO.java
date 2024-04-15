package com.poly.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.poly.entity.Account;
import com.poly.entity.ApartType;
import com.poly.entity.Apartment;

public interface ApartTypeDAO extends JpaRepository<ApartType,String>{
	@Query("SELECT o FROM ApartType o WHERE o.id = ?1")
	Optional<ApartType> findById(String id); 
}
