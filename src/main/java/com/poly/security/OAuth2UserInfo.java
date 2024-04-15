package com.poly.security;

import java.util.Map;

import lombok.Getter;

@Getter
public abstract class OAuth2UserInfo {
	protected Map<String, Object> attributes;

	  public OAuth2UserInfo(Map<String, Object> attributes) {
		  System.out.println(attributes);
	    this.attributes = attributes;
	  }

	  public abstract String getId();

	  public abstract String getUsername();

	  public abstract String getEmail();

}
