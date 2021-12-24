package com.techmeridian.stockmanagement.journal;

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
import com.techmeridian.stockmanagement.warehouse.WareHouse;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "phy_journal_template")
@Data
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class PhyJournalTemplate {

	@Id
	@Column(name = "phy_journal_template_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer phyJournalTemplateId;

	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;

	@ManyToOne
	@JoinColumn(name = "ware_house_id")
	private WareHouse wareHouse;

	@Column(name = "name")
	private String name;
}
