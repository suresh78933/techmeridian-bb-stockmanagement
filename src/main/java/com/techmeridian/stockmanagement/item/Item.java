package com.techmeridian.stockmanagement.item;

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
@Table(name = "item_master")
@Data
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class Item implements Serializable {

	@Id
	@Column(name = "item_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer itemId;

	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;

	@Column(name = "item_no")
	private String itemNo;

	@Column(name = "item_desciption")
	private String itemDescription;

	@Column(name = "base_unit_of_measure")
	private String baseUnitOfMeasure;

	@Column(name = "shelf_no")
	private String shelfNo;

	@Column(name = "standard_cost")
	private String standardCost;

	@Column(name = "unit_cost")
	private String unitCost;

	@Column(name = "blocked")
	private String blocked;

	@Column(name = "purchase_unit_of_measure")
	private String purchaseUnitOfMeasure;

	@Column(name = "e_tag")
	private String eTag;

	@Column(name = "production_bom_number")
	private String productionBomNumber;

	@Column(name = "routing_no")
	private String routingNo;

	@Column(name = "vendor")
	private String vendor;

	@Column(name = "part_no")
	private String partNo;

	@Column(name = "inventory")
	private String inventory;

}
