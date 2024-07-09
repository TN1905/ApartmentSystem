package com.poly.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
	@ManyToOne @JoinColumn(name="accountId")
	Account account;
	@JsonIgnore
	@OneToMany(mappedBy = "apartment")
	List<RentApartmentDetail> rentApartmentDetail;@JsonIgnore
    @OneToMany(mappedBy = "apartment", cascade = CascadeType.ALL)
    private List<ApartmentImage> images;
	private String status;
	@JsonIgnore
    @OneToMany(mappedBy = "apartment", cascade = CascadeType.ALL)
    private List<FavoriteApartment> favoriteApartments;
	public void setImages(List<ApartmentImage> images) {
	    this.images = images;
	}
	@Override
	public String toString() {
		return "Apartment [id=" + id + ", content=" + content + ", ward=" + ward + ", district=" + district + ", city="
				+ city + ", address=" + address + ", price=" + price + ", acreage=" + acreage + ", cityValue="
				 + ", districtValue="  + ", wardValue=" +  ", imagetitle="
				+ ", createdate="
				+ createdate + ", description=" + description
				 + ", status=" + status + "]";
	}
	
	
}
