package com.poly.RestController;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.poly.dao.ApartmentDAO;
import com.poly.dao.ApartmentImageDAO;
import com.poly.entity.Apartment;
import com.poly.entity.ApartmentImage;
import com.poly.entity.Wallet;
import com.poly.services.CloudinaryService;

@RestController
@CrossOrigin("*")
@RequestMapping("/cloudinary")
public class RestCloudinaryController {
	@Autowired
	CloudinaryService cloudinaryService;
	
	@Autowired
	ApartmentImageDAO dao;
	
	@Autowired
	ApartmentDAO aDao;
	
	@GetMapping("/list")
	public ResponseEntity<List<ApartmentImage>> list(){
		List<ApartmentImage> list = dao.findAll();
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<List<ApartmentImage>> getApartmentImage(@PathVariable("id") String id) {
		try {
			List<ApartmentImage> list = dao.findByApartmentId(id);
			return ResponseEntity.ok(list);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PostMapping("/upload")
	@ResponseBody
	public ResponseEntity<Map<String, String>> upload(@RequestParam MultipartFile multipartFile, @RequestParam String apartmentId) throws IOException {
	    BufferedImage bi = ImageIO.read(multipartFile.getInputStream());
	    if (bi == null) {
	        return new ResponseEntity<>(Map.of("message", "Image not valid!"), HttpStatus.BAD_REQUEST);
	    }

	    Optional<Apartment> apartmentOptional = aDao.findById(apartmentId);
	    if (apartmentOptional.isEmpty()) {
	        return new ResponseEntity<>(Map.of("message", "Apartment not found"), HttpStatus.NOT_FOUND);
	    }

	    Apartment apartment = apartmentOptional.get();
	    Map result = cloudinaryService.upload(multipartFile);
	    ApartmentImage image = new ApartmentImage((String) result.get("public_id"),
	            (String) result.get("url"), apartment);
	    dao.save(image);

	    return new ResponseEntity<>(Map.of("message", "upload image successfully", "url", (String) result.get("url")), HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> delete(@PathVariable("id") String id){
		Optional<ApartmentImage> imageOptional = dao.findById(id);
		if(imageOptional.isEmpty()) {
			return new ResponseEntity<>("image not exists", HttpStatus.NOT_FOUND);
		}
		ApartmentImage image = imageOptional.get();
		String cloudinaryImageId = image.getId();
		try {
			cloudinaryService.delete(cloudinaryImageId);
		} catch (IOException e) {
			return new ResponseEntity<>("Failed to delete image from Cloudinary",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		dao.deleteById(id);
		return new ResponseEntity<>("Delete image successfully",HttpStatus.OK);
				
	}
	
}
