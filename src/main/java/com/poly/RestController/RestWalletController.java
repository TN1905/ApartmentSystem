package com.poly.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.poly.dao.WalletDAO;
import com.poly.entity.Apartment;
import com.poly.entity.ApartmentImage;
import com.poly.entity.RentApartmentDetail;
import com.poly.entity.Wallet;

@CrossOrigin("*")
@RestController
public class RestWalletController {
	@Autowired
	WalletDAO walletDao;
	
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
			Wallet wallet = walletDao.findById(id).get();
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
	
	@DeleteMapping("/rest/wallet/{id}")
	public void delete(@PathVariable("id") Long id) {
		walletDao.deleteById(id);
	}
}
