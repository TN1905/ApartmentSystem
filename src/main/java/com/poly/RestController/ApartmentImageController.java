package com.poly.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.poly.dao.ApartmentImageDAO;
import com.poly.entity.ApartmentImage;
import com.poly.services.ApartmentImageService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/files/images")
public class ApartmentImageController {
	 @Autowired
	 private ApartmentImageService apartmentImageService;
	 @Autowired
	 private ApartmentImageDAO apartmentImageDao;
	 
	 private static final String UPLOAD_DIR = "src/main/resources/static/images/";

	 @PostMapping
	 public ResponseEntity<List<String>> uploadImages(@RequestParam("files") MultipartFile[] files) {
	     List<String> fileNames = new ArrayList<>();
	     try {
	         for (MultipartFile file : files) {
	             // Kiểm tra xem file có dữ liệu không
	             if (!file.isEmpty()) {
	                 String fileName = StringUtils.cleanPath(file.getOriginalFilename());
	                 // Lưu tên tệp vào danh sách fileNames
	                 fileNames.add(fileName);
	                 // Tiến hành xử lý lưu file vào hệ thống tại đây nếu cần
	                 // Ví dụ: lưu vào thư mục UPLOAD_DIR
	                 Path path = Paths.get(UPLOAD_DIR + fileName);
	                 Files.createDirectories(path.getParent());
	                 Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	             }
	         }
	     } catch (IOException e) {
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	     }
	     return ResponseEntity.ok(fileNames);
	 }
	 
	 @PostMapping("/save")
	    public ResponseEntity<Void> saveImageInfo(@RequestBody List<ApartmentImage> images) {
	        try {
	            for (ApartmentImage image : images) {
	                apartmentImageDao.save(image);
	            }
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	        }
	        return ResponseEntity.status(HttpStatus.CREATED).build();
	    }
	 
	 @PutMapping("/update")
	    public ResponseEntity<Void> updateImageInfo(@RequestBody List<ApartmentImage> images) {
	        try {
	            for (ApartmentImage image : images) {
	                apartmentImageDao.save(image);
	            }
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	        }
	        return ResponseEntity.status(HttpStatus.CREATED).build();
	    }
	 
	 


	    @GetMapping
	    public ResponseEntity<List<String>> listFiles() {
	        try {
	            List<String> fileNames = new ArrayList<>();
	            Files.walk(Paths.get(UPLOAD_DIR))
	                    .filter(Files::isRegularFile)
	                    .forEach(path -> fileNames.add(path.getFileName().toString()));
	            return ResponseEntity.ok(fileNames);
	        } catch (IOException e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	        }
	    }
	    
	    @GetMapping("/{apartmentId}")
	    public ResponseEntity<List<ApartmentImage>> listFilesByApartment(@PathVariable String apartmentId) {
	        try {
	            List<ApartmentImage> images = apartmentImageDao.findByApartmentId(apartmentId);
	            List<String> fileNames = new ArrayList<>();
	            for (ApartmentImage image : images) {
	                fileNames.add(image.getImageData());
	            }
	            return ResponseEntity.ok(images);
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	        }
	    }

	    @DeleteMapping("/{filename}")
	    public ResponseEntity<Void> deleteFile(@PathVariable String filename) {
	        try {
	            Path path = Paths.get(UPLOAD_DIR + filename);
	            Files.deleteIfExists(path);
	            return ResponseEntity.noContent().build();
	        } catch (IOException e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	        }
	    }
}
