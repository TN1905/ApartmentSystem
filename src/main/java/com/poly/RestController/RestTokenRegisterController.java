package com.poly.RestController;

import org.eclipse.angus.mail.iap.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poly.dao.AccountDAO;
import com.poly.dao.ConfirmationTokenDAO;
import com.poly.entity.Account;
import com.poly.entity.ConfirmationToken;
import com.poly.entity.Wallet;


@CrossOrigin("*")
@RestController
public class RestTokenRegisterController {
	@Autowired
	ConfirmationTokenDAO confirmationTokenDao;
	
	@Autowired
	AccountDAO dao;
	
	@GetMapping("/rest/confirmtokenregister")
	public ResponseEntity<Account> confirmTokenRegister(@RequestParam("token") String token) {
		try {
			ConfirmationToken tokenRegister = confirmationTokenDao.findByConfirmationToken(token);
	        System.out.println(tokenRegister);
	        if(tokenRegister != null)
	        {
	        	Account account = dao.findByUsername(tokenRegister.getAccount().getUsername());
	        	account.setEnabled(true);
	            dao.save(account);
	            return ResponseEntity.ok(account);
	        }
	        else
	        {
	 
	        	return null;
	        }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
		
	}
	
}
