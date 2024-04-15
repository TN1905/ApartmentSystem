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
import com.poly.entity.ApartType;
import com.poly.entity.Apartment;

@CrossOrigin("*")
@RestController
public class RestApartTypeController {
	@Autowired
	ApartTypeDAO dao;
	
	@GetMapping("/rest/aparttypes")
	public List<ApartType> getAll(Model model){
		return dao.findAll();
	}
	
	@GetMapping("/rest/aparttypes/{id}")
	public ApartType getOne(@PathVariable("id") String id) {
		return dao.findById(id).get();
	}
	
	@PostMapping("/rest/aparttypes")
	public ApartType post(@RequestBody ApartType apartType) {
		dao.save(apartType);
		return apartType;
	}
	
	@PutMapping("/rest/aparttypes/{id}")
	public ApartType put(@PathVariable("id") String id,@RequestBody ApartType apartType) {
		dao.save(apartType);
		return apartType;
	}
	
	@DeleteMapping("/rest/aparttypes/{id}")
	public void delete(@PathVariable("id") String id) {
		dao.deleteById(id);
	}
}
