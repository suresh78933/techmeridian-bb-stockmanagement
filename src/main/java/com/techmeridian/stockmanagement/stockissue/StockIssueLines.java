package com.techmeridian.stockmanagement.stockissue;

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

@Entity
@Table(name = "stock_issue_lines")
@Data
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class StockIssueLines {

	@Id
	@Column(name = "stock_issue_line_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int stockIssueLineId;

	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;

	@ManyToOne
	@JoinColumn(name = "stock_issue")
	private StockIssue stockIssue;

	@Column(name = "issue_document_no")
	private String issueDocumentNo;

	@Column(name = "line_no")
	private Integer lineNo;

	@Column(name = "type")
	private String type;

	@Column(name = "no")
	private String no;

	@Column(name = "part_no")
	private String partNo;

	@Column(name = "shelf_no")
	private String shelfNo;

	@Column(name = "description")
	private String description;

	@Column(name = "hmr_km")
	private String hmrKm;

	@Column(name = "unit_of_measure")
	private String unitOfMeasure;

	@Column(name = "location")
	private String location;

	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "mobile_nav_quantity")
	private Integer mobileNavQuantity;

	@Column(name = "shortcut_dimension_1_code")
	private String shortcutDimension1Code;

	@Column(name = "shortcut_dimension_2_code")
	private String shortcutDimension2Code;

	@Column(name = "available_inventory")
	private String availableInventory;

	@Column(name = "quantity_to_issue")
	private String quantityToIssue;

	@Column(name = "quantity_issued")
	private String quantityIssued;

	@Column(name = "outstanding_quantity")
	private String outstandingQuantity;

	@Column(name = "unit_amount")
	private String unitAmount;

	@Column(name = "amount")
	private String amount;

	@Column(name = "qty_per_unit_of_measure")
	private String qtyPerUnitOfMeasure;

	@Column(name = "total_inventory_on_issue")
	private String totalInventoryOnIssue;

	@Column(name = "change_key")
	private String changeKey;

	@Column(name = "push_to_nav", columnDefinition="TINYINT")
	private Boolean pushToNav;
}
