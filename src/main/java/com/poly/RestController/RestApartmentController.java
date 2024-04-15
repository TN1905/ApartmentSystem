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

import com.poly.dao.ApartTypeDAO;
import com.poly.dao.ApartmentDAO;
import com.poly.entity.Apartment;


@CrossOrigin("*")
@RestController
public class RestApartmentController {
	@Autowired
	ApartmentDAO dao;

	
	@GetMapping("/rest/apartments")
	public List<Apartment> getAll(Model model){
		return dao.findAll();
	}
	
	@GetMapping("/rest/apartments/{id}")
	public Apartment getOne(@PathVariable("id") String id) {
		return dao.findById(id).get();
	}
	
	@PostMapping("/rest/apartments")
	public Apartment post(@RequestBody Apartment apartment) {
		dao.save(apartment);
		return apartment;
	}
	
	@PutMapping("/rest/apartments/{id}")
	public Apartment put(@PathVariable("id") String id,@RequestBody Apartment apartment) {
		dao.save(apartment);
		return apartment;
	}
	
	@DeleteMapping("/rest/apartments/{id}")
	public void delete(@PathVariable("id") String id) {
		dao.deleteById(id);
	}
}
