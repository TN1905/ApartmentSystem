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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.poly.config.Config;
import com.poly.config.Environment;
import com.poly.dto.PaymentResDTO;
import com.poly.entity.Apartment;
import com.poly.enums.RequestType;
import com.poly.payment.PaymentResponse;
import com.poly.processor.CreateOrderMoMo;
import com.poly.services.SessionService;
import com.poly.utils.LogUtils;

import jakarta.servlet.http.HttpServletRequest;




@RestController
@Controller
public class PaymentController {
	@Autowired
	HttpServletRequest req;
	
	@Autowired
	SessionService sessionService;
	
	
	@GetMapping("/user/create_payment_vnpay")
	public String createPayment() throws UnsupportedEncodingException{
		sessionService.set("month", null);
		Apartment apartment = sessionService.get("apartmentPayment");
		int month = 1;
		String choosemonth = req.getParameter("choosemonth");
		System.out.println(choosemonth);
		System.out.println(choosemonth);
		if(choosemonth.equals("onemonth")) {
			month = 1;
			sessionService.set("month", month);
		}else if(choosemonth.equals("sixmonth")) {
			month = 6;
			sessionService.set("month", month);
		}else if(choosemonth.equals("twelvemonth")) {
			month = 12;
			sessionService.set("month", month);
		}
//        long amount = Integer.parseInt(req.getParameter("amount"))*100;
//        String bankCode = req.getParameter("bankCode");
		System.out.println(apartment);
		double price = apartment.getPrice();
		
		long amount = Math.round(price * 100 * month);
        String vnp_TxnRef = Config.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1";

        String vnp_TmnCode = Config.vnp_TmnCode;
        
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", Config.vnp_Version);
        vnp_Params.put("vnp_Command", Config.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_OrderType", Config.orderType);


       vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl);
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
        
        PaymentResDTO paymentResDTO = new PaymentResDTO();
        paymentResDTO.setStatus("OK");
        paymentResDTO.setMessage("Successfully");
        paymentResDTO.setURL(paymentUrl);
//        com.google.gson.JsonObject job = new JsonObject();
//        job.addProperty("code", "00");
//        job.addProperty("message", "success");
//        job.addProperty("data", paymentUrl);
//        Gson gson = new Gson();
//        resp.getWriter().write(gson.toJson(job));
        
        return paymentUrl;
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
      PaymentResponse captureWalletMoMoResponse = CreateOrderMoMo.process(environment, orderId, requestId, Long.toString(amount), orderInfo, returnURL, notifyURL, "", RequestType.PAY_WITH_ATM, null);
      System.out.println(captureWalletMoMoResponse.getPayUrl());
      return captureWalletMoMoResponse.getPayUrl();
  }
	
	
}
