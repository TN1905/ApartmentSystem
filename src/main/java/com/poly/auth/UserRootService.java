package com.poly.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.poly.dao.AccountDAO;
import com.poly.entity.Account;
import com.poly.entity.ReCapchaResponse;
import com.poly.services.SessionService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserRootService implements UserDetailsService{
	private final AccountDAO accDao;
	@Autowired
	SessionService sessionService;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	HttpServletRequest req;
	public UserRootService(AccountDAO accDao) {
	    this.accDao = accDao;
	  }
	
	public UserDetails loadUserByUsername(String username)
		      throws UsernameNotFoundException {
		String captchaResponse = req.getParameter("g-recaptcha-response");
		String url = "https://www.google.com/recaptcha/api/siteverify";
		String param = "?secret=6LeLiE8pAAAAAN7_WEtfdxzUynvA1dR_dijzuYEd&response=" + captchaResponse;
		ReCapchaResponse reCaptchaResponse = restTemplate.exchange(url+param, HttpMethod.POST, null, ReCapchaResponse.class).getBody();
		System.out.println(reCaptchaResponse);
		sessionService.set("captcha", reCaptchaResponse);
		System.out.println("???");
		    Account acc = accDao.findByUsername(username);
		    return UserRoot.create(acc);
		  }
}
