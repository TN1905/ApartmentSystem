package com.poly.entity;

import java.io.Serializable;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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
	private String password_payment;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "account_id")
	private Account account;
	@JsonManagedReference
	@OneToMany(mappedBy = "wallet")
	List<WalletTransaction> walletTransaction;
	@JsonManagedReference
    @OneToMany(mappedBy = "wallet")
    private List<PaymentInfo> paymentInfo;
	
	public Boolean checkPassword(String password) {
	    // Nếu mật khẩu được lưu dưới dạng mã hóa
	    // Ví dụ: nếu bạn sử dụng BCrypt
	    return BCrypt.checkpw(password, this.password_payment);
	}
}
