package com.poly.entity;

import java.io.Serializable;

import com.poly.utils._enum.RoleUserEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;


@SuppressWarnings("serial")
@Entity
@Table
@Data
@NoArgsConstructor
public class Role implements Serializable{
	 @Id
	  private Integer id;
	  private String name;
	  
	  public Role(RoleUserEnum role ) {
	    this.id = role.getValue();
	    this.name = role.name();
	  }
}
