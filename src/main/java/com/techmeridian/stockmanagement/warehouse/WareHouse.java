package com.techmeridian.stockmanagement.warehouse;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.techmeridian.stockmanagement.company.Company;

import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name = "ware_house")
@Data
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class WareHouse implements Serializable {

	@Id
	@Column(name = "ware_house_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer wareHouseId;

	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;

	@Column(name = "code")
	private String code;

	@Column(name = "name")
	private String name;

}
