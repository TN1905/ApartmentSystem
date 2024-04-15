package com.poly.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.poly.entity.Account;
import com.poly.entity.Apartment;
import com.poly.entity.RentApartment;


public interface ApartmentDAO extends JpaRepository<Apartment,String>{
	@Query("SELECT o FROM Apartment o WHERE o.price BETWEEN ?1 AND ?2 AND o.status = true")
	List<Apartment> findByPrice(double minPrice, double maxPrice);
	
	Page<Apartment> findAllByContentLike(String keywords, Pageable pageable);
	
	@Query("SELECT o FROM Apartment o WHERE (o.price BETWEEN ?1 AND ?2) AND o.apartmentType.id = ?3 AND o.cityValue = ?4 AND o.districtValue = ?5 AND o.wardValue = ?6 AND o.status = true")
	List<Apartment> findFilter(double min,double max,String type,int cityValue,int districtValue,int wardValue);

	Page<Apartment> findAllByStatus(Boolean status,Pageable pageable);
}
