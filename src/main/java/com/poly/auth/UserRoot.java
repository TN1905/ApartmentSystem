package com.poly.auth;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.poly.entity.Account;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRoot implements UserDetails, OAuth2User {
	
	private Account acc;
	private Collection<? extends GrantedAuthority> authorities;
	private Map<String, Object> attributes;
	
	public static UserRoot create(Account acc) {
	    List<GrantedAuthority> authorities = acc.getRoles()
	        .stream().map(role -> new SimpleGrantedAuthority(role.getName()))
	        .collect(Collectors.toList());
	    return UserRoot.builder()
	        .acc(acc)
	        .authorities(authorities)
	        .build();
	  }

	public static UserRoot create(Account acc, Map<String, Object> attributes) {
	    UserRoot userRoot = UserRoot.create(acc);
	    userRoot.setAttributes(attributes);
	    return userRoot;
	  }

	  @Override
	  public <A> A getAttribute(String name) {
	    return OAuth2User.super.getAttribute(name);
	  }

	  @Override
	  public Map<String, Object> getAttributes() {
	    return attributes;
	  }

	  @Override
	  public Collection<? extends GrantedAuthority> getAuthorities() {
	    return this.authorities;
	  }

	  @Override
	  public String getPassword() {
	    return this.acc.getPassword();
	  }

	  @Override
	  public String getUsername() {
	    return this.acc.getUsername();
	  }

	  @Override
	  public boolean isAccountNonExpired() {
	    return true;
	  }

	  @Override
	  public boolean isAccountNonLocked() {
	    return true;
	  }

	  @Override
	  public boolean isCredentialsNonExpired() {
	    return true;
	  }

	  @Override
	  public boolean isEnabled() {
	    return true;
	  }

	  @Override
	  public String getName() {
	    return this.acc.getUsername();
	  }

}
