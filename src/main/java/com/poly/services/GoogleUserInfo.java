package com.poly.services;

import java.util.Map;

import com.poly.security.OAuth2UserInfo;

public class GoogleUserInfo extends OAuth2UserInfo{
	 public GoogleUserInfo(Map<String, Object> attributes) {
		    super(attributes);
		  }

		  @Override
		  public String getId() {
		    return (String) attributes.get("sub");
		  }

		  @Override
		  public String getUsername() {
		    return (String) attributes.get("name");
		  }

		  @Override
		  public String getEmail() {
		    return (String) attributes.get("email");
		  }

}
