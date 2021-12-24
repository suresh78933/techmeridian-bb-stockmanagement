package com.techmeridian.stockmanagement.user;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.techmeridian.stockmanagement.company.Company;
import com.techmeridian.stockmanagement.role.Role;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@SuppressWarnings("serial")
@Entity
@Table(name = "user")
@Data
@ToString
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({ "createdBy", "createdOn", "updatedBy", "updatedOn", "userSecurityId", "winUserName",
		"winSecurityId", "licenseType", "key" })
public class User implements Serializable {

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;

	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "full_name")
	private String fullName;

	@Column(name = "user_security_id")
	private String userSecurityId;

	@Column(name = "email_address")
	private String emailAddress;

	@Column(name = "win_user_name")
	private String winUserName;

	@Column(name = "win_security_id")
	private String winSecurityId;

	@Column(name = "license_type")
	private String licenseType;

	@Column(name = "created_on")
	private Date createdOn;

	@Column(name = "updated_on")
	private Date updatedOn;

	@Transient
	private String password;

	@Transient
	private List<Company> companies;
}
