package com.techmeridian.stockmanagement.purchaseorder;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.techmeridian.stockmanagement.company.Company;
import com.techmeridian.stockmanagement.item.Item;
import com.techmeridian.stockmanagement.user.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "purchase_order_lines")
@Data
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class PurchaseOrderLines {

	@Id
	@Column(name = "purchase_order_line_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int purchaseOrderLineId;

	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;

	@ManyToOne
	@JoinColumn(name = "purchase_order")
	private PurchaseOrder purchaseOrder;

	@Column(name = "document_type")
	private String documentType;

	@Column(name = "document_no")
	@JsonProperty("purchaseOrderNo")
	private String documentNo;

	@Column(name = "line_no")
	private String lineNo;

	@Column(name = "type")
	private String type;

	@Column(name = "item_no")
	private String itemNo;

	@ManyToOne
	@JoinColumn(name = "item_id")
	private Item item;

	@Column(name = "variant_code")
	private String variantCode;

	@Column(name = "service_tax_registration_no")
	private String serviceTaxRegistrationNo;

	@Column(name = "vat_prod_posting_group")
	private String vatProdPostingGroup;

	@Column(name = "description")
	private String description;

	@Column(name = "location_code")
	private String locationCode;

	@Column(name = "bin_code")
	private String binCode;

	@Column(name = "unit_of_measure_code")
	private String unitOfMeasureCode;

	@Column(name = "unit_of_measure")
	private String unitOfMeasure;

	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "job_remaining_quantity")
	private Integer jobRemainingQty;

	@Column(name = "reserved_quantity")
	private Integer reservedQuantity;

	@Column(name = "quantity_to_receive")
	private Integer quantityToReceive;

	@Column(name = "quantity_received")
	private Integer quantityReceived;

	@Column(name = "quantity_to_invoice")
	private Integer quantityToInvoice;

	@Column(name = "quantity_invoiced")
	private Integer quantityInvoiced;

	@Column(name = "quantity_to_assign")
	private Integer quantityToAssign;

	@Column(name = "quantity_assigned")
	private Integer quantityAssigned;

	@Column(name = "quantity_mobile_nav_receieved")
	@JsonProperty("quantityScanned")
	private Integer quantityMobileNavReceieved;

	@Column(name = "direct_unit_cost")
	private String directUnitCost;

	@Column(name = "line_amount")
	private String lineAmount;

	@Column(name = "line_discount_percent")
	private String lineDiscountPercent;

	@Column(name = "line_discount_amount")
	private String lineDiscountAmount;

	@Column(name = "requested_receipt_date")
	private String requestedReceiptDate;

	@Column(name = "promised_receipt_date")
	private String promisedReceiptDate;

	@Column(name = "planned_receipt_date")
	private String plannedReceiptDate;

	@Column(name = "expected_receipt_date")
	private String expectedReceiptDate;

	@Column(name = "order_date")
	private String orderDate;

	@Column(name = "e_tag")
	private String eTag;

	@OneToOne
	@JoinColumn(name = "updated_by")
	private User updatedBy;

	@Column(name = "updated_on")
	private Date updatedOn;

	@Column(name = "change_key")
	private String changeKey;
	
	@Column(name = "part_no")
	private String partNo;
	
	@Column(name = "push_to_nav", columnDefinition="TINYINT")
	private Boolean pushToNav;
}
