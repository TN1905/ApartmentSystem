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

import com.poly.dao.PaymentInfoDAO;

import com.poly.entity.PaymentInfo;


@CrossOrigin("*")
@RestController
public class RestPaymentInfoController {
	@Autowired
	PaymentInfoDAO paymentInfoDao;
	
	@GetMapping("/rest/paymentinfo")
	public ResponseEntity<List<PaymentInfo>> getAll(Model model){
		try {
			List<PaymentInfo> list = paymentInfoDao.findAll();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
	}
	
	@GetMapping("/rest/paymentinfo/{id}")
	public ResponseEntity<PaymentInfo> getOne(@PathVariable("id") Long id) {
		try {
			PaymentInfo paymentInfo = paymentInfoDao.findById(id).get();
			return ResponseEntity.ok(paymentInfo);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PostMapping("/rest/paymentinfo")
	public ResponseEntity<PaymentInfo> post(@RequestBody PaymentInfo paymentInfo) {
		try {
			PaymentInfo payInfo = paymentInfoDao.save(paymentInfo);
			return ResponseEntity.ok(payInfo);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}
	
	@PutMapping("/rest/paymentinfo/{id}")
	public ResponseEntity<PaymentInfo> put(@PathVariable("id") Long id,@RequestBody PaymentInfo paymentInfo) {
		try {
			PaymentInfo payInfo = paymentInfoDao.save(paymentInfo);
			return ResponseEntity.ok(payInfo);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
	}
	
	@DeleteMapping("/rest/paymentinfo/{id}")
	public void delete(@PathVariable("id") Long id) {
		paymentInfoDao.deleteById(id);
	}
}
