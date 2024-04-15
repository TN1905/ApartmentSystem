package com.poly.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
@Table(name="Apartment")
public class Apartment implements Serializable{
	@Id
	private String id;
	@Column(columnDefinition = "NVARCHAR(MAX)")
	private String content;
	@Column(columnDefinition = "NVARCHAR(MAX)")
	private String ward;
	@Column(columnDefinition = "NVARCHAR(MAX)")
	private String district;
	@Column(columnDefinition = "NVARCHAR(MAX)")
	private String city;
	@Column(columnDefinition = "NVARCHAR(MAX)")
	private String address;
	private Double price;
	private Double acreage;
	private int cityValue;
	private int districtValue;
	private int wardValue;
	private String imagetitle;
	private String image1;
	private String image2;
	private String image3;
	@Temporal(TemporalType.DATE)
	@Column(name = "createdate")
	private Date createdate = new Date();
	@Column(columnDefinition = "NVARCHAR(MAX)")
    private String description;
	@ManyToOne @JoinColumn(name="apartmentTypeId")
	ApartType apartmentType;
	@JsonIgnore
	@OneToMany(mappedBy = "apartment")
	List<RentApartmentDetail> rentApartmentDetail;
	private Boolean status;
	
	@Override
	public String toString() {
		return "Apartment [id=" + id + ", content=" + content + ", ward=" + ward + ", district=" + district + ", city="
				+ city + ", address=" + address + ", price=" + price + ", acreage=" + acreage + ", cityValue="
				+ cityValue + ", districtValue=" + districtValue + ", wardValue=" + wardValue + ", imagetitle="
				+ imagetitle + ", image1=" + image1 + ", image2=" + image2 + ", image3=" + image3 + ", createdate="
				+ createdate + ", description=" + description
				 + ", status=" + status + "]";
	}
	
	
}
