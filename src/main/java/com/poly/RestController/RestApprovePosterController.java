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

import com.poly.dao.ApprovePosterDAO;
import com.poly.entity.Account;
import com.poly.entity.ApprovePoster;
import com.poly.entity.WalletTransaction;

@CrossOrigin("*")
@RestController
public class RestApprovePosterController {
	@Autowired
	ApprovePosterDAO approvePosterDao;
	
	@GetMapping("/rest/getApprove")
	public ResponseEntity<List<ApprovePoster>> getAll(Model model){
		try {
			List<ApprovePoster> list = approvePosterDao.findApprovePoster();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
	}
	
	@GetMapping("/rest/checkapprove/{id}")
	public ResponseEntity<ApprovePoster> checkApprovePoster(@PathVariable("id") long accountid){
		try {
			ApprovePoster app = approvePosterDao.checkApproveByAccount(accountid);
            return ResponseEntity.ok(app);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
	}
	
	@PostMapping("/rest/createRegisterPoster")
	public ResponseEntity<ApprovePoster> post(@RequestBody ApprovePoster approvePoster) {
		try {
			ApprovePoster approve = approvePosterDao.save(approvePoster);
			return ResponseEntity.ok(approve);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}
	
	@PutMapping("/rest/updateApprovePoster/{id}")
	public ResponseEntity<ApprovePoster> put(@PathVariable("id") Long id,@RequestBody ApprovePoster approvePoster) {
		try {
			ApprovePoster approve = approvePosterDao.findById(id).get();
			approve.setStatus(true);
			approve = approvePosterDao.save(approve);
			return ResponseEntity.ok(approve);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@DeleteMapping("/rest/deleteApprovePoster/{id}")
	public void delete(@PathVariable("id") Long id) {
		approvePosterDao.deleteById(id);
	}
}
