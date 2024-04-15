package com.poly.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rented {
	@Id
	String id;
	String name;
	String person;
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdate")
    private LocalDateTime createdate = LocalDateTime.now();
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "endate")
    private LocalDateTime enddate;
}
