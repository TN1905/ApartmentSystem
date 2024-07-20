package com.poly.utils;

import lombok.Data;

@Data
public class _enum {
	public enum RoleUserEnum {
	    ADMIN(1),
	    USER(2),
	    POSTER(3);

	    private final int value;

	    RoleUserEnum(int value) {
	      this.value = value;
	    }

	    public int getValue() {
	      return this.value;
	    }

	  }
}
