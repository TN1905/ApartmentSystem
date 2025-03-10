package com.poly.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poly.utils._enum.RoleUserEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Table(name="accounts")
@Builder
public class Account implements Serializable{
	@Id
	private long id;
	private String username;
	private String password;
	@Column(columnDefinition = "NVARCHAR(MAX)")
	private String firstname;
	@Column(columnDefinition = "NVARCHAR(MAX)")
	private String lastname;
	private String email;
	private String phone;
	@Column(columnDefinition = "NVARCHAR(MAX)")
	private String gender;
	@Builder.Default
	@ManyToMany(fetch = FetchType.EAGER)
	  @JoinTable(name = "user_role",
	      joinColumns = {@JoinColumn(name = "account_id", referencedColumnName = "id")},
	      inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
	  private Set<Role> roles = new HashSet<>();
	  @Builder.Default
	
	@Transient
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean checkPassword(String rawPassword) {
        return PASSWORD_ENCODER.matches(rawPassword, this.password);
    }
    @JsonIgnore
    @OneToMany(mappedBy="account", fetch = FetchType.EAGER)
	List<RentApartment> rentApartment;
    @Enumerated(EnumType.STRING)
    private Provider provider;
    @JsonIgnore
    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    private List<FavoriteApartment> favoriteApartments;
    
    @JsonIgnore
    @OneToOne(mappedBy="account",fetch=FetchType.EAGER)
    Wallet wallet;
    @JsonIgnore
    @OneToOne(mappedBy="account",fetch=FetchType.EAGER)
    ConfirmationToken confirmationToken;
    @JsonIgnore
    @OneToOne(mappedBy="account",fetch=FetchType.EAGER)
    AccountSession accountSession;
    @JsonIgnore
    @OneToMany(mappedBy="account", fetch = FetchType.EAGER)
	List<Apartment> apartment;
    private boolean isEnabled;
    
    public Provider getProvider() {
        return provider;
    }
 
    public void setProvider(Provider provider) {
        this.provider = provider;
    }
    
    public String getRoleString() {
        return this.roles.stream().map(r -> r.getName()).collect(Collectors.joining(", "));
      }
    
    public Account(long id, String username, String password, String firstname, String lastname, String email, String phone, String gender,boolean isEnabled) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.roles = new HashSet<>();
        this.roles.add(new Role(RoleUserEnum.USER));
        this.isEnabled = isEnabled;
    }
    
    @Override
    public String toString() {
        return "Account(id=" + id + ", username=" + username + ", firstname=" + firstname + ", lastname=" + lastname + ", email=" + email + ", phone=" + phone + ", gender=" + gender + ", isEnabled=" + isEnabled +")";
    }

}
