package com.poly.entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@SuppressWarnings("serial")
@Data
@Entity
@Table(name = "RentApartmentDetail")
public class RentApartmentDetail implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	Double price;
	String vnpBankCode;
	String vnpBankTranNo;
	String vnpCardType;
	String vnpOrderInfo;
	String vnpTransactionStatus;
	
	@ManyToOne @JoinColumn(name = "apartmentId")
	Apartment apartment;
	@ManyToOne @JoinColumn(name = "rentApartmentId")
	RentApartment rentApartment;
}
