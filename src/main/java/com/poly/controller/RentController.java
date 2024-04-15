package com.poly.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.poly.dao.AccountDAO;
import com.poly.dao.ApartmentDAO;
import com.poly.dao.RentApartmentDAO;
import com.poly.dao.RentApartmentDetailDAO;
import com.poly.entity.Account;
import com.poly.entity.Apartment;
import com.poly.entity.RentApartment;
import com.poly.entity.RentApartmentDetail;
import com.poly.services.SessionService;

import jakarta.servlet.http.HttpServletRequest;



@Controller
public class RentController {
	@Autowired
	AccountDAO accountDao;

	@Autowired
	ApartmentDAO apartmentDao;

	@Autowired
	HttpServletRequest req;
	
	@Autowired 
	RentApartmentDAO rentApartmentDao;
	
	@Autowired
	RentApartmentDetailDAO rentApartmentDetailDao;
	
	@Autowired
	private RestTemplate restTemplate;

	
	@Autowired
	SessionService sessionService;
	@RequestMapping("/rentApartment/{id}")
	public String form(Model model,@PathVariable("id") String apartmentId) {
		
//		Account acc = sessionService.get("currentUser");
		String API_URL1 = "http://localhost:8080/rest/accounts/1705562446065";
		String API_URL = "http://localhost:8080/rest/apartments/" + apartmentId;
        ResponseEntity<Apartment> response = restTemplate.exchange(API_URL, HttpMethod.GET, null, 
            new ParameterizedTypeReference<Apartment>() {});
        ResponseEntity<Account> response1 = restTemplate.exchange(API_URL1, HttpMethod.GET, null, 
                new ParameterizedTypeReference<Account>() {});
        
        // Kiểm tra xem phản hồi có thành công không
        if (response.getStatusCode().is2xxSuccessful() && response1.getStatusCode().is2xxSuccessful()) {
            // Lấy danh sách căn hộ từ phản hồi
            Apartment apartment = response.getBody();
            Account acc = response1.getBody();
            model.addAttribute("apartment", apartment);
            model.addAttribute("account", acc);
            sessionService.set("accountPayment", acc);
			sessionService.set("apartmentPayment", apartment);
            System.out.println(apartment);
        } else {
            System.out.println("Không thể lấy dữ liệu từ API.");
        }

		return "user/rentapartment";
	}

	@RequestMapping("/user/paymentsuccessVNPAY")
	public String paymentsuccessVNPAY(Model model) {
		String vnpAmount = req.getParameter("vnp_Amount");
        String vnpBankCode = req.getParameter("vnp_BankCode");
        String vnpBankTranNo = req.getParameter("vnp_BankTranNo");
        String vnpCardType = req.getParameter("vnp_CardType");
        String vnpOrderInfo = req.getParameter("vnp_OrderInfo");
        String vnpPayDate = req.getParameter("vnp_PayDate");
        String vnpResponseCode = req.getParameter("vnp_ResponseCode");
        String vnpTmnCode = req.getParameter("vnp_TmnCode");
        String vnpTransactionNo = req.getParameter("vnp_TransactionNo");
        String vnpTransactionStatus = req.getParameter("vnp_TransactionStatus");
        String vnpTxnRef = req.getParameter("vnp_TxnRef");
        String vnpSecureHash = req.getParameter("vnp_SecureHash");
        RentApartment rentApartment = new RentApartment();
        RentApartmentDetail rentApartmentDetail = new RentApartmentDetail();
        LocalDateTime currentDate = LocalDateTime.now();
        long currentTimeMillis = System.currentTimeMillis();
		// Định dạng ngày tháng theo yyyy/mm/dd
        int month = sessionService.get("month");
        
        Account acc = sessionService.get("accountPayment");
        Apartment apartment = sessionService.get("apartmentPayment");
        LocalDateTime monthrent = currentDate.plusMonths(month);
        rentApartment.setEnddate(monthrent);
        rentApartment.setMonthrent(month);
        rentApartment.setAccount(acc);
        rentApartment.setApartment(apartment);
        rentApartment.setId(currentTimeMillis);
        
        rentApartmentDetail.setApartment(apartment);
        rentApartmentDetail.setPrice(Double.parseDouble(vnpAmount)/1000);
        rentApartmentDetail.setRentApartment(rentApartment);
        rentApartmentDetail.setVnpBankCode(vnpBankCode);
        rentApartmentDetail.setVnpBankTranNo(vnpBankTranNo);
        rentApartmentDetail.setVnpCardType(vnpCardType);
        rentApartmentDetail.setVnpOrderInfo(vnpOrderInfo);
        
        rentApartmentDetail.setVnpTransactionStatus(vnpTransactionStatus);
        rentApartmentDao.save(rentApartment);
        rentApartmentDetailDao.save(rentApartmentDetail);
        apartment.setStatus(false);
        apartmentDao.save(apartment);
        model.addAttribute("paymentDetails",rentApartmentDetail);
        return "user/paymentsuccessForm";
	}
	
	@RequestMapping("/user/paymentsuccessMOMO")
	public String paymentsuccessMomo(Model model) {
		String partnerCode = req.getParameter("partnerCode");
        String orderId = req.getParameter("orderId");
        String requestId = req.getParameter("requestId");
        String amount = req.getParameter("amount");
        String orderInfo = req.getParameter("orderInfo");
        String orderType = req.getParameter("orderType");
        String payType = req.getParameter("payType");
        String responseTime = req.getParameter("responseTime");
        String signature = req.getParameter("signature");
        String resultCode = req.getParameter("resultCode");
        RentApartment rentApartment = new RentApartment();
        RentApartmentDetail rentApartmentDetail = new RentApartmentDetail();
        LocalDate currentDate = LocalDate.now();
        long currentTimeMillis = System.currentTimeMillis();
		// Định dạng ngày tháng theo yyyy/mm/dd
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		String formattedDate = currentDate.format(formatter);
        Account acc = sessionService.get("accountPayment");
        Apartment apartment = sessionService.get("apartmentPayment");
        
        rentApartment.setAccount(acc);
        rentApartment.setApartment(apartment);
        rentApartment.setId(currentTimeMillis);
        rentApartmentDetail.setApartment(apartment);
        rentApartmentDetail.setPrice(Double.parseDouble(amount));
        rentApartmentDetail.setRentApartment(rentApartment);
        rentApartmentDetail.setVnpBankCode(orderType);
        rentApartmentDetail.setVnpBankTranNo(orderId);
        rentApartmentDetail.setVnpCardType(payType);
        rentApartmentDetail.setVnpOrderInfo(orderInfo);
        
        rentApartmentDetail.setVnpTransactionStatus(resultCode);
        rentApartmentDao.save(rentApartment);
        rentApartmentDetailDao.save(rentApartmentDetail);
        apartment.setStatus(false);
        apartmentDao.save(apartment);
        model.addAttribute("paymentDetails",rentApartmentDetail);
        return "user/paymentsuccessForm";
	}
}
