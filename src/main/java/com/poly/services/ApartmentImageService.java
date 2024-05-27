package com.poly.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;
import com.poly.dao.ApartmentDAO;
import com.poly.dao.ApartmentImageDAO;
import com.poly.entity.Apartment;
import com.poly.entity.ApartmentImage;

@Service
public class ApartmentImageService {
	@Autowired
    private ApartmentImageDAO apartmentImageRepository;

    @Autowired
    private ApartmentDAO apartmentRepository;
    
    private static final String UPLOAD_DIR = "src/main/resources/static/images/";
    
    public List<ApartmentImage> getAllImages() {
        return apartmentImageRepository.findAll();
    }
    
    public List<ApartmentImage> saveImages(String[] filenames, String apartmentId) throws IOException {
        Apartment apartment = apartmentRepository.findById(apartmentId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid apartment ID"));

        List<ApartmentImage> images = new ArrayList<>();
        for (String filename : filenames) {
            ApartmentImage image = new ApartmentImage();
            image.setImageData(filename); // Lưu đường dẫn của ảnh
            image.setApartment(apartment);
            images.add(image);
        }
        
        return apartmentImageRepository.saveAll(images);
    }

    private String[] readFileData(String[] filenames) throws IOException {
        return filenames; // Trả về danh sách các đường dẫn của ảnh
    }
}
