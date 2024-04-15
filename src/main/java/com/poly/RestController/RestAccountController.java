package com.poly.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.poly.dao.AccountDAO;
import com.poly.dao.ApartTypeDAO;
import com.poly.entity.Account;
import com.poly.entity.ApartType;

@CrossOrigin("*")
@RestController
public class RestAccountController {
	@Autowired
	AccountDAO dao;
	
	@GetMapping("/rest/accounts")
	public List<Account> getAll(Model model){
		return dao.findAll();
	}
	
	@GetMapping("/rest/accounts/{id}")
	public Account getOne(@PathVariable("id") Long id) {
		return dao.findById(id).get();
	}
	
	@PostMapping("/rest/accounts")
	public Account post(@RequestBody Account account) {
		dao.save(account);
		return account;
	}
	
	@PutMapping("/rest/accounts/{id}")
	public Account put(@PathVariable("id") Long id,@RequestBody Account account) {
		dao.save(account);
		return account;
	}
	
	@DeleteMapping("/rest/accounts/{id}")
	public void delete(@PathVariable("id") Long id) {
		dao.deleteById(id);
	}
}
