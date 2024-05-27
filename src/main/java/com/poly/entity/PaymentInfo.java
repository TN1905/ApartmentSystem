package com.poly.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="PaymentInfo")
public class PaymentInfo {
	@Id
	private long id;
	private String name;
	private int payment_number;
	@JsonIgnore
	@OneToMany(mappedBy="paymentinfo",fetch=FetchType.EAGER)
	private List<Wallet> wallet;
	
	
}
