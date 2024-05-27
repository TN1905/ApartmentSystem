package com.poly.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.poly.dao.ApartTypeDAO;
import com.poly.dao.ApartmentDAO;
import com.poly.entity.ApartType;
import com.poly.entity.Apartment;

import com.poly.services.ParamService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;



@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class ApartmentController {
	@Autowired
	ApartTypeDAO apartTypeDao;
	@Autowired
	ApartmentDAO apartmentDAO;
	@Autowired
	ParamService paramService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	HttpServletRequest req;
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping("/admin/apartment")
	public String apartmentIndex(Model model) {
		Apartment apart = new Apartment();
		model.addAttribute("apart",apart);
		List<Apartment> list = apartmentDAO.findAll();
		model.addAttribute("apartmentList",list);
		return "admin/apartment";
	}
	
//	@RequestMapping("/admin/createApartment")
//	public String createApartment(Model model,@Valid @ModelAttribute("apart") Apartment apartment,
//			@RequestParam("apartImageTitle") MultipartFile imageTitle,@RequestParam("apartImage1") MultipartFile image1,
//			@RequestParam("apartImage2") MultipartFile image2,@RequestParam("apartImage3") MultipartFile image3,
//			@RequestParam("city") String city, @RequestParam("district") String district,
//	        @RequestParam("ward") String ward,@RequestParam("cityValue") String cityValue,
//	        @RequestParam("districtValue") String districtValue,@RequestParam("wardValue") String wardValue) throws ServletException, IOException{
//		System.out.println(city);
//		System.out.println(district);
//		System.out.println(ward);
//		String staticImagesPath = Paths.get("src/main/resources/static/images").toAbsolutePath().toString();
//		File imagePath = null;
//		File imagePath1 = null;
//		File imagePath2 = null;
//		File imagePath3 = null;
//		LocalDate currentDate = LocalDate.now();
//		System.out.println(cityValue);
//		System.out.println(districtValue);
//		System.out.println(wardValue);
//		// Định dạng ngày tháng theo yyyy/mm/dd
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
//		String formattedDate = currentDate.format(formatter);
//		// Kiểm tra xem người dùng đã chọn ảnh hay chưa
//		String apartmentTypeId = req.getParameter("apartmentType");
//		System.out.println(apartmentTypeId);
//		ApartType apartmentType = apartTypeDao.findById(apartmentTypeId).orElse(null);
//	    if (image1 != null && !image1.isEmpty() && image2 != null && !image2.isEmpty()
//	    		&& image3 != null && !image3.isEmpty() && imageTitle != null && !imageTitle.isEmpty()) {
//	        imagePath = paramService.save(imageTitle, staticImagesPath);
//	        imagePath1 = paramService.save(image1, staticImagesPath);
//	        imagePath2 = paramService.save(image2, staticImagesPath);
//	        imagePath3 = paramService.save(image3, staticImagesPath);
//	    } else {
//	        // Nếu không có ảnh được chọn, gán ảnh mặc định
//	        // Đường dẫn đến ảnh mặc định (điều này cần được điều chỉnh dựa trên cấu trúc dự án của bạn)
//	        String defaultImagePath = Paths.get("src/main/resources/static/images/default.jpg").toAbsolutePath().toString();
//	        imagePath = new File(defaultImagePath);
//	        imagePath1 = new File(defaultImagePath);
//	        imagePath2 = new File(defaultImagePath);
//	        imagePath3 = new File(defaultImagePath);
//	    }
//	    int apartmentQuantity = (int) apartmentDAO.count();
//	    String imageTitleName = imageTitle.getOriginalFilename();
//	    String image1Name = image1.getOriginalFilename();
//	    String image2Name = image2.getOriginalFilename();
//	    String image3Name = image3.getOriginalFilename();
//	    long currentTimeMillis = System.currentTimeMillis();
//	    apartment.setApartmentType(apartmentType);
//	    apartment.setId("APART"+currentTimeMillis);
//	    apartment.setImagetitle(imageTitleName);
//	    apartment.setImage1(image1Name);
//	    apartment.setImage2(image2Name);
//	    apartment.setImage3(image3Name);
//	    apartmentDAO.save(apartment);
//	    apartment = new Apartment();
//	    model.addAttribute("messageApartment","Tạo mới nhà trọ thành công");
//	    List<Apartment> apartmentList = apartmentDAO.findAll();
//	    model.addAttribute("apartmentList", apartmentList);
//		return "admin/apartment";
//	}
	
	@ModelAttribute("apartType")
	public List<ApartType> getFaculties(){
		String API_URL = "http://localhost:8080/rest/aparttypes";
        ResponseEntity<List<ApartType>> response = restTemplate.exchange(API_URL, HttpMethod.GET, null, 
            new ParameterizedTypeReference<List<ApartType>>() {});
        if (response.getStatusCode().is2xxSuccessful()) {
            // Lấy danh sách căn hộ từ phản hồi
            List<ApartType> apartType = response.getBody();
      
            System.out.println(apartType);
            return apartType;
        } else {
            System.out.println("Không thể lấy dữ liệu từ API.");
            return null;
        }
	}
	
//	@RequestMapping("/admin/updateApartment")
//	public String updateApartment(Model model,@Valid @ModelAttribute("apart") Apartment apartment,
//			@RequestParam("apartImageTitle") MultipartFile imageTitle,@RequestParam("apartImage1") MultipartFile image1,
//			@RequestParam("apartImage2") MultipartFile image2,@RequestParam("apartImage3") MultipartFile image3,
//			@RequestParam("city") String city, @RequestParam("district") String district,
//	        @RequestParam("ward") String ward,@RequestParam("cityValue") String cityValue,
//	        @RequestParam("districtValue") String districtValue,@RequestParam("wardValue") String wardValue) throws ServletException, IOException{
//	    Apartment existingApartment = apartmentDAO.findById(apartment.getId()).orElse(null);
//	    System.out.println(apartment.getId());
//	    System.out.println(existingApartment);
//		String staticImagesPath = Paths.get("src/main/resources/static/images").toAbsolutePath().toString();
//		System.out.println(image1);
//		System.out.println(image2);
//		System.out.println(image3);
//		System.out.println(imageTitle);
//		File imagePath = null;
//		File imagePath1 = null;
//		File imagePath2 = null;
//		File imagePath3 = null;
//		LocalDate currentDate = LocalDate.now();
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
//		String formattedDate = currentDate.format(formatter);
//		// Kiểm tra xem người dùng đã chọn ảnh hay chưa
//		String apartmentTypeId = req.getParameter("apartmentType");
//		System.out.println(apartmentTypeId);
//		ApartType apartmentType = apartTypeDao.findById(apartmentTypeId).orElse(null);
//		System.out.println(apartmentType);
//		if (image1 != null && !image1.isEmpty() && image2 != null && !image2.isEmpty()
//	    		&& image3 != null && !image3.isEmpty() && imageTitle != null && !imageTitle.isEmpty()) {
//	        imagePath = paramService.save(imageTitle, staticImagesPath);
//	        imagePath1 = paramService.save(image1, staticImagesPath);
//	        imagePath2 = paramService.save(image2, staticImagesPath);
//	        imagePath3 = paramService.save(image3, staticImagesPath);
//	    } else {
//	        // Nếu không có ảnh được chọn, gán ảnh mặc định
//	        // Đường dẫn đến ảnh mặc định (điều này cần được điều chỉnh dựa trên cấu trúc dự án của bạn)
//	        String defaultImagePath = Paths.get("src/main/resources/static/images/default.jpg").toAbsolutePath().toString();
//	        imagePath = new File(defaultImagePath);
//	        imagePath1 = new File(defaultImagePath);
//	        imagePath2 = new File(defaultImagePath);
//	        imagePath3 = new File(defaultImagePath);
//	    }
//		
//		String imageTitleName = imageTitle.getOriginalFilename();
//	    String image1Name = image1.getOriginalFilename();
//	    String image2Name = image2.getOriginalFilename();
//	    String image3Name = image3.getOriginalFilename();
//	    System.out.println(imageTitleName);
//		System.out.println(image1Name);
//		System.out.println(image2Name);
//		System.out.println(image3Name );
//	    existingApartment.setApartmentType(apartmentType);
//	    if(imageTitle.isEmpty() || imageTitle.equals("")) {
//	    	String it = req.getParameter("it");
//	    	existingApartment.setImagetitle(it);
//	    }else {
//	    	existingApartment.setImagetitle(imageTitleName);
//	    }
//	    if(image1.isEmpty() || image1.equals("")) {
//	    	String i1 = req.getParameter("i1");
//	    	existingApartment.setImage1(i1);
//	    }else {
//	    	existingApartment.setImage1(image1Name);
//	    }
//	    if(image2.isEmpty() || image2.equals("")) {
//	    	String i2 = req.getParameter("i2");
//	    	existingApartment.setImage2(i2);
//	    }else {
//	    	existingApartment.setImage2(image2Name);
//	    }
//	    if(image3.isEmpty() || image3.equals("")) {
//	    	String i3 = req.getParameter("i3");
//	    	existingApartment.setImage3(i3);
//	    }else {
//	    	existingApartment.setImage3(image3Name);
//	    }
//	    
//	    existingApartment.setAcreage(apartment.getAcreage());
//	    existingApartment.setAddress(apartment.getAddress());
//	    existingApartment.setCity(apartment.getCity());
//	    existingApartment.setCityValue(apartment.getCityValue());
//	    existingApartment.setDescription(apartment.getDescription());
//	    existingApartment.setDistrict(apartment.getDistrict());
//	    existingApartment.setDistrictValue(apartment.getDistrictValue());
//	    existingApartment.setWard(apartment.getWard());
//	    existingApartment.setWardValue(apartment.getWardValue());
//	    existingApartment.setContent(apartment.getContent());
//	    existingApartment.setPrice(apartment.getPrice());
//	    System.out.println(existingApartment);
//	    apartmentDAO.save(existingApartment);
//	    List<Apartment> apartmentList = apartmentDAO.findAll();
//	    model.addAttribute("apartmentList", apartmentList);
//	    model.addAttribute("messageApartment","Cập nhật nhà trọ thành công");
//	    return "admin/apartment";
//	}
//	
//

	@RequestMapping("/admin/saveUpdatedApartment")
	public String saveUpdatedApartment(@ModelAttribute("existingApartment") Apartment updatedApartment,
	        @RequestParam("apartImageTitle") MultipartFile imageTitle,
	        @RequestParam("apartImage1") MultipartFile image1,
	        @RequestParam("apartImage2") MultipartFile image2,
	        @RequestParam("apartImage3") MultipartFile image3,
	        @RequestParam("city") String city,
	        @RequestParam("district") String district,
	        @RequestParam("ward") String ward, Model model) throws ServletException, IOException {
			List<Apartment> apartmentList = apartmentDAO.findAll();
		    model.addAttribute("apartmentList", apartmentList);
		    return "admin/apartment";
	}

	@RequestMapping("/admin/deleteApartment")
	public String deleteApartment(Model model, @RequestParam("id") String apartmentId) {
	    apartmentDAO.deleteById(apartmentId);
	    model.addAttribute("messageApartment","Xóa nhà trọ thành công");
	    Apartment apartment = new Apartment();
	    model.addAttribute("apart",apartment);
	    List<Apartment> list = apartmentDAO.findAll();
		model.addAttribute("apartmentList",list);
	    return "admin/apartment";
	}

//	@RequestMapping("/editApartment/{id}")
//	public String editApartment(Model model, @PathVariable("id") String apartmentId) {
//		System.out.println(apartmentId);
//	    Optional<Apartment> existingApartment = apartmentDAO.findById(apartmentId);
//	    System.out.println(existingApartment);
//	    List<ApartType> apartmentTypes = apartTypeDao.findAll();
//	    
//	    existingApartment.ifPresent(apartment -> {
//	        String defaultImagePath = Paths.get(apartment.getImagetitle()).toString();
//	        String defaultImagePath1 = Paths.get(apartment.getImage1()).toString();
//	        String defaultImagePath2 = Paths.get(apartment.getImage2()).toString();
//	        String defaultImagePath3 = Paths.get(apartment.getImage3()).toString();
//	        
//	     
//	        model.addAttribute("apartImageTitle", defaultImagePath);
//	        model.addAttribute("apartImage1", defaultImagePath1);
//	        model.addAttribute("apartImage2", defaultImagePath2);
//	        model.addAttribute("apartImage3", defaultImagePath3);
//
//	        System.out.println(defaultImagePath);
//	        System.out.println(defaultImagePath1);
//	        System.out.println(defaultImagePath2);
//	        System.out.println(defaultImagePath3);
//	        model.addAttribute("city", apartment.getCity());
//	        model.addAttribute("district", apartment.getDistrict());
//	        model.addAttribute("ward", apartment.getWard());
//	        model.addAttribute("cityValue", apartment.getCityValue());
//	        model.addAttribute("districtValue", apartment.getDistrictValue());
//	        model.addAttribute("wardValue", apartment.getWardValue());
//	        model.addAttribute("id", apartment.getId());
//	        System.out.println(apartment.getCityValue());
//	        System.out.println( apartment.getDistrictValue());
//	        System.out.println(apartment.getWardValue());
//	    });
//	    
//	    model.addAttribute("apart", existingApartment);
//	    model.addAttribute("apartmentTypes", apartmentTypes);
//	    
//
//		List<Apartment> list = apartmentDAO.findAll();
//		model.addAttribute("apartmentList",list);
//	    return "admin/apartment";
//	}

	@RequestMapping("/admin/resetForm")
	public String resetForm(Model model) {
	    Apartment apart = new Apartment();
	    model.addAttribute("apart", apart);
	    return "admin/apartment";
	}
	
	@ModelAttribute("status")
	public Map<Boolean,String> getGenders(){
		Map<Boolean, String> map = new HashMap<>();
		map.put(true, "Có");
		map.put(false, "Không");
		return map;
	}
	
	@RequestMapping("/deleteApartment/{id}")
	public String deleteApartment1(Model model, @PathVariable("id") String apartmentId) {
		Optional<Apartment> existingApartment = apartmentDAO.findById(apartmentId);
		
		 existingApartment.ifPresent(apart -> {
			 apartmentDAO.delete(apart);
		    });
		
		
		model.addAttribute("messageApartment","Xóa nhà trọ thành công");
		List<Apartment> list = apartmentDAO.findAll();
		model.addAttribute("apartmentList",list);
	    return "admin/apartment";
	}
}
