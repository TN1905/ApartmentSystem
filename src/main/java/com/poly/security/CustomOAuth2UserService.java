package com.poly.security;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.poly.auth.UserRoot;
import com.poly.dao.AccountDAO;
import com.poly.entity.Account;
import com.poly.entity.ReCapchaResponse;
import com.poly.entity.Role;
import com.poly.services.GoogleUserInfo;
import com.poly.services.SessionService;
import com.poly.utils._enum.RoleUserEnum;

import jakarta.persistence.Transient;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Component
public class CustomOAuth2UserService extends DefaultOAuth2UserService implements AuthenticationSuccessHandler{
	@Autowired
	private final AccountDAO accountDao;
	@Transient
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
	@Autowired
	HttpServletRequest req;
	@Autowired
	SessionService sessionService;
	@Autowired
	private RestTemplate restTemplate;
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//		String captchaResponse = req.getParameter("g-recaptcha-response");
//		String url = "https://www.google.com/recaptcha/api/siteverify";
//		String param = "?secret=6LeLiE8pAAAAAN7_WEtfdxzUynvA1dR_dijzuYEd&response=" + captchaResponse;
//		ReCapchaResponse reCaptchaResponse = restTemplate.exchange(url+param, HttpMethod.POST, null, ReCapchaResponse.class).getBody();
//		System.out.println(reCaptchaResponse);
//		sessionService.set("captcha", reCaptchaResponse);
//		sessionService.set("login-capcha", recaptcha);
		OAuth2User user = super.loadUser(userRequest);
		String typeOAuth = userRequest.getClientRegistration().getClientName();
		System.out.println(typeOAuth);
		Account userOptional;
		OAuth2UserInfo oAuth2UserInfo = switch (typeOAuth) {
		case "Google" -> new GoogleUserInfo(user.getAttributes());
		case "Facebook" -> new GoogleUserInfo(user.getAttributes());
		default -> new GoogleUserInfo(user.getAttributes());
		};
		
		if(typeOAuth.equals("Google")) {
			userOptional  = accountDao.findByUsername(oAuth2UserInfo.getEmail());
		}else if(typeOAuth.equals("Facebook")) {
			System.out.println(oAuth2UserInfo.getEmail());
			System.out.println(oAuth2UserInfo.getEmail());
			userOptional  = accountDao.findByUsername(oAuth2UserInfo.getEmail());
		}else {
			userOptional  = accountDao.findByUsername(oAuth2UserInfo.getEmail());
		}
		
		Account acc;
		
		System.out.println(userOptional);
		System.out.println(userOptional);
		System.out.println(oAuth2UserInfo.getUsername());
		System.out.println(oAuth2UserInfo.getUsername());
		System.out.println(oAuth2UserInfo.getEmail());
		System.out.println(oAuth2UserInfo.getEmail());
		return UserRoot.create(userOptional, oAuth2UserInfo.getAttributes());
	}
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
//		String captchaResponse = req.getParameter("g-recaptcha-response");
//		String url = "https://www.google.com/recaptcha/api/siteverify";
//		String param = "?secret=6LeLiE8pAAAAAN7_WEtfdxzUynvA1dR_dijzuYEd&response=" + captchaResponse;
//		ReCapchaResponse reCaptchaResponse = restTemplate.exchange(url+param, HttpMethod.POST, null, ReCapchaResponse.class).getBody();
//		System.out.println(reCaptchaResponse);
//		sessionService.set("captcha", reCaptchaResponse);
//		sessionService.set("login-capcha", recaptcha);
		System.out.println(".....");
		System.out.println(authentication);
		String redirectUrl = null;
		if(authentication.getPrincipal() instanceof DefaultOAuth2User) {
		DefaultOAuth2User  userDetails = (DefaultOAuth2User ) authentication.getPrincipal();
		System.out.println(userDetails);
         String username = userDetails.getAttribute("email") !=null?userDetails.getAttribute("email"):userDetails.getAttribute("login")+"@gmail.com" ;
        
          if(accountDao.findByUsername(username) == null) {
        	  long currentTimeMillis = System.currentTimeMillis();
        	  Account acc = new Account();
        	  acc.setId(currentTimeMillis);
        	  acc.setUsername(username);
        	  acc.setFirstname("none");
        	  acc.setLastname("none"); 
        	  acc.setPassword(PASSWORD_ENCODER.encode("Yummy12"));
        	  acc.setPhone(0);
        	  acc.setGender("other");
        	  acc.setEmail(username);

        	  
        	  accountDao.save(acc);
          }
		}else if(authentication.getPrincipal() instanceof CustomOAuth2User) {
			System.out.println("cai nay vao khong");
			CustomOAuth2User userDetails = (CustomOAuth2User) authentication.getPrincipal();
			String username = userDetails.getAttribute("email") !=null?userDetails.getAttribute("email"):userDetails.getAttribute("login")+"@gmail.com" ;
			System.out.println(username);
			if(accountDao.findByUsername(username) == null) {
	        	  System.out.println("cai nay vao khong1");
	        	  System.out.println();
	        	  long currentTimeMillis = System.currentTimeMillis();
	        	  Account acc = new Account();
	        	  acc.setId(currentTimeMillis);
	        	  acc.setUsername(username);
	        	  acc.setFirstname("none");
	        	  acc.setLastname("none"); 
	        	  acc.setPassword(PASSWORD_ENCODER.encode("Yummy12"));
	        	  acc.setPhone(0);
	        	  acc.setGender("other");
	        	  acc.setEmail(username);
	        	  Set<Role> roles = new HashSet<>();
	              roles.add(new Role(RoleUserEnum.USER));
	              acc.setRoles(roles);
	        	  System.out.println(acc);
	        	  accountDao.save(acc);
	        	  System.out.println("cai nay vao khong2");
	          }
		}
		redirectUrl = "/user/index";
		new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
		
	}
}
