package com.poly.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poly.dao.WalletDAO;
import com.poly.entity.Wallet;

import jakarta.persistence.Transient;

@CrossOrigin("*")
@RestController
public class RestWalletController {
	@Autowired
	WalletDAO walletDao;
	
	@Transient
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
	
	@GetMapping("/rest/wallet")
	public ResponseEntity<List<Wallet>> getAll(Model model){
		try {
			List<Wallet> list = walletDao.findAll();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
	}
	
	@GetMapping("/rest/wallet/{id}")
	public ResponseEntity<Wallet> getOne(@PathVariable("id") Long id) {
		try {
			Wallet wallet = walletDao.findByAccount(id);
			return ResponseEntity.ok(wallet);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PostMapping("/rest/wallet")
	public ResponseEntity<Wallet> post(@RequestBody Wallet wallet) {
		try {
			Wallet wal = walletDao.save(wallet);
			return ResponseEntity.ok(wal);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}
	
	@PutMapping("/rest/wallet/{id}")
	public ResponseEntity<Wallet> put(@PathVariable("id") Long id,@RequestBody Wallet wallet) {
		try {
			Wallet wal = walletDao.save(wallet);
			return ResponseEntity.ok(wal);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
	}
	
	@PutMapping("/rest/passwordpayment/{id}")
	public ResponseEntity<Wallet> updatePasswordPayment(@PathVariable("id") Long id,@RequestBody String passwordPayment) {
		System.out.println(passwordPayment);
		try {
			Wallet wal = walletDao.findById(id).get();
			if(wal!= null) {
				wal.setPassword_payment(PASSWORD_ENCODER.encode(passwordPayment));
				walletDao.save(wal);
			}
			return ResponseEntity.ok(wal);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
	}
	
	@GetMapping("/rest/checkPasswordPayment/{id}")
	public ResponseEntity<Boolean> checkPasswordPayment(@PathVariable("id") Long id, @RequestParam("passwordPayment") String passwordPayment) {
	    try {
	        Wallet wal = walletDao.findById(id).orElse(null);
	        if (wal != null) {
	            // Kiểm tra mật khẩu
	            Boolean check = wal.checkPassword(passwordPayment);
	            System.out.println("Received password: " + passwordPayment);
	            System.out.println("Password check result: " + check);
	            return ResponseEntity.ok(check);
	        } else {
	            return ResponseEntity.ok(false);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}
	
	@DeleteMapping("/rest/wallet/{id}")
	public void delete(@PathVariable("id") Long id) {
		walletDao.deleteById(id);
	}
}
