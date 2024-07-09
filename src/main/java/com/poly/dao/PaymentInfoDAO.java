package com.poly.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.entity.PaymentInfo;

public interface PaymentInfoDAO extends JpaRepository<PaymentInfo, Long>{

}
