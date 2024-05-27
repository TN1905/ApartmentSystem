package com.poly.entity;

import java.util.Date;

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
public class WalletTransaction {
	@Id
	private long id;
	private String receiver;
	private double amount;
	private String receiver_payment_number;
	@Temporal(TemporalType.DATE)
	@Column(name = "createdate")
	private Date createdate = new Date();
	@ManyToOne @JoinColumn(name="walletId")
	private Wallet wallet;
	@ManyToOne @JoinColumn(name="paymentInfoId")
	private PaymentInfo paymentInfo;
	
}
