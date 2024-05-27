package com.poly.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.entity.ApartmentImage;

public interface ApartmentImageDAO extends JpaRepository<ApartmentImage, Long>{

	List<ApartmentImage> findByApartmentId(String apartmentId);

}
