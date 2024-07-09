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

import com.poly.dao.RoleDAO;
import com.poly.dao.WalletDAO;
import com.poly.entity.Role;
import com.poly.entity.Wallet;

@CrossOrigin("*")
@RestController
public class RestRoleController {
	@Autowired
	RoleDAO roleDao;
	
	@GetMapping("/rest/role")
	public ResponseEntity<List<Role>> getAll(Model model){
		try {
			List<Role> list = roleDao.findAll();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
	}
	
	@GetMapping("/rest/role/{id}")
	public ResponseEntity<Role> getOne(@PathVariable("id") int id) {
		try {
			Role role = roleDao.findById(id).get();
			return ResponseEntity.ok(role);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PostMapping("/rest/role")
	public ResponseEntity<Role> post(@RequestBody Role role) {
		try {
			Role rol = roleDao.save(role);
			return ResponseEntity.ok(rol);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}
	
	@PutMapping("/rest/role/{id}")
	public ResponseEntity<Role> put(@PathVariable("id") int id,@RequestBody Role role) {
		try {
			Role rol = roleDao.save(role);
			return ResponseEntity.ok(rol);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
	}
	
	@DeleteMapping("/rest/role/{id}")
	public void delete(@PathVariable("id") int id) {
		roleDao.deleteById(id);
	}
}
