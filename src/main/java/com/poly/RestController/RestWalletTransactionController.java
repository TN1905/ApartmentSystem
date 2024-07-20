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

import com.poly.dao.WalletTransactionDAO;
import com.poly.entity.Wallet;
import com.poly.entity.WalletTransaction;

@CrossOrigin("*")
@RestController
public class RestWalletTransactionController {
	@Autowired
	WalletTransactionDAO walletTransactionDao;
	
	@GetMapping("/rest/wallettransaction")
	public ResponseEntity<List<WalletTransaction>> getAll(Model model){
		try {
			List<WalletTransaction> list = walletTransactionDao.findAll();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
	}
	
	@GetMapping("/rest/wallettransaction/{id}")
	public ResponseEntity<List<WalletTransaction>> getOne(@PathVariable("id") Long id) {
		try {
			List<WalletTransaction> list = walletTransactionDao.findTransactionByWallet(id);
			return ResponseEntity.ok(list);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/rest/totalrented")
	public ResponseEntity<Double> getTotalPriceRented() {
		try {
			Double d = walletTransactionDao.sumTransactionRented();
			return ResponseEntity.ok(d);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/rest/totalcommission")
	public ResponseEntity<Double> getTotalCommissionRented() {
		try {
			Double d = walletTransactionDao.sumTransactionCommission();
			return ResponseEntity.ok(d);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/rest/totalusersrented")
	public ResponseEntity<Integer> getTotalUserRented() {
		try {
			int d = walletTransactionDao.countUserRented();
			return ResponseEntity.ok(d);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/rest/pricerentedbymonth/{month}")
	public ResponseEntity<Double> priceRentedByMonth(@PathVariable("month") Integer month) {
		try {
			Double d = walletTransactionDao.getPriceRentedByMonth(month);
			return ResponseEntity.ok(d);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/rest/pricecommissionbymonth/{month}")
	public ResponseEntity<Double> priceCommissionByMonth(@PathVariable("month") Integer month) {
		try {
			Double d = walletTransactionDao.getPriceCommissionByMonth(month);
			return ResponseEntity.ok(d);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/rest/pricedepositebymonth/{month}")
	public ResponseEntity<Double> priceDepositeByMonth(@PathVariable("month") Integer month) {
		try {
			Double d = walletTransactionDao.getPriceDepositeByMonth(month);
			return ResponseEntity.ok(d);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/rest/pricewithdrawbymonth/{month}")
	public ResponseEntity<Double> priceWithdrawByMonth(@PathVariable("month") Integer month) {
		try {
			Double d = walletTransactionDao.getPriceWithdrawByMonth(month);
			return ResponseEntity.ok(d);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PostMapping("/rest/wallettransaction")
	public ResponseEntity<WalletTransaction> post(@RequestBody WalletTransaction walletTransaction) {
		try {
			WalletTransaction walletTran = walletTransactionDao.save(walletTransaction);
			return ResponseEntity.ok(walletTran);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}
	
	@PutMapping("/rest/wallettransaction/{id}")
	public ResponseEntity<WalletTransaction> put(@PathVariable("id") Long id,@RequestBody WalletTransaction walletTransaction) {
		try {
			WalletTransaction walletTran = walletTransactionDao.save(walletTransaction);
			return ResponseEntity.ok(walletTran);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
	}
	
	@DeleteMapping("/rest/wallettransaction/{id}")
	public void delete(@PathVariable("id") Long id) {
		walletTransactionDao.deleteById(id);
	}
}
