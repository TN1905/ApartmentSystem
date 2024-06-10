package com.poly.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.entity.ConfirmationToken;

public interface ConfirmationTokenDAO extends JpaRepository<ConfirmationToken, Long>{
	ConfirmationToken findByConfirmationToken(String confirmationToken);
}
