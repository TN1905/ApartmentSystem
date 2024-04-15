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

import com.poly.dao.RentApartmentDAO;
import com.poly.dao.RentApartmentDetailDAO;
import com.poly.entity.RentApartment;
import com.poly.entity.RentApartmentDetail;

@CrossOrigin("*")
@RestController
public class RestRentApartmentDetailController {
	@Autowired
	RentApartmentDetailDAO dao;
	
	@GetMapping("/rest/rentapartmentdetails")
	public List<RentApartmentDetail> getAll(Model model){
		return dao.findAll();
	}
	
	@GetMapping("/rest/rentapartmentdetails/{id}")
	public RentApartmentDetail getOne(@PathVariable("id") Integer id) {
		return dao.findById(id).get();
	}
	
	@PostMapping("/rest/rentapartmentdetails")
	public RentApartmentDetail post(@RequestBody RentApartmentDetail rentApartmentDetail) {
		dao.save(rentApartmentDetail);
		return rentApartmentDetail;
	}
	
	@PutMapping("/rest/rentapartmentdetails/{id}")
	public RentApartmentDetail put(@PathVariable("id") Long id,@RequestBody RentApartmentDetail rentApartmentDetail) {
		dao.save(rentApartmentDetail);
		return rentApartmentDetail;
	}
	
	@DeleteMapping("/rest/rentapartmentdetails/{id}")
	public void delete(@PathVariable("id") Integer id) {
		dao.deleteById(id);
	}
}
