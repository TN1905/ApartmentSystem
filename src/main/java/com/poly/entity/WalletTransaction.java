package com.poly.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SuppressWarnings("serial")
@Table(name="WalletTransaction")
public class WalletTransaction implements Serializable{
	@Id
	private long id;
	private String receiver;
	private double amount;
	private String receiver_payment_number;
	private String txig;
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime localDateTime;
	@JsonBackReference
	@ManyToOne @JoinColumn(name="walletId")
	private Wallet wallet;
	private String transactionType;
	
}
