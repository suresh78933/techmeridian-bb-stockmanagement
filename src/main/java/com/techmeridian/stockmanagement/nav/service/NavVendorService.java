package com.techmeridian.stockmanagement.nav.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.ksoap2.serialization.SoapObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techmeridian.stockmanagement.company.CompanyRepository;
import com.techmeridian.stockmanagement.vendor.VendorList;
import com.techmeridian.stockmanagement.vendor.VendorListRepository;

@Service
public class NavVendorService {

	private Logger logger = Logger.getLogger(NavVendorService.class);

	@Autowired
	private NavProperties navProperties;

	@Autowired
	VendorListRepository vendorListRepository;

	@Autowired
	private CompanyRepository companyRepository;

	public String getVendorList() {
		logger.info("Fetching vendor list ... ");
		String[] methodResponse = new String[] { "OK" };

		companyRepository.findAll().forEach(company -> {
			logger.info("Fetching vendor list for company " + company.getName());
			try {
				String url = String.format(navProperties.getNavVendorListSoapURL(),
						company.getName().replaceAll(" ", "%20"));
				SoapObject response = NavServiceUtil.getInstance().getDataFromNav(
						navProperties.getNavVendorListNamespace(), url, navProperties.getNavVendorListSoapAction(),
						navProperties.getNavVendorListMethodName(), navProperties.getNavUserName(),
						navProperties.getNavPassword(), navProperties.getNavDomain(),
						navProperties.getNavWorkstation());

				Set<VendorList> navVendorList = new HashSet<VendorList>();
				for (int j = 0; j < response.getPropertyCount(); j++) {
					SoapObject soapObject = (SoapObject) response.getProperty(j);

					VendorList vendorList = new VendorList();
					vendorList.setNumber(ServiceUtil.getValue("No", soapObject));
					vendorList.setName(ServiceUtil.getValue("Name", soapObject));
					vendorList.setResponsibilityCenter(ServiceUtil.getValue("Responsibility_Center", soapObject));
					vendorList.setLocationCode(ServiceUtil.getValue("Location_Code", soapObject));
					vendorList.setPostCode(ServiceUtil.getValue("Post_Code", soapObject));
					vendorList.setCountryRegionCode(ServiceUtil.getValue("Country_Region_Code", soapObject));
					vendorList.setPhoneNo(ServiceUtil.getValue("Phone_No", soapObject));
					vendorList.setFaxNo(ServiceUtil.getValue("Fax_No", soapObject));
					vendorList.setContact(ServiceUtil.getValue("Contact", soapObject));
					vendorList.setPurchaserCode(ServiceUtil.getValue("Purchaser_Code", soapObject));
					vendorList.setVendorPostingGroup(ServiceUtil.getValue("Vendor_Posting_Group", soapObject));
					vendorList.setGenBusPostingGroup(ServiceUtil.getValue("Gen_Bus_Posting_Group", soapObject));
					vendorList.setVatBusPostingGroup(ServiceUtil.getValue("VAT_Bus_Posting_Group", soapObject));
					vendorList.setSearchName(ServiceUtil.getValue("Search_Name", soapObject));
					vendorList.setShipmentMethodCode(ServiceUtil.getValue("Shipment_Method_Code", soapObject));
					vendorList.setCurrencyCode(ServiceUtil.getValue("Currency_Code", soapObject));
					vendorList.setLanguageCode(ServiceUtil.getValue("Language_Code", soapObject));
					vendorList.setCompany(company);

					navVendorList.add(vendorList);
				}

				Map<String, VendorList> persistedVendorList = new HashMap<String, VendorList>();
				vendorListRepository.findByCompany(company).stream()
						.forEach(vendor -> persistedVendorList.put(vendor.getNumber(), vendor));

				Set<VendorList> vendorListToPersist = new HashSet<VendorList>();
				for (VendorList navVendor : navVendorList) {
					if (persistedVendorList.containsKey(navVendor.getNumber())) {
						navVendor.setVendorListId(persistedVendorList.get(navVendor.getNumber()).getVendorListId());
						logger.info(navVendor.getNumber() + " exists in db, will be updated.");
					} else {
						logger.info("Found new vendor " + navVendor.getNumber() + ", will be persisted.");
					}
					vendorListToPersist.add(navVendor);
				}
				vendorListRepository.save(vendorListToPersist);

				logger.info("Fetching vendor list for company " + company.getName() + ", Success.");
			} catch (Exception exception) {
				logger.error("Error fetching vendor list for company " + company.getName(), exception);
				methodResponse[0] = "Error";
			}
		});
		logger.info("Fetching vendor list, " + methodResponse[0]);
		return methodResponse[0];
	}
}
