package com.techmeridian.stockmanagement.purchaseorder;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.techmeridian.stockmanagement.company.Company;
import com.techmeridian.stockmanagement.item.Item;

public interface PurchaseOrderLinesRepository extends CrudRepository<PurchaseOrderLines, Integer> {

	List<PurchaseOrderLines> findByCompany(Company company);

	List<PurchaseOrderLines> findByItemNo(String itemNo);

	PurchaseOrderLines findByItemAndPurchaseOrder(Item item, PurchaseOrder purchaseOrder);

	List<PurchaseOrderLines> findByDocumentNo(String purchaseOrderNo);

	List<PurchaseOrderLines> findByPurchaseOrder(PurchaseOrder purchaseOrder);

	List<PurchaseOrderLines> findByPurchaseOrderAndCompany(PurchaseOrder purchaseOrder, Company company);

}
