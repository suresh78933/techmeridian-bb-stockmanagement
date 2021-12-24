package com.techmeridian.stockmanagement.journal;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.techmeridian.stockmanagement.company.Company;
import com.techmeridian.stockmanagement.item.Item;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@SuppressWarnings("serial")
@Entity
@Table(name = "phy_journal_item")
@Data
@ToString
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(value = { "changeKey" })
public class PhyJournalItem implements Serializable {

	@Id
	@Column(name = "journal_item_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer journalItemId;

	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;

	@Column(name = "journal_template_name")
	private String journalTemplateName;

	@Column(name = "journal_batch_name")
	private String journalBatchName;

	@Column(name = "line_no")
	private Integer lineNo;

	@Column(name = "posting_date")
	private String postingDate;

	@Column(name = "document_date")
	private String documentDate;

	@Column(name = "location_code")
	private String locationCode;

	@Column(name = "bin_code")
	private String binCode;

	@Column(name = "salespers_purch_code")
	private String salespersPurchCode;

	@Column(name = "entry_type")
	private String entryType;

	@Column(name = "document_no")
	private String documentNo;

	@Column(name = "variant_code")
	private String variantCode;

	@Column(name = "item_no")
	private String itemNo;

	@Column(name = "description")
	private String description;

	@Column(name = "gen_bus_posting_group")
	private String genBusPostingGroup;

	@Column(name = "gen_prod_posting_group")
	private String genProdPostingGroup;

	@Column(name = "qty_calculated")
	private Integer qtyCalculated;

	@Column(name = "qty_phys_inventory")
	private Integer qtyPhysInventory;

	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "unit_of_measure_code")
	private String unitOfMeasureCode;

	@Column(name = "unit_amount")
	private String unitAmount;

	@Column(name = "amount")
	private String amount;

	@Column(name = "indirect_cost_percent")
	private String indirectCostPercent;

	@Column(name = "unit_cost")
	private String unitCost;

	@Column(name = "applies_to_entry")
	private Integer appliesToEntry;

	@Column(name = "phys_inventory")
	private String physInventory;

	@Column(name = "item_description")
	private String itemDescription;

	@Column(name = "reason_code")
	private String reasonCode;

	@Column(name = "mobile_nav_quantity")
	private Integer mobileNavQuantity;

	@Column(name = "change_key")
	private String changeKey;

	@ManyToOne
	@JoinColumn(name = "item_id")
	private Item item;

	@ManyToOne
	@JoinColumn(name = "phy_journal_id")
	private PhyJournal phyJournal;
}
