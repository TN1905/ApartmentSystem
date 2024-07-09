package com.poly.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.poly.entity.RentApartment;
import com.poly.entity.Wallet;

public interface RentApartmentDAO extends JpaRepository<RentApartment,Long>{
	@Query(value = "SELECT * FROM RentApartment WHERE apartment_id = :id", nativeQuery = true)
    List<RentApartment> findByRentApartment(@Param("id") String id); 
}
