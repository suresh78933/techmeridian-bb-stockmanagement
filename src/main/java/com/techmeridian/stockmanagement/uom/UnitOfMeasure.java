package com.techmeridian.stockmanagement.uom;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "unit_of_measure")
@Data
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class UnitOfMeasure {

	@Id
	@Column(name = "unit_of_measure_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int unitOfMeasureId;

	@Column(name = "code")
	private String code;

	@Column(name = "description")
	private String description;

	@Column(name = "international_standard_code")
	private String internationalStandardCode;

	@Column(name = "e_tag")
	private String eTag;
}
