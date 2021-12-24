package com.techmeridian.stockmanagement.stockissue;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.techmeridian.stockmanagement.company.Company;

public interface StockIssueRepository extends CrudRepository<StockIssue, Serializable> {

	StockIssue findByIssueNo(String issueNo);

	StockIssue findByStockIssueIdAndCompany(Integer stockIssueId, Company company);

	StockIssue findByIssueNoAndCompany(String issueNo, Company company);

	List<StockIssue> findByCompany(Company company);
}
