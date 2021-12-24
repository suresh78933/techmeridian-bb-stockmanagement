package com.techmeridian.stockmanagement.purchaseorder;

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
@Table(name = "purchase_orders")
@Data
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class PurchaseOrder implements Serializable {

	@Id
	@Column(name = "purchase_order_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer purchaseOrderId;

	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;

	@Column(name = "number")
	private String number;

	@Column(name = "buy_from_vendor_no")
	private String buyFromVendorNo;

	@Column(name = "buy_from_vendor_name")
	private String buyFromVendorName;

	@Column(name = "buy_from_city")
	private String buyFromCity;

	@Column(name = "buy_from_address")
	private String buyFromAddress;

	@Column(name = "buy_from_address_2")
	private String buyFromAddress2;

	@Column(name = "pay_to_vendor_no")
	private String payToVendorNo;

	@Column(name = "pay_to_name")
	private String payToName;

	@Column(name = "pay_to_city")
	private String payToCity;

	@Column(name = "pay_to_address")
	private String payToAddress;

	@Column(name = "pay_to_address_2")
	private String payToAddress2;

	@Column(name = "order_date")
	private String orderDate;

	@Column(name = "document_date")
	private String documentDate;

	@Column(name = "expected_receipt_date")
	private String expectedReceiptDate;

	@Column(name = "currency_code")
	private String currencyCode;

	@Column(name = "purchaser_code")
	private String purchaserCode;

	@Column(name = "status")
	private String status;

	@Column(name = "ship_to_name")
	private String shipToName;

	@Column(name = "ship_to_city")
	private String shipToCity;

	@Column(name = "ship_to_address")
	private String shipToAddress;

	@Column(name = "ship_to_address_2")
	private String shipToAddress2;

	@Column(name = "vendor_order_no")
	private String vendorOrderNo;

	@Column(name = "vendor_shipment_no")
	private String vendorShipmentNo;

	@Column(name = "vendor_invoice_no")
	private String vendorInvoiceNo;

}
