package com.techmeridian.stockmanagement.vendor;

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
@Table(name = "vendor_list")
@Data
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class VendorList implements Serializable {

	@Id
	@Column(name = "vendor_list_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer vendorListId;

	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;

	@Column(name = "number")
	private String number;

	@Column(name = "name")
	private String name;

	@Column(name = "responsibility_center")
	private String responsibilityCenter;

	@Column(name = "location_code")
	private String locationCode;

	@Column(name = "post_code")
	private String postCode;

	@Column(name = "country_region_code")
	private String countryRegionCode;

	@Column(name = "phone_no")
	private String phoneNo;

	@Column(name = "fax_no")
	private String faxNo;

	@Column(name = "contact")
	private String contact;

	@Column(name = "purchaser_code")
	private String purchaserCode;

	@Column(name = "vendor_posting_group")
	private String vendorPostingGroup;

	@Column(name = "gen_bus_posting_group")
	private String genBusPostingGroup;

	@Column(name = "vat_bus_posting_group")
	private String vatBusPostingGroup;

	@Column(name = "search_name")
	private String searchName;

	@Column(name = "shipment_method_code")
	private String shipmentMethodCode;

	@Column(name = "currency_code")
	private String currencyCode;

	@Column(name = "language_code")
	private String languageCode;

}
