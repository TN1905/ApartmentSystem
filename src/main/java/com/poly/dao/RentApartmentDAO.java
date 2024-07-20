package com.poly.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.poly.entity.RentApartment;
import com.poly.entity.Wallet;

public interface RentApartmentDAO extends JpaRepository<RentApartment,Long>{
	@Query(value = "SELECT * FROM rent_apartment WHERE apartment_id = :id", nativeQuery = true)
    List<RentApartment> findByRentApartment(@Param("id") String id); 
	
	@Query(value = "SELECT * FROM rent_apartment WHERE account_id = :id", nativeQuery = true)
    List<RentApartment> findRentedByAccount(Long id); 
	
	@Query(value = "SELECT * FROM rent_apartment WHERE endate BETWEEN :startDate AND :endDate AND alert_expire = 'false' ", nativeQuery = true)
    List<RentApartment> getRentedWithEndDate(LocalDateTime startDate,LocalDateTime endDate);

}
