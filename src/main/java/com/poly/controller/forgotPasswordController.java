package com.poly.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poly.dao.AccountDAO;
import com.poly.entity.Account;
import com.poly.services.MailerService;
import com.poly.services.SessionService;
import io.micrometer.common.util.StringUtils;
import jakarta.mail.MessagingException;
import jakarta.persistence.Transient;
import jakarta.servlet.ServletException;
import jakarta.validation.Valid;

@Controller
public class forgotPasswordController {
	@Autowired
	MailerService mailer;
	@Autowired
	AccountDAO dao;
	@Autowired
	SessionService sessionService;
	@Transient
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
	@RequestMapping("/user/forgotpassword")
	public String forgotPassword(Model model) {
		model.addAttribute("message", null);
		
		return "user/forgotpassword";
	}

	@RequestMapping("/user/sendforgotpassword")
	public String sendForgotPassword(Model model, @RequestParam("email") String email, Account acc) throws MessagingException {
		model.addAttribute("message", null);

		if (StringUtils.isEmpty(email)) {
			model.addAttribute("message", "Hãy điền địa chỉ email của bạn.");
			return "user/forgotpassword";
		}

		Account account = dao.findByEmailIgnoreCase(email);
		if (account == null) {
			model.addAttribute("message", "Không tìm thấy địa chỉ email.");
			return "user/forgotpassword";
		}
		String code = mailer.send(acc.getEmail(), "Confirm Register", "Your 6 digits are ");
		sessionService.set("forgotPasswordCode", code);
		sessionService.set("forgotPasswordEmail", email);
		System.out.println(sessionService.get("forgotPasswordCode").toString());
		mailer.send(email, "Forgot Password", "Mã xác nhận của bạn là: " + code);

		model.addAttribute("message",
				"Một mã xác nhận đã được gửi đến email của bạn. Vui lòng kiểm tra hộp thư đến và nhập mã để đặt lại mật khẩu của bạn.");
		return "user/confirmforgotpassword";
	}

	@RequestMapping("/user/confirmforgotpassword")
	public String confirmForgotPassword(Model model, @RequestParam("code") String code) {
	    model.addAttribute("message", null);

	    String expectedCode = (String) sessionService.get("forgotPasswordCode");
	    String email = (String) sessionService.get("forgotPasswordEmail");

	    if (!code.equals(expectedCode) || (email == null || email.isEmpty())) {
	        model.addAttribute("message", "Mã xác nhận sai.");
	        return "user/confirmforgotpassword";
	    }
	    model.addAttribute("account",new Account());
	    return "user/resetpassword";
	}


	
	@RequestMapping("/user/resetpassword")
	public String resetPassword(@RequestParam("confirmPassword") String confirmPassword,Model model,
			@Valid @ModelAttribute("account") Account acc,BindingResult result
	                            ) throws MessagingException,ServletException, IOException{
		System.out.println(result.hasErrors());
		System.out.println(result.getErrorCount());
		System.out.println(result.getFieldErrorCount(confirmPassword));
		if(result.hasFieldErrors("password")) {
			model.addAttribute("message", "Lỗi mật khẩu");
			return "user/resetpassword";
		}else {
			if (!acc.getPassword().equals(confirmPassword)) {
		        model.addAttribute("message", "Mật khẩu mới và mật khẩu xác nhận không khớp.");
		        return "user/resetpassword";
		    }

		    String email = (String) sessionService.get("forgotPasswordEmail");

		    if (email == null || email.isEmpty()) {
		        model.addAttribute("message", "Không thể xác định email người dùng.");
		        return "user/resetpassword";
		    }
		    Account account = dao.findByEmailIgnoreCase(email);
		    if (account == null) {
		        model.addAttribute("message", "Không tìm thấy tài khoản.");
		        return "user/resetpassword";
		    }
		    String encodedPassword = PASSWORD_ENCODER.encode(acc.getPassword());
		    account.setPassword(encodedPassword);
		    dao.save(account);

		    sessionService.set("forgotPasswordEmail", null);
		    sessionService.set("forgotPasswordCode", null);
		    model.addAttribute("message", "Đặt lại mật khẩu thành công. Vui lòng đăng nhập bằng mật khẩu mới.");
		    return "user/login";
		}
		
	    
	}
}
