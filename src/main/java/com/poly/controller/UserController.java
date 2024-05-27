package com.poly.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.poly.auth.UserRoot;
import com.poly.config.Environment;
import com.poly.dao.AccountDAO;
import com.poly.dao.ApartTypeDAO;
import com.poly.dao.ApartmentDAO;
import com.poly.dao.RentApartmentDetailDAO;
import com.poly.entity.Account;
import com.poly.entity.ApartType;
import com.poly.entity.Apartment;
import com.poly.entity.HistoryRented;
import com.poly.entity.ReCapchaResponse;
import com.poly.entity.Rented;
import com.poly.entity.Role;
import com.poly.enums.RequestType;
import com.poly.payment.PaymentResponse;
import com.poly.processor.CreateOrderMoMo;
import com.poly.security.CustomOAuth2User;
import com.poly.services.MailerServiceImpl;
import com.poly.services.SessionService;
import com.poly.
services.UserService;
import com.poly.utils.LogUtils;
import com.poly.utils._enum.RoleUserEnum;

import jakarta.mail.MessagingException;
import jakarta.persistence.Transient;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;

@Controller
public class UserController {
	@Autowired
	ApartTypeDAO apartTypeDao;
	@Autowired
	AccountDAO dao;
	@Autowired
	MailerServiceImpl mailer;
	
	@Autowired
	SessionService sessionService;
	
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	ApartmentDAO apartmentDao;
	
	@Autowired
	RentApartmentDetailDAO reportDao;
	
	@Autowired
	HttpServletRequest req;

	@Transient
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
	
	@RequestMapping("/user/login")
	public String login(Model model) {
		model.addAttribute("messageCodeLoginFail",null);
		model.addAttribute("messageCodeLoginSuccess",null);

		return "user/login";
		
	}
	
	@RequestMapping("/user/body")
	public String body(Model model) {
	
		System.out.println("lmm");
		return "user/body2";
		
	}
	
	@RequestMapping("/user/logout")
	public String logout(Model model) {
		sessionService.set("currentUser", null);
		return "user/login";
		
	}
	
	@RequestMapping("/user/index")
	public String index(Model model, Authentication auth) {
		System.out.println("?");
		ReCapchaResponse reCaptchaResponse = (ReCapchaResponse) sessionService.get("captcha");
//		String captchaResponse = sessionService.get("login-capcha");
//		String url = "https://www.google.com/recaptcha/api/siteverify";
//		String param = "?secret=6LeLiE8pAAAAAN7_WEtfdxzUynvA1dR_dijzuYEd&response=" + captchaResponse;
//		ReCapchaResponse reCaptchaResponse = restTemplate.exchange(url+param, HttpMethod.POST, null, ReCapchaResponse.class).getBody();
//		System.out.println(reCaptchaResponse);
		SecurityContext securityContext = SecurityContextHolder.getContext();
		System.out.println(securityContext);
		System.out.println(securityContext.getAuthentication().getPrincipal());
		if(securityContext.getAuthentication().getPrincipal() instanceof DefaultOAuth2User) {
			DefaultOAuth2User user = (DefaultOAuth2User) securityContext.getAuthentication().getPrincipal();
			String username = user.getAttribute("email");
			Account acc = dao.findByUsername(username);
			sessionService.set("currentUser", acc);
			System.out.println(acc);
			return "user/body";
		}else if(securityContext.getAuthentication().getPrincipal() instanceof UserRoot) {
			if(reCaptchaResponse.isSuccess()) {
				UserRoot user = (UserRoot) securityContext.getAuthentication().getPrincipal();
				
				String username = user.getAcc().getUsername();
				System.out.println(user);
				System.out.println(username);
				Account acc = dao.findByUsername(username);
				System.out.println(acc);
				sessionService.set("currentUser", acc);
				System.out.println(acc);
				return "user/body";
			}else {
				model.addAttribute("messageCodeLoginFail","Vui lòng xác thực Captcha");
				return "user/login";
			}
			
		}
		return "user/body";
		
		
	}
	
	@RequestMapping("/user/register")
	public String register(Model model,@ModelAttribute("account") Account account) {

	        return "user/register";
		
	}
	
	@RequestMapping("/user/signup")
	public String signup(@RequestParam("confirmPass") String confirmPass,
			@RequestParam("g-recaptcha-response") String captchaResponse,Model model,@Valid @ModelAttribute("account") Account acc,BindingResult result) throws MessagingException,ServletException, IOException{
		model.addAttribute("messageRegister",null);
		sessionService.set("accResgister", null);
		sessionService.set("codeRegister", null);
		String url = "https://www.google.com/recaptcha/api/siteverify";
		String param = "?secret=6LeLiE8pAAAAAN7_WEtfdxzUynvA1dR_dijzuYEd&response=" + captchaResponse;	
		ReCapchaResponse reCaptchaResponse = restTemplate.exchange(url+param, HttpMethod.POST, null, ReCapchaResponse.class).getBody();
		if(result.hasErrors()) {
			model.addAttribute("messageRegister", "Some fields are not valid. Please fix them!");
			return "user/register";
		}
		else {
			if(reCaptchaResponse.isSuccess()) {
					
					
					sessionService.set("accResgister", null);
					long currentTimeMillis = System.currentTimeMillis();
			        System.out.println("Current Time in Milliseconds: " + currentTimeMillis);
			        if(acc.getPassword().equals(confirmPass)) {
			        	acc.setId(currentTimeMillis);
			        	acc.setPassword(confirmPass);
			        	
			        	Set<Role> roles = new HashSet<>();
		                roles.add(new Role(RoleUserEnum.USER));
		                acc.setRoles(roles);
			        	sessionService.set("accResgister", acc);
			        	System.out.println(acc);
			        	String code = mailer.send(acc.getEmail(), "Confirm Register", "Your 6 digits are ");
			        	sessionService.set("codeRegister", code);
			        	return "user/confirmRegister";	
			        }else {
			        	model.addAttribute("messageRegister","Confirm Password not match");
			        	return "user/register";
			        }
			        	
			}else {
				model.addAttribute("messageRegister","captcha not successfully");
				return "user/register";
			}		
		}
		
	}
	
	@RequestMapping("/user/confirmRegister")
	public String confirmRegister(Model model,@RequestParam("codeRegister") String code) {
		System.out.println(sessionService.get("codeRegister").toString());
		Account acc = sessionService.get("accResgister");
		System.out.println(acc.getLastname());
		model.addAttribute("messageCodeRegister",null);
		if(code.equals(sessionService.get("codeRegister"))) {
		
			acc.setPassword(PASSWORD_ENCODER.encode(acc.getPassword()));
			
			System.out.println(acc);
			dao.save(sessionService.get("accResgister"));
			model.addAttribute("messageCodeRegisterSuccess","Create account successfully");
			return "user/confirmRegister";	
		}else {
			model.addAttribute("messageCodeRegisterFail","Wrong code, re confirm 6 digits please");
			return "user/confirmRegister";	
		}
	}
	
	@RequestMapping("user/signin")
	public String signin(@RequestParam("username") String username, @RequestParam("password") String password,
			@RequestParam("g-recaptcha-response") String captchaResponse,Model model) {
		model.addAttribute("messageCodeLoginFail",null);
		String url = "https://www.google.com/recaptcha/api/siteverify";
		String param = "?secret=6LeLiE8pAAAAAN7_WEtfdxzUynvA1dR_dijzuYEd&response=" + captchaResponse;
		ReCapchaResponse reCaptchaResponse = restTemplate.exchange(url+param, HttpMethod.POST, null, ReCapchaResponse.class).getBody();
		System.out.println(reCaptchaResponse);
		if(reCaptchaResponse.isSuccess()) {
			Account acc = dao.findByUsername(username);
			System.out.println(acc);
			if(acc!=null) {
				if(acc.checkPassword(password)) {
					System.out.println(acc.getRoleString());
					if(acc.getRoleString().equals("USER")) {
						
						System.out.println("vo cai nay khong");
						sessionService.set("currentUser", acc);
						return "/account/login-check";
					}else {		
						sessionService.set("currentUser", acc);
						return "/account/login-check";
					}
//					
				}else {
					model.addAttribute("messageCodeLoginFail","Mật khẩu không đúng");
					return "user/login";
				}
			}else {
				model.addAttribute("messageCodeLoginFail","Tài khoản không đúng");
				return "user/login";
			}
		}else {
			model.addAttribute("messageCodeLoginFail","Vui lòng xác thực");
			return "user/login";
		}
	}
	
	@RequestMapping("/user/fail")
	public String fail(Model model) {
		model.addAttribute("messageCodeLoginFail","Mật khẩu hoặc tài khoản không chính xác");
		return "user/login";
	}
	
	@RequestMapping("/user/denied")
	public String denied(Model model) {
		sessionService.set("currentUser", new Account());
		model.addAttribute("messageCodeLoginFail","Vui lòng đăng nhập với tài khoản admin");
		return "user/login";
	}
	
	@RequestMapping("/account/login-check")
	public String check(Model model,@RequestParam("g-recaptcha-response") String captchaResponse) {
		model.addAttribute("messageCodeLoginFail",null);
		String url = "https://www.google.com/recaptcha/api/siteverify";
		String param = "?secret=6LeLiE8pAAAAAN7_WEtfdxzUynvA1dR_dijzuYEd&response=" + captchaResponse;
		ReCapchaResponse reCaptchaResponse = restTemplate.exchange(url+param, HttpMethod.POST, null, ReCapchaResponse.class).getBody();
		System.out.println(reCaptchaResponse);
		if(reCaptchaResponse.isSuccess()) {
			
			return "user/login";
			
		}else {
			model.addAttribute("messageCodeLoginFail","Vui lòng xác thực");
			return "user/login";
		}

	}
	
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
	
//	@RequestMapping("/user/listapartment")
//	public String listapartment(@RequestParam("field") Optional<String> field,Model model,
//			@RequestParam("p") Optional<Integer> p) {
//
//		int pageNumber = p.orElse(0);
//        int pageSize = 6;
//        
//        // Xác định trường sắp xếp
//        Sort sort = Sort.by(Sort.Direction.DESC, field.orElse("price"));
//        
//        // Tạo đối tượng Pageable để truy vấn dữ liệu
//        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
//	
//        String API_URL = "http://localhost:8080/rest/apartments";
//        ResponseEntity<List<Apartment>> response = restTemplate.exchange(API_URL, HttpMethod.GET, null, 
//            new ParameterizedTypeReference<List<Apartment>>() {});
//        
//        // Kiểm tra xem phản hồi có thành công không
//        if (response.getStatusCode().is2xxSuccessful()) {
//            // Lấy danh sách căn hộ từ phản hồi
//            List<Apartment> apartmentList = response.getBody();
//            model.addAttribute("listApartment", apartmentList);
//            System.out.println(apartmentList);
//        } else {
//            System.out.println("Không thể lấy dữ liệu từ API.");
//        }
//        
//        // Trả về view
//        return "user/listapartment";
//	}
	
	@RequestMapping("/user/filterapartment")
	public String listapartment(@RequestParam("field") Optional<String> field,Model model,
			@RequestParam("p") Optional<Integer> p,
            @RequestParam("apartType") String apartType,
            @RequestParam("priceFilter") String priceFilter,
            @RequestParam("cityFilter") int cityFilter,
            @RequestParam("districtFilter") int districtFilter,
            @RequestParam("wardFilter") int wardFilter) {

		Sort sort = Sort.by(Direction.DESC, field.orElse("price"));
		model.addAttribute("field", field.orElse("price"));
		Pageable pageable = PageRequest.of(p.orElse(0), 6, sort);
		double min = 0;
		double max = 0;
		
		if(!apartType.isEmpty()) {
			if(priceFilter.equals("1to2")){
				min = 1000000;
				max = 2000000;
			}else if(priceFilter.equals("2to3")) {
				min = 2000000;
				max = 3000000;
			}else if(priceFilter.equals("3to5")) {
				min = 3000000;
				max = 5000000;
			}else if(priceFilter.equals("5to10")) {
				min = 5000000;
				max = 10000000;
			}else if(priceFilter.equals("upto10")) {
				min = 10000000;
				max = 1000000000;
			}
//			List<Apartment> listApartment = apartmentDao.findFilter(min, max,apartType,cityFilter,districtFilter,wardFilter);
//			sessionService.set("listApartment", new PageImpl<>(listApartment, pageable, listApartment.size()));
		}else {
//			Page<Apartment> page = apartmentDao.findAllByStatus(true,pageable);

		}
		req.setAttribute("views","/WEB-INF/views/user/listApartmentForm.jsp");
		return "/WEB-INF/views/user/index.jsp";
	}
	
	
	
	@RequestMapping("/detailapartment/{id}")
	public String detailApartment(Model model,@PathVariable("id") String apartmentId) {

		model.addAttribute("apartmentId",apartmentId);
		System.out.println(apartmentId);
		return "user/detailapartment1";
	}
	
	@GetMapping("/login/google")
    public String redirectToGoogleLoginPage() {
        // Thực hiện redirect hoặc mở popup để đăng nhập bằng Google
        return "redirect:/oauth2/authorization/google";
    }
	
	@GetMapping("/login/facebook")
    public String redirectToFacebookLoginPage() {
        // Thực hiện redirect hoặc mở popup để đăng nhập bằng Google
        return "redirect:/oauth2/authorization/facebook";
    }
	
//	@GetMapping("/user/userInfo")
//    public String userInfo(Model model) {
//		
//		Account acc = sessionService.get("currentUser");
//        if (acc != null) {
//            model.addAttribute("currentUser", acc);
//            req.setAttribute("views","/WEB-INF/views/user/userInfo.jsp");
//    		return "/WEB-INF/views/user/index.jsp";
//        } else {
//        	req.setAttribute("views","/WEB-INF/views/user/loginForm.jsp");
//    		return "/WEB-INF/views/user/index.jsp";
//        }
//    }
	
	@GetMapping("/editUser")
    public String editUser(Model model) {
        // Xử lý logic hiển thị form edit
        return "editUserForm";
    }
	@Autowired
    private UserService userService;

//	@PostMapping("/user/updateUser")
//	public String updateUser(
//			@RequestParam("username") String username,
//            @RequestParam("lastname") String lastname,
//            @RequestParam("firstname") String firstname,
//            @RequestParam("email") String email,
//            @RequestParam("phone") String phone,
//            @RequestParam("password") String password,
//            @RequestParam("gender") String gender,
//            Model model) {
//
//	    Account currentUser = dao.findByUsername(username);
//	    if (currentUser != null) {
//	        currentUser.setLastname(lastname);
//	        currentUser.setFirstname(firstname);
//	        currentUser.setEmail(email);
//
//	        // Ép kiểu và kiểm tra cho trường phone
//	        if (phone != null && !phone.isEmpty()) {
//	            try {
//	                int phoneNumber = Integer.parseInt(phone);
//	                currentUser.setPhone(phoneNumber);
//	            } catch (NumberFormatException e) {
//	                // Xử lý khi không thể chuyển đổi thành kiểu int
//	                // Ví dụ: log lỗi, thông báo cho người dùng, ...
//	            }
//	        }
//	        currentUser.setPassword(PASSWORD_ENCODER.encode(password));
//	        currentUser.setGender(gender);
//
//	        // Lưu thông tin người dùng đã cập nhật vào cơ sở dữ liệu
//	        dao.save(currentUser);
//	        Account updatedUser = dao.findByUsername(username);
//	        sessionService.set("currentUser", updatedUser);
//	    }
//
//	    // Cập nhật thông tin người dùng
//	    
//	    return "redirect:/user/userInfo";
//	}




    @GetMapping("/deleteUser")
    public String deleteUser(Model model) {
        // Xử lý logic hiển thị form xác nhận xóa
        return "deleteUserForm";
    }

    @PostMapping("/confirmDeleteUser")
    public String confirmDeleteUser(Model model) {
        // Xử lý logic xác nhận xóa người dùng
        // Lưu ý: cần kiểm tra tính hợp lệ của dữ liệu và thực hiện xóa trong database
        return "redirect:/logout.html";
    }
    
    @RequestMapping("/user/historyrent")
    public String historyrent(Model model) {
    	Account acc = sessionService.get("currentUser");
    	List<HistoryRented> items = reportDao.getHistoryRent(acc.getUsername());
    	model.addAttribute("items",items);
    	req.setAttribute("views","/WEB-INF/views/user/historyrent.jsp");
		return "/WEB-INF/views/user/index.jsp";
    }
    
    @RequestMapping("/account/accessDenied")
    public String accessDenied(Model model) {
    	model.addAttribute("messageCodeLoginFail","Vui lòng đăng nhập với tài khoản admin");
    	sessionService.set("currentUser", null);
		req.setAttribute("views","/WEB-INF/views/user/loginForm.jsp");
		return "/WEB-INF/views/user/index.jsp";
    }
    

//	---------------------------------------------------------------------------------------------------------
//	---------------------------------------------------------------------------------------------------------
	@RequestMapping("/user/userInfo")
    public String userInfo(Model model) {
		System.out.println("0000000000000000000000000");
		Account acc = sessionService.get("currentUser");
        if (acc != null) {
            model.addAttribute("profile", acc);
            System.out.println("1111111111111111111111111");
    		return "user/userInfo";
    		
        } else {
        	System.out.println("222222222222222222222222");
    		return "user/login";
        }
    }
	
//	---------------------------------------------------------------------------------------------------------

	@RequestMapping("/user/updateUser")
	public String updateUser(
			@ModelAttribute("profile") Account acc,
            Model model) {
		System.out.println("322333333333333333333333333");
	    Account currentUser = dao.findByUsername(acc.getUsername());
	    currentUser.setFirstname(acc.getFirstname());
	    currentUser.setGender(acc.getGender());
	    currentUser.setLastname(acc.getLastname());
	    currentUser.setPhone(acc.getPhone());
	    currentUser.setEmail(acc.getEmail());

	        // Lưu thông tin người dùng đã cập nhật vào cơ sở dữ liệu
	        dao.save(currentUser);
	     // Cập nhật thông tin người dùng
//	        return "user/updateUser";
	    

        sessionService.set("currentUser", currentUser);
        model.addAttribute("profile", currentUser);
	    
	    return "user/userInfo";
	}
	
	@RequestMapping("/user/changePass")
	public String changePass(Model model) {
		Account acc = sessionService.get("currentUser");
		model.addAttribute("acc",acc);
		return "user/changePass";
		
	}
	
	@RequestMapping("/user/updatePass")
	public String updatePass(@RequestParam("oldpass") String oldpass,@RequestParam("confirmPass") String confirmPass,Model model,
			@Valid @ModelAttribute("acc") Account acc,BindingResult result) throws MessagingException,ServletException, IOException {
		Account account = sessionService.get("currentUser");
		if(account.checkPassword(oldpass)) {
			if(result.hasFieldErrors("password")) {
				model.addAttribute("messagechange","Lỗi mật khẩu");
				return "user/changePass";
			}else {
				if(acc.getPassword().equals(confirmPass)) {
					String encodedPassword = PASSWORD_ENCODER.encode(acc.getPassword());
					account.setPassword(encodedPassword);
					dao.save(account);
					model.addAttribute("messagechange","Đổi pass thành công");
					return "user/changePass";
				}else {
					model.addAttribute("messagechange","Mật khẩu nhập lại sai");
					return "user/changePass";
				}
			}
		}else {
			model.addAttribute("messagechange","Mật khẩu cũ không chính xác");
			return "user/changePass";
		}
	}
	
	@RequestMapping("user/listapartment1")
    public String apartment() {
    	return "user/listapartment2";
    }

//	---------------------------------------------------------------------------------------------------------
//	---------------------------------------------------------------------------------------------------------
    

	
	

	
	
}
