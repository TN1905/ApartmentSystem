package com.poly.entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
@SuppressWarnings("serial")
@Entity
@Table(name="Wallet")
public class Wallet implements Serializable{
	@Id
	private long id;
	private double balance;
	private int password_payment;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "account_id")
	private Account account;
	@ManyToOne @JoinColumn(name="paymentId")
	private PaymentInfo paymentinfo;
	@OneToMany(mappedBy = "wallet")
	List<WalletTransaction> walletTransaction;
}
