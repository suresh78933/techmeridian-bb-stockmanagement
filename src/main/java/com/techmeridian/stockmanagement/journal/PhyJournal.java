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

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "phy_journal")
@Data
@ToString
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class PhyJournal {

	@Id
	@Column(name = "phy_journal_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer phyJournalId;

	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;

	@Column(name = "phy_journal_template")
	private String journalTemplateName;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "no_series")
	private String noSeries;

	@Column(name = "location_code")
	private String locationCode;

}
