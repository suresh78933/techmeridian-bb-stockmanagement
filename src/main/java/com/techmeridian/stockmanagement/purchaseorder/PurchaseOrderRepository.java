package com.techmeridian.stockmanagement.purchaseorder;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.techmeridian.stockmanagement.company.Company;

public interface PurchaseOrderRepository extends CrudRepository<PurchaseOrder, Integer> {

	PurchaseOrder findByNumber(String purchaseOrderNo);

	PurchaseOrder findByPurchaseOrderIdAndCompany(Integer purchaseOrderId, Company company);

	PurchaseOrder findByNumberAndCompany(String purchaseOrderNo, Company company);

	List<PurchaseOrder> findByCompany(Company company);
}
