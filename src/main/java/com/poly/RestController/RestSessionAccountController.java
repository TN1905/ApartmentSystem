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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poly.dao.SessionAccountDAO;
import com.poly.entity.AccountSession;
import com.poly.entity.SessionAccountUpdateRequest;


@CrossOrigin("*")
@RestController
public class RestSessionAccountController {
	@Autowired
	SessionAccountDAO sessionDao;
	

	
	@GetMapping("/rest/sessionAccount/{id}")
	public ResponseEntity<AccountSession> getOne(@PathVariable("id") Long id) {
		try {
			AccountSession accountSession = sessionDao.findSessionByAccountId(id);
			return ResponseEntity.ok(accountSession);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PostMapping
    public ResponseEntity<AccountSession> updateSessionAccount(@RequestBody SessionAccountUpdateRequest request) {
        try {
            AccountSession accountSession = sessionDao.updateSession(request.getAccountId(), request.getSession(), request.getExpires());
            System.out.println(accountSession);
            return ResponseEntity.ok(accountSession);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
	
	@PostMapping("/rest/sessionAccount")
	public ResponseEntity<AccountSession> post(@RequestBody AccountSession accountSession) {
		try {
			AccountSession accSession = sessionDao.save(accountSession);
			return ResponseEntity.ok(accSession);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}
	
	@PutMapping("/rest/sessionAccount/{id}")
	public ResponseEntity<AccountSession> put(@PathVariable("id") Long id,@RequestBody AccountSession accountSession) {
		try {
			AccountSession accSession = sessionDao.findSessionByAccountId(id);
			accSession.setExpires(accountSession.getExpires());
			accSession.setSession_token(accountSession.getSession_token());
			accSession.setCreate_at(accountSession.getCreate_at());
			sessionDao.save(accSession);
			return ResponseEntity.ok(accSession);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
	}
	
	@DeleteMapping("/rest/sessionAccount/{id}")
	public void delete(@PathVariable("id") Integer id) {
		sessionDao.deleteById(id);
	}
}


