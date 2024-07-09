package com.poly.security;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.poly.dao.AccountDAO;
import com.poly.entity.Account;
import com.poly.entity.Role;
import com.poly.utils._enum.RoleUserEnum;

import jakarta.persistence.Transient;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler{
	@Autowired
	AccountDAO accountDao;
	
	@Transient
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
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
        	  acc.setPhone("");
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
	        	  acc.setPhone("");
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
