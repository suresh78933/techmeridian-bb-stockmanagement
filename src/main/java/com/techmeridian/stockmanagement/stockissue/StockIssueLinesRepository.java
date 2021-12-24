package com.techmeridian.stockmanagement.stockissue;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.techmeridian.stockmanagement.company.Company;

public interface StockIssueLinesRepository extends CrudRepository<StockIssueLines, Serializable> {

	List<StockIssueLines> findByStockIssue(StockIssue stockIssue);

	List<StockIssueLines> findByCompany(Company company);

	StockIssueLines findByNoAndStockIssue(String no, StockIssue stockIssue);

	List<StockIssueLines> findByStockIssueAndCompany(StockIssue stockIssue, Company company);
}
