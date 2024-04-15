package com.poly.entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="apartmentType")
public class ApartType implements Serializable{
	@Id
	private String id;
	@Column(columnDefinition = "NVARCHAR(MAX)")
	private String name;
	@JsonIgnore
	@OneToMany(mappedBy="apartmentType")
	List<Apartment> apartment;
	
	@Override
	public String toString() {
	    return getId(); // hoặc bất kỳ trường nào mà bạn muốn hiển thị trong select box
	}
}
