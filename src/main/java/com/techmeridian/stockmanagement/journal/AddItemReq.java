package com.techmeridian.stockmanagement.journal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class AddItemReq {

	private String item;
	private String wareHouseCode;
	private Integer quantity;
	private Integer journalId;
	private Integer purchaseOrderId;
	private Integer stockIssueId;
}
