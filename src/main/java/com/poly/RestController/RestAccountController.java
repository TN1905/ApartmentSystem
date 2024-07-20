package com.poly.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
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

import com.poly.dao.AccountDAO;
import com.poly.dao.ApartTypeDAO;
import com.poly.dao.ConfirmationTokenDAO;
import com.poly.entity.Account;
import com.poly.entity.ApartType;
import com.poly.entity.ConfirmationToken;
import com.poly.entity.Role;
import com.poly.entity.Wallet;
import com.poly.services.MailerServiceImpl;
import com.poly.utils._enum.RoleUserEnum;

import jakarta.persistence.Transient;

@CrossOrigin("*")
@RestController
public class RestAccountController {
	@Autowired
	AccountDAO dao;
	
	@Transient
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
	
	@Autowired
	MailerServiceImpl mailer;
	
	@Autowired
	ConfirmationTokenDAO confirmationTokenDao;
	
	@GetMapping("/rest/accounts")
	public List<Account> getAll(Model model){
		return dao.findAll();
	}
	
	@GetMapping("/rest/accounts/{username}")
	public Account getOne(@PathVariable("username") String id) {
		return dao.findByUsername(id);
	}
	
	@GetMapping("/rest/account")
	public ResponseEntity<Account> login(@RequestParam("username") String email, @RequestParam("password") String password) {
		try {
			Account acc = dao.findByUsername(email);
			System.out.println(dao);
			if(acc!=null) {
				if(acc.checkPassword(password)) {
					return ResponseEntity.ok(acc);
				}else {
					return null;
				}
				
			}else {
				return null;
			}
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
		
	}
	
	@PostMapping("/rest/accounts")
	public ResponseEntity<Account> post(@RequestBody Account account) {
		
		try {
			long currentTimeMillis = System.currentTimeMillis();
			account.setId(currentTimeMillis);
			account.setPassword(PASSWORD_ENCODER.encode(account.getPassword()));
			Set<Role> roles = new HashSet<>();
            roles.add(new Role(RoleUserEnum.USER));
            account.setRoles(roles);
            account.setEnabled(false);
			Account acc = dao.save(account);
			ConfirmationToken confirmationToken = new ConfirmationToken(account);
			confirmationTokenDao.save(confirmationToken);
			SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(account.getUsername());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setText("To confirm your account, please click here : "
            +"http://localhost:3000/confirmregister?token="+confirmationToken.getConfirmationToken());
            mailer.sendEmail(mailMessage);
			return ResponseEntity.ok(acc);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/rest/checkPass")
	public ResponseEntity<Boolean> checkPass(@RequestParam("username") String email, @RequestParam("password") String password) {
		try {
			Account acc = dao.findByUsername(email);
			System.out.println(dao);
			if(acc!=null) {
				if(acc.checkPassword(password)) {
					return ResponseEntity.ok(true);
				}else {
					return ResponseEntity.ok(false);
				}
				
			}else {
				return ResponseEntity.ok(false);
			}
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
		
	}
	
	@PutMapping("/rest/changePass/{id}")
	public ResponseEntity<Account> changePass(@PathVariable("id") Long id,@RequestBody Account account) {
		try {
			Account acc = dao.findById(id).get();
			if(acc!= null) {
				acc.setPassword(PASSWORD_ENCODER.encode(account.getPassword()));
				acc = dao.save(acc);
			}
			return ResponseEntity.ok(acc);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PutMapping("/rest/addRole/{id}")
	public ResponseEntity<Account> addRole(@PathVariable("id") Long id,@RequestBody Account account) {
		try {
			Account acc = dao.findById(id).get();
			if(acc!= null) {
				Set<Role> roles = new HashSet<>();
	            roles.add(new Role(RoleUserEnum.USER));
	            roles.add(new Role(RoleUserEnum.POSTER));
	            acc.setRoles(roles);
				acc = dao.save(acc);
			}
			return ResponseEntity.ok(acc);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PutMapping("/rest/accounts/{id}")
	public ResponseEntity<Account> put(@PathVariable("id") Long id,@RequestBody Account account) {
		try {
			Account acc = dao.findById(id).get();
			if(acc!= null) {
				acc.setFirstname(account.getFirstname());
				acc.setLastname(account.getLastname());
				acc.setPhone(account.getPhone());
				acc.setGender(account.getGender());
				acc.setEnabled(account.isEnabled());
				acc = dao.save(acc);
			}
			return ResponseEntity.ok(acc);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PutMapping("/rest/updateAccount/{id}")
	public Account updateAccount(@PathVariable("id") Long id,@RequestBody Account account) {
		dao.save(account);
		return account;
	}
	
	@DeleteMapping("/rest/accounts/{id}")
	public void delete(@PathVariable("id") Long id) {
		dao.deleteById(id);
	}
}
