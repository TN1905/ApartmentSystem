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

import com.poly.dao.ApartTypeDAO;
import com.poly.dao.ApartmentDAO;
import com.poly.dao.ApartmentImageDAO;
import com.poly.entity.Apartment;
import com.poly.entity.ApartmentImage;
import com.poly.services.ApartmentImageService;


@CrossOrigin("*")
@RestController
public class RestApartmentController {
	@Autowired
	ApartmentDAO dao;
	@Autowired
	 private ApartmentImageDAO apartmentImageDao;

	
	@GetMapping("/rest/apartments")
	public List<Apartment> getAll(Model model){
		return dao.findAll();
	}
	
	@GetMapping("/rest/apartments/{id}")
	public Apartment getOne(@PathVariable("id") String id) {
		return dao.findById(id).get();
	}
	
	@GetMapping("/rest/apartmentAccountId/{accountId}")
	public ResponseEntity<List<Apartment>> getListApartmentById(@PathVariable("accountId") Long id) {
		 try {
			 	List<Apartment> list = dao.getListApartmentById(id);

	            return ResponseEntity.ok(list);
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	        }
	}
	
	@PostMapping("/rest/apartments")
	public Apartment post(@RequestBody Apartment apartment) {
		dao.save(apartment);
		return apartment;
	}
	
	@PostMapping("/{id}/images")
    public ResponseEntity<List<ApartmentImage>> addImages(@PathVariable String id, @RequestBody List<ApartmentImage> images) {
        try {
            Apartment apartment = dao.findById(id).orElseThrow(() -> new RuntimeException("Apartment not found"));
            for (ApartmentImage image : images) {
                image.setApartment(apartment);
            }
            List<ApartmentImage> savedImages = apartmentImageDao.saveAll(images);
            return ResponseEntity.ok(savedImages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
	
	@PostMapping("/rest/apartments/bulk-upload")
    public ResponseEntity<?> bulkUpload(@RequestBody List<Apartment> apartments) {
        try {
            dao.saveAll(apartments);
            return ResponseEntity.ok("Data uploaded and processed successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing data: " + e.getMessage());
        }
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
