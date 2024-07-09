package com.poly.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.poly.entity.Account;
import com.poly.entity.Apartment;
import com.poly.entity.FavoriteApartment;
import com.poly.entity.RentApartment;


public interface ApartmentDAO extends JpaRepository<Apartment,String>{

	@Query(value = "SELECT * FROM apartment WHERE account_id = :account_id", nativeQuery = true)
    List<Apartment> getListApartmentById(Long account_id);
}
