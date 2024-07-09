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

import com.poly.dao.AccountDAO;
import com.poly.dao.RentApartmentDAO;
import com.poly.entity.Account;
import com.poly.entity.ApartmentImage;
import com.poly.entity.RentApartment;

@CrossOrigin("*")
@RestController
public class RestRentApartmentController {
	@Autowired
	RentApartmentDAO dao;
	
	@GetMapping("/rest/rentapartments")
	public List<RentApartment> getAll(Model model){
		return dao.findAll();
	}
	
	@GetMapping("/rest/rentapartments/{id}")
	public RentApartment getOne(@PathVariable("id") Long id) {
		return dao.findById(id).get();
	}
	
	@GetMapping("/rest/rentapartment/{id}")
	public ResponseEntity<List<RentApartment>> getListRentApartment(@PathVariable("id") String id) {
		try {
			List<RentApartment> list = dao.findByRentApartment(id);
			return ResponseEntity.ok(list);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PostMapping("/rest/rentapartments")
	public RentApartment post(@RequestBody RentApartment rentApartment) {
		dao.save(rentApartment);
		return rentApartment;
	}
	
	@PutMapping("/rest/rentapartments/{id}")
	public RentApartment put(@PathVariable("id") Long id,@RequestBody RentApartment rentApartment) {
		dao.save(rentApartment);
		return rentApartment;
	}
	
	@DeleteMapping("/rest/rentapartments/{id}")
	public void delete(@PathVariable("id") Long id) {
		dao.deleteById(id);
	}
}
