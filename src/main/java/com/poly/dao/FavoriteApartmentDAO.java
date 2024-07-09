package com.poly.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.poly.entity.AccountSession;
import com.poly.entity.FavoriteApartment;

public interface FavoriteApartmentDAO extends JpaRepository<FavoriteApartment,Long>{
	@Query(value = "SELECT * FROM favorite_apartment WHERE account_id = :account_id AND apartment_id = :apartment_id", nativeQuery = true)
    FavoriteApartment findFavoriteApartment(@Param("account_id") long account_id, @Param("apartment_id") String apartment_id);
	
	@Query(value = "SELECT * FROM favorite_apartment WHERE account_id = :account_id", nativeQuery = true)
	List<FavoriteApartment> getFavoriteApart(@Param("account_id") long account_id);
}
