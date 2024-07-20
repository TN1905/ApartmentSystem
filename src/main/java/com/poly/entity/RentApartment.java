package com.poly.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name="RentApartment")
public class RentApartment implements Serializable {
    @Id
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdate")
    private LocalDateTime createdate = LocalDateTime.now();
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "endate")
    private LocalDateTime enddate;
    
    private String status;
    
    private int monthrent;
    @ManyToOne
    @JoinColumn(name = "accountId")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "apartmentId")
    private Apartment apartment;
    @Column(name = "alert_expire", nullable = true)
    private Boolean alertExpire = false;
    
    
}
