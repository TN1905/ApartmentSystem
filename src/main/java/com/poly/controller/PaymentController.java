package com.poly.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.poly.config.Config;
import com.poly.config.Environment;
import com.poly.dao.RentApartmentDAO;
import com.poly.dto.PaymentResDTO;
import com.poly.entity.Apartment;
import com.poly.entity.RentApartment;
import com.poly.enums.RequestType;
import com.poly.payment.PaymentResponse;
import com.poly.processor.CreateOrderMoMo;
import com.poly.services.SessionService;
import com.poly.utils.LogUtils;

import jakarta.servlet.http.HttpServletRequest;


@CrossOrigin("*")
@RestController
@Controller
public class PaymentController {
	@Autowired
	HttpServletRequest req;
	
	@Autowired
	SessionService sessionService;
	
	@Autowired
	RentApartmentDAO rentApartmentDao;
	
	@PostMapping("/rest/create_payment_vnpay")
	public ResponseEntity<Map<String, String>> create_payment_method(@RequestParam("walletId") Long walletId
			, @RequestParam("amount") double amount,@RequestParam("month") int month
			, @RequestBody RentApartment rentApartment) throws UnsupportedEncodingException{
		RentApartment rent = rentApartmentDao.save(rentApartment);
		long amountDeposite = Math.round(amount * 100);
        String vnp_TxnRef = Config.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1";

        String vnp_TmnCode = Config.vnp_TmnCode;
        
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", Config.vnp_Version);
        vnp_Params.put("vnp_Command", Config.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amountDeposite));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Nap tien:" + vnp_TxnRef);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_OrderType", Config.orderType);


       vnp_Params.put("vnp_ReturnUrl", Config.deposite_vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
        
        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Successfully");
        response.put("URL", paymentUrl);
//        com.google.gson.JsonObject job = new JsonObject();
//        job.addProperty("code", "00");
//        job.addProperty("message", "success");
//        job.addProperty("data", paymentUrl);
//        Gson gson = new Gson();
//        resp.getWriter().write(gson.toJson(job));
        
        return ResponseEntity.ok(response);
	}
	
	@GetMapping("/user/create_payment_momo")
	 public  String createMoMoOrder() throws Exception{
		LogUtils.init();
		Apartment apartment = sessionService.get("apartmentPayment");
		String requestId = String.valueOf(System.currentTimeMillis());
      String orderId = String.valueOf(System.currentTimeMillis());
      String partnerClientId = "partnerClientId";
      Long transId = 2L;
      int month = 1;
      	String choosemonth = req.getParameter("choosemonth");
		System.out.println(choosemonth);
		if(choosemonth.equals("onemonth")) {
			month = 1;
		}else if(choosemonth.equals("sixmonth")) {
			month = 6;
		}else if(choosemonth.equals("twelvemonth")) {
			month = 12;
		}
		System.out.println(month);
      double price = apartment.getPrice();
      long amount = Math.round(price) * month;
      System.out.println(amount);
      String orderInfo = "Pay With MoMo";
      String returnURL = "http://localhost:8080/user/paymentsuccessMOMO";
      String notifyURL = "https://google.com.vn";
      String callbackToken = "callbackToken";
      String token = "";
      String paymentUrl = ""; // Gán giá trị đường dẫn thanh toán từ MoMo vào đây
      Environment environment = Environment.selectEnv("dev");
      PaymentResponse captureWalletMoMoResponse = CreateOrderMoMo.process(environment, orderId, requestId, Long.toString(1000000), orderInfo, returnURL, notifyURL, "", RequestType.PAY_WITH_ATM, null);
      System.out.println(captureWalletMoMoResponse.getPayUrl());
      return captureWalletMoMoResponse.getPayUrl();
  }
	
	@PostMapping("/rest/create_payment_momo")
	public ResponseEntity<Map<String, String>> create_payment_momo(@RequestParam("walletId") Long walletId
			, @RequestParam("amount") double amount,@RequestParam("month") int month
			, @RequestBody RentApartment rentApartment) throws Exception {
	    LogUtils.init();
	    RentApartment apartRent = rentApartmentDao.save(rentApartment);
	    String requestId = String.valueOf(System.currentTimeMillis());
	    String orderId = String.valueOf(System.currentTimeMillis());
	    String partnerClientId = "partnerClientId";
	    long amountDeposite = Math.round(amount);
	    String orderInfo = "Pay With MoMo";
	    String returnURL = "http://localhost:3000/transaction";
	    String notifyURL = "https://google.com.vn";
	    Environment environment = Environment.selectEnv("dev");
	    PaymentResponse captureWalletMoMoResponse = CreateOrderMoMo.process(environment, orderId, requestId, Long.toString(amountDeposite), orderInfo, returnURL, notifyURL, "", RequestType.PAY_WITH_ATM, null);
	    Map<String, String> response = new HashMap<>();
	    response.put("status", "OK");
	    response.put("message", "Successfully");
	    response.put("URL", captureWalletMoMoResponse.getPayUrl());
	    return ResponseEntity.ok(response);
	}
	
	
	
	@GetMapping("/rest/deposite_momo")
	public ResponseEntity<Map<String, String>> MOMOdepositeToWallet(@RequestParam("walletId") long id, @RequestParam("amount") double amount) throws Exception {
	    LogUtils.init();
	    String requestId = String.valueOf(System.currentTimeMillis());
	    String orderId = String.valueOf(System.currentTimeMillis());
	    String partnerClientId = "partnerClientId";
	    long amountDeposite = Math.round(amount);
	    String orderInfo = "Pay With MoMo";
	    String returnURL = "http://localhost:3000/transaction";
	    String notifyURL = "https://google.com.vn";
	    Environment environment = Environment.selectEnv("dev");
	    PaymentResponse captureWalletMoMoResponse = CreateOrderMoMo.process(environment, orderId, requestId, Long.toString(amountDeposite), orderInfo, returnURL, notifyURL, "", RequestType.PAY_WITH_ATM, null);
	    Map<String, String> response = new HashMap<>();
	    response.put("status", "OK");
	    response.put("message", "Successfully");
	    response.put("URL", captureWalletMoMoResponse.getPayUrl());
	    return ResponseEntity.ok(response);
	}
	
	@GetMapping("/rest/deposite_vnpay")
	public ResponseEntity<Map<String, String>> VNPdepositeToWallet(@RequestParam("walletId") long id, @RequestParam("amount") double amount) throws UnsupportedEncodingException{
		long amountDeposite = Math.round(amount * 100);
        String vnp_TxnRef = Config.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1";

        String vnp_TmnCode = Config.vnp_TmnCode;
        
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", Config.vnp_Version);
        vnp_Params.put("vnp_Command", Config.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amountDeposite));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Nap tien:" + vnp_TxnRef);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_OrderType", Config.orderType);


       vnp_Params.put("vnp_ReturnUrl", Config.deposite_vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
        
        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Successfully");
        response.put("URL", paymentUrl);
//        com.google.gson.JsonObject job = new JsonObject();
//        job.addProperty("code", "00");
//        job.addProperty("message", "success");
//        job.addProperty("data", paymentUrl);
//        Gson gson = new Gson();
//        resp.getWriter().write(gson.toJson(job));
        
        return ResponseEntity.ok(response);
	}
	
	
	
	
}
