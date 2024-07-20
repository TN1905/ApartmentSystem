package com.poly.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poly.dao.RentApartmentDAO;
import com.poly.entity.RentApartment;
import com.poly.entity.Wallet;
import com.poly.services.MailerService;
import com.poly.services.PdfUtil;

@CrossOrigin("*")
@RestController
public class RestSendContractController {
	@Autowired
	MailerService mailerService;
	
	@Autowired
	RentApartmentDAO rentApartmentDao;
	
	@GetMapping("/rest/sendContract")
	public ResponseEntity<Boolean> post(@RequestParam("email") String email, @RequestParam("rentid") Long rentid) {
		try {
			RentApartment rent = rentApartmentDao.findById(rentid).orElseThrow(() -> new RuntimeException("Rent not found"));
			String pdfPath = PdfUtil.createContractPdf(rent);
	        mailerService.sendContractEmail(email, pdfPath);
			return ResponseEntity.ok(true);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
