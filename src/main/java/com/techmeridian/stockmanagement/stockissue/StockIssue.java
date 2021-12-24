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
@Table(name = "stock_issue")
@Data
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class StockIssue {

	@Id
	@Column(name = "stock_issue_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer stockIssueId;

	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;

	@Column(name = "issue_no")
	private String issueNo;

	@Column(name = "issue_description")
	private String issueDescription;

	@Column(name = "issue_ref_no")
	private String issueRefNo;

	@Column(name = "status")
	private String status;

	@Column(name = "issue_raise_by")
	private String issueRaiseBy;

	@Column(name = "issue_raised_on")
	private String issueRaisedOn;

	@Column(name = "issued_by")
	private String issuedBy;

	@Column(name = "issued_on")
	private String issuedOn;

	@Column(name = "issued_to")
	private String issuedTo;

	@Column(name = "global_dimension_1_code")
	private String globalDimension1Code;

	@Column(name = "global_dimension_2_code")
	private String globalDimension2Code;

	@Column(name = "location")
	private String location;

}
