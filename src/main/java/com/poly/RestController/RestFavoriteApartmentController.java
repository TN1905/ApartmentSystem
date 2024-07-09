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

import com.poly.dao.FavoriteApartmentDAO;
import com.poly.entity.FavoriteApartment;
import com.poly.entity.SessionAccountUpdateRequest;

@CrossOrigin("*")
@RestController
public class RestFavoriteApartmentController {
	@Autowired
	FavoriteApartmentDAO favoriteApartmentDao;
	
	@PostMapping("/rest/favorites")
	public ResponseEntity<FavoriteApartment> createFavorite(@RequestBody FavoriteApartment favoriteApartment){
		try {
			 FavoriteApartment favApart = favoriteApartmentDao.save(favoriteApartment);
            return ResponseEntity.ok(favApart);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
	}
	
	@GetMapping("/rest/favorites/{account_id}")
	public ResponseEntity<List<FavoriteApartment>> getFavorites(@PathVariable("account_id") Long account_id){
		try {
			List<FavoriteApartment> list  = favoriteApartmentDao.getFavoriteApart(account_id);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
	}
	
	@GetMapping("/rest/favorites")
	public ResponseEntity<FavoriteApartment> getOne(@RequestParam("account_id") Long account_id, @RequestParam("apartment_id") String apartment_id) {
	    try {
	        FavoriteApartment favoriteApartment = favoriteApartmentDao.findFavoriteApartment(account_id, apartment_id);
	        if (favoriteApartment != null) {
	            return ResponseEntity.ok(favoriteApartment);
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}
	
	@PutMapping("/rest/favorites/{id}")
	public ResponseEntity<FavoriteApartment> put(@PathVariable("id") Long id,@RequestBody FavoriteApartment favorite) {
		try {
			FavoriteApartment favoriteApartment = favoriteApartmentDao.save(favorite);
			return ResponseEntity.ok(favoriteApartment);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
	}
	
	@DeleteMapping("/rest/favorites/{id}")
	public void delete(@PathVariable("id") Long id) {
		favoriteApartmentDao.deleteById(id);
	}
}
