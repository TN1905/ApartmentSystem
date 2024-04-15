package com.poly.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;



@Service
public class SessionService {
	@Autowired
	HttpSession session;
	
	@SuppressWarnings("uncheck")
	public <T> T get(String name) {
		return (T) session.getAttribute(name);
	}
	
	public <T> T get(String name, T defaultValue) {
	    Object value = session.getAttribute(name);
	    return value != null ? (T) value : defaultValue;
	}

	
	public void set(String name,Object value) {
		session.setAttribute(name, value);
	}
	
	public void remove(String name) {
		session.removeAttribute(name);
	}
}
