package com.techmeridian.stockmanagement.nav.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public final class NavProperties {

	@Value("${nav.username}")
	private String navUserName;

	@Value("${nav.password}")
	private String navPassword;

	@Value("${nav.domain}")
	private String navDomain;

	@Value("${nav.workstation}")
	private String navWorkstation;

	// Item list
	@Value("${nav.itemlist.namespace}")
	private String navItemListNamespace;

	@Value("${nav.itemlist.soapurl}")
	private String navItemListSoapURL;

	@Value("${nav.itemlist.soapaction}")
	private String navItemListSoapAction;

	@Value("${nav.itemlist.methodname}")
	private String navItemListMethodName;

	// Vendor list
	@Value("${nav.vendorlist.namespace}")
	private String navVendorListNamespace;

	@Value("${nav.vendorlist.soapurl}")
	private String navVendorListSoapURL;

	@Value("${nav.vendorlist.soapaction}")
	private String navVendorListSoapAction;

	@Value("${nav.vendorlist.methodname}")
	private String navVendorListMethodName;

	// Unit of measure
	@Value("${nav.unitofmeasure.namespace}")
	private String navUnitOfMeasureNamespace;

	@Value("${nav.unitofmeasure.soapurl}")
	private String navUnitOfMeasureSoapURL;

	@Value("${nav.unitofmeasure.soapaction}")
	private String navUnitOfMeasureSoapAction;

	@Value("${nav.unitofmeasure.methodname}")
	private String navUnitOfMeasureMethodName;

	// Journal Batches
	@Value("${nav.journallist.namespace}")
	private String navJournallistNamespace;

	@Value("${nav.journallist.soapurl}")
	private String navJournallistSoapurl;

	@Value("${nav.journallist.soapaction}")
	private String navJournallistSoapaction;

	@Value("${nav.journallist.methodname}")
	private String navJournallistMethodname;

	// Location List
	@Value("${nav.locationlist.namespace}")
	private String navLocationListNamespace;

	@Value("${nav.locationlist.soapurl}")
	private String navLocationListSoapURL;

	@Value("${nav.locationlist.soapaction}")
	private String navLocationListSoapAction;

	@Value("${nav.locationlist.methodname}")
	private String navLocationListMethodName;

	// Phy Inv Journal Read multiple
	@Value("${nav.phyinjournal-read-multiple.namespace}")
	private String navPhyInvJournalReadMultipleNamespace;

	@Value("${nav.phyinjournal-read-multiple.soapurl}")
	private String navPhyInvJournalReadMultipleSoapurl;

	@Value("${nav.phyinjournal-read-multiple.soapaction}")
	private String navPhyInvJournalReadMultipleSoapaction;

	@Value("${nav.phyinjournal-read-multiple.methodname}")
	private String navPhyInvJournalReadMultipleMethodname;

	// Phy Inv Journal Create
	@Value("${nav.phyinjournal-create.namespace}")
	private String navPhyInvJournalCreateNamespace;

	@Value("${nav.phyinjournal-create.soapurl}")
	private String navPhyInvJournalCreateSoapurl;

	@Value("${nav.phyinjournal-create.soapaction}")
	private String navPhyInvJournalCreateSoapaction;

	@Value("${nav.phyinjournal-create.methodname}")
	private String navPhyInvJournalCreateMethodname;

	// Phy Inv Journal Update
	@Value("${nav.phyinjournal-update.namespace}")
	private String navPhyInvJournalUpdateNamespace;

	@Value("${nav.phyinjournal-update.soapurl}")
	private String navPhyInvJournalUpdateSoapurl;

	@Value("${nav.phyinjournal-update.soapaction}")
	private String navPhyInvJournalUpdateSoapaction;

	@Value("${nav.phyinjournal-update.methodname}")
	private String navPhyInvJournalUpdateMethodname;

	// Phy Inv Journal Read
	@Value("${nav.phyinjournal-read.namespace}")
	private String navPhyInvJournalReadNamespace;

	@Value("${nav.phyinjournal-read.soapurl}")
	private String navPhyInvJournalReadSoapurl;

	@Value("${nav.phyinjournal-read.soapaction}")
	private String navPhyInvJournalReadSoapaction;

	@Value("${nav.phyinjournal-read.methodname}")
	private String navPhyInvJournalReadMethodname;

	// Purchase order list
	@Value("${nav.purchaseorderlist.namespace}")
	private String navPurchaseOrderListNamespace;

	@Value("${nav.purchaseorderlist.soapurl}")
	private String navPurchaseOrderListSoapURL;

	@Value("${nav.purchaseorderlist.soapaction}")
	private String navPurchaseOrderListSoapAction;

	@Value("${nav.purchaseorderlist.methodname}")
	private String navPurchaseOrderListMethodName;

	// Purchase order lines read multiple
	@Value("${nav.purchaseorderlines-read-multiple.namespace}")
	private String navPurchaseOrderLinesReadMultipleNamespace;

	@Value("${nav.purchaseorderlines-read-multiple.soapurl}")
	private String navPurchaseOrderLinesReadMultipleSoapURL;

	@Value("${nav.purchaseorderlines-read-multiple.soapaction}")
	private String navPurchaseOrderLinesReadMultipleSoapAction;

	@Value("${nav.purchaseorderlines-read-multiple.methodname}")
	private String navPurchaseOrderLinesReadMultipleMethodName;

	// Purchase order lines
	@Value("${nav.purchaseorderlines-read.namespace}")
	private String navPurchaseOrderLinesReadNamespace;

	@Value("${nav.purchaseorderlines-read.soapurl}")
	private String navPurchaseOrderLinesReadSoapURL;

	@Value("${nav.purchaseorderlines-read.soapaction}")
	private String navPurchaseOrderLinesReadSoapAction;

	@Value("${nav.purchaseorderlines-read.methodname}")
	private String navPurchaseOrderLinesReadMethodName;

	// Purchase order lines
	@Value("${nav.purchaseorderlines-update.namespace}")
	private String navPurchaseOrderLinesUpdateNamespace;

	@Value("${nav.purchaseorderlines-update.soapurl}")
	private String navPurchaseOrderLinesUpdateSoapURL;

	@Value("${nav.purchaseorderlines-update.soapaction}")
	private String navPurchaseOrderLinesUpdateSoapAction;

	@Value("${nav.purchaseorderlines-update.methodname}")
	private String navPurchaseOrderLinesUpdateMethodName;

	// Stock issue
	@Value("${nav.stockissuelist.namespace}")
	private String navStockIssueListNamespace;

	@Value("${nav.stockissuelist.soapurl}")
	private String navStockIssueListSoapURL;

	@Value("${nav.stockissuelist.soapaction}")
	private String navStockIssueListSoapAction;

	@Value("${nav.stockissuelist.methodname}")
	private String navStockIssueListMethodName;

	// stock issue lines read multiple
	@Value("${nav.stockissuelines-read-multiple.namespace}")
	private String navStockIssueLinesReadMultipleNamespace;

	@Value("${nav.stockissuelines-read-multiple.soapurl}")
	private String navStockIssueLinesReadMultipleSoapURL;

	@Value("${nav.stockissuelines-read-multiple.soapaction}")
	private String navStockIssueLinesReadMultipleSoapAction;

	@Value("${nav.stockissuelines-read-multiple.methodname}")
	private String navStockIssueLinesReadMultipleMethodName;

	// stock issue lines read
	@Value("${nav.stockissuelines-read.namespace}")
	private String navStockIssueLinesReadNamespace;

	@Value("${nav.stockissuelines-read.soapurl}")
	private String navStockIssueLinesReadSoapURL;

	@Value("${nav.stockissuelines-read.soapaction}")
	private String navStockIssueLinesReadSoapAction;

	@Value("${nav.stockissuelines-read.methodname}")
	private String navStockIssueLinesReadMethodName;

	// stock issue lines update
	@Value("${nav.stockissuelines-update.namespace}")
	private String navStockIssueLinesUpdateNamespace;

	@Value("${nav.stockissuelines-update.soapurl}")
	private String navStockIssueLinesUpdateSoapURL;

	@Value("${nav.stockissuelines-update.soapaction}")
	private String navStockIssueLinesUpdateSoapAction;

	@Value("${nav.stockissuelines-update.methodname}")
	private String navStockIssueLinesUpdateMethodName;

	// Company list
	@Value("${nav.company-list.namespace}")
	private String navCompanyListNamespace;

	@Value("${nav.company-list.soapurl}")
	private String navCompanyListSoapURL;

	@Value("${nav.company-list.soapaction}")
	private String navCompanyListSoapAction;

	@Value("${nav.company-list.methodname}")
	private String navCompanyListMethodName;

	// User list
	@Value("${nav.userlist.namespace}")
	private String navUserListNamespace;

	@Value("${nav.userlist.soapurl}")
	private String navUserListSoapURL;

	@Value("${nav.userlist.soapaction}")
	private String navUserListSoapAction;

	@Value("${nav.userlist.methodname}")
	private String navUserListMethodName;
}
