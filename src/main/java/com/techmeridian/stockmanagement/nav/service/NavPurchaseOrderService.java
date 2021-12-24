package com.techmeridian.stockmanagement.nav.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.json.XML;
import org.ksoap2.serialization.SoapObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techmeridian.stockmanagement.company.CompanyRepository;
import com.techmeridian.stockmanagement.purchaseorder.PurchaseOrder;
import com.techmeridian.stockmanagement.purchaseorder.PurchaseOrderRepository;
import com.techmeridian.stockmanagement.soap.SoapParam;
import com.techmeridian.stockmanagement.soap.SoapRequestType;
import com.techmeridian.stockmanagement.soap.SoapUtils;

@Service
public class NavPurchaseOrderService {
	private Logger logger = Logger.getLogger(NavPurchaseOrderService.class);

	@Autowired
	private NavProperties navProperties;

	@Autowired
	PurchaseOrderRepository purchaseOrderRepository;

	@Autowired
	private CompanyRepository companyRepository;

	public String getPurchaseOrderList_Old() {
		logger.info("Fetching purchase order list from nav services ... ");
		String[] methodResponse = new String[] { "OK" };

		companyRepository.findAll().forEach(company -> {
			logger.info("Fetching journal list for company " + company.getName());
			try {
				String url = String.format(navProperties.getNavPurchaseOrderListSoapURL(),
						company.getName().replaceAll(" ", "%20"));
				SoapObject response = NavServiceUtil.getInstance().getDataFromNav(
						navProperties.getNavPurchaseOrderListNamespace(), url,
						navProperties.getNavPurchaseOrderListSoapAction(),
						navProperties.getNavPurchaseOrderListMethodName(), navProperties.getNavUserName(),
						navProperties.getNavPassword(), navProperties.getNavDomain(),
						navProperties.getNavWorkstation());

				Set<PurchaseOrder> navPurchaseOrderList = new HashSet<PurchaseOrder>();
				for (int j = 0; j < response.getPropertyCount(); j++) {
					SoapObject soapObject = (SoapObject) response.getProperty(j);

					PurchaseOrder purchaseOrder = new PurchaseOrder();
					purchaseOrder.setNumber(ServiceUtil.getValue("No", soapObject));
					purchaseOrder.setBuyFromVendorNo(ServiceUtil.getValue("Buy_from_Vendor_No", soapObject));
					purchaseOrder.setBuyFromVendorName(ServiceUtil.getValue("Buy_from_Vendor_Name", soapObject));
					purchaseOrder.setBuyFromCity(ServiceUtil.getValue("Buy_from_City", soapObject));
					purchaseOrder.setBuyFromAddress(ServiceUtil.getValue("Buy_from_Address", soapObject));
					purchaseOrder.setBuyFromAddress2(ServiceUtil.getValue("Buy_from_Address_2", soapObject));
					purchaseOrder.setPayToVendorNo(ServiceUtil.getValue("Pay_to_Vendor_No", soapObject));
					purchaseOrder.setPayToName(ServiceUtil.getValue("Pay_to_Name", soapObject));
					purchaseOrder.setPayToCity(ServiceUtil.getValue("Pay_to_City", soapObject));
					purchaseOrder.setPayToAddress(ServiceUtil.getValue("Pay_to_Address", soapObject));
					purchaseOrder.setPayToAddress2(ServiceUtil.getValue("Pay_to_Address_2", soapObject));
					purchaseOrder.setOrderDate(ServiceUtil.getValue("Order_Date", soapObject));
					purchaseOrder.setDocumentDate(ServiceUtil.getValue("Document_Date", soapObject));
					purchaseOrder.setExpectedReceiptDate(ServiceUtil.getValue("Expected_Receipt_Date", soapObject));
					purchaseOrder.setCurrencyCode(ServiceUtil.getValue("Currency_Code", soapObject));
					purchaseOrder.setPurchaserCode(ServiceUtil.getValue("Purchaser_Code", soapObject));
					purchaseOrder.setStatus(ServiceUtil.getValue("Status", soapObject));
					purchaseOrder.setShipToName(ServiceUtil.getValue("Ship_to_Name", soapObject));
					purchaseOrder.setShipToCity(ServiceUtil.getValue("Ship_to_City", soapObject));
					purchaseOrder.setShipToAddress(ServiceUtil.getValue("Ship_to_Address", soapObject));
					purchaseOrder.setShipToAddress2(ServiceUtil.getValue("Ship_to_Address_2", soapObject));
					purchaseOrder.setVendorOrderNo(ServiceUtil.getValue("Vendor_Order_No", soapObject));
					purchaseOrder.setVendorShipmentNo(ServiceUtil.getValue("Vendor_Shipment_No", soapObject));
					purchaseOrder.setVendorInvoiceNo(ServiceUtil.getValue("Vendor_Invoice_No", soapObject));
					purchaseOrder.setCompany(company);

					navPurchaseOrderList.add(purchaseOrder);
				}

				Map<String, PurchaseOrder> persistedPurchaseOrderList = new HashMap<String, PurchaseOrder>();
				purchaseOrderRepository.findByCompany(company).stream().forEach(
						purchaseOrder -> persistedPurchaseOrderList.put(purchaseOrder.getNumber(), purchaseOrder));

				Set<PurchaseOrder> purchaseOrdersToPersist = new HashSet<PurchaseOrder>();
				for (PurchaseOrder navPurchaseOrder : navPurchaseOrderList) {
					if (persistedPurchaseOrderList.containsKey(navPurchaseOrder.getNumber())) {
						navPurchaseOrder.setPurchaseOrderId(
								persistedPurchaseOrderList.get(navPurchaseOrder.getNumber()).getPurchaseOrderId());
						logger.info(navPurchaseOrder.getNumber() + " exists in db, will be updated.");
					} else {
						logger.info("Found new po " + navPurchaseOrder.getNumber() + ", will be persisted.");
					}
					purchaseOrdersToPersist.add(navPurchaseOrder);
				}
				purchaseOrderRepository.save(purchaseOrdersToPersist);
				logger.info("Fetching po list from nav services for company " + company.getName() + ", Success.");

			} catch (Exception exception) {
				logger.error("Error fetching po list", exception);
				methodResponse[0] = "Error";
			}
		});
		return methodResponse[0];
	}

	public String getPurchaseOrderList() {
		logger.info("Fetching purchase order list from nav services ... ");
		String[] methodResponse = new String[] { "OK" };

		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		HttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();

		credentialsProvider.setCredentials(AuthScope.ANY, new NTCredentials(navProperties.getNavUserName(),
				navProperties.getNavPassword(), navProperties.getNavWorkstation(), navProperties.getNavDomain()));

		companyRepository.findAll().forEach(company -> {
			logger.info("Fetching journal list for company " + company.getName());
			try {
				String url = String.format(navProperties.getNavPurchaseOrderListSoapURL(),
						company.getName().replaceAll(" ", "%20"));

				List<String> readMultiplePOMainTags = Arrays.asList(new String[] {});
				List<SoapParam> readMultiplePOParams = new ArrayList<SoapParam>();

				// Create a StringEntity for the SOAP XML.
				String soapContent = SoapUtils.getSoapContent(readMultiplePOParams, readMultiplePOMainTags,
						navProperties.getNavPurchaseOrderListNamespace(), SoapRequestType.READ_MULTIPLE);
				logger.info("Request body: " + soapContent);

				StringEntity stringEntity = new StringEntity(soapContent, "UTF-8");
				stringEntity.setChunked(true);

				// Request parameters and other properties.
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(stringEntity);
				httpPost.addHeader(SoapUtils.ACCEPT, SoapUtils.ACCEPT_VALUE);
				httpPost.addHeader(SoapUtils.CONTENT_TYPE, SoapUtils.ACCEPT_VALUE);
				httpPost.addHeader(SoapUtils.SOAP_ACTION, navProperties.getNavPurchaseOrderListSoapAction());

				// Execute and get the response.
				HttpResponse response = httpClient.execute(httpPost);
				HttpEntity entity = response.getEntity();

				String strResponse = null;
				if (entity != null) {
					strResponse = EntityUtils.toString(entity);
					logger.info("Response body: " + strResponse);

					if (!strResponse.contains("s:Fault")) {

						JSONObject soapDatainJsonObject = XML.toJSONObject(strResponse);

						ObjectMapper objectMapper = new ObjectMapper();
						JsonNode jsonNode = objectMapper.readTree(soapDatainJsonObject.toString());

						JsonNode journalList = jsonNode.path("Soap:Envelope").path("Soap:Body")
								.path("ReadMultiple_Result").path("ReadMultiple_Result").path("PurchaseOrder");
						Set<PurchaseOrder> navPurchaseOrderList = new HashSet<PurchaseOrder>();
						if (journalList.size() > 0) {
							journalList.spliterator().forEachRemaining(action -> {
								PurchaseOrder purchaseOrder = new PurchaseOrder();
								purchaseOrder.setNumber(action.path("No").asText());
								purchaseOrder.setBuyFromVendorNo(action.path("Buy_from_Vendor_No").asText());
								purchaseOrder.setBuyFromVendorName(action.path("Buy_from_Vendor_Name").asText());
								purchaseOrder.setBuyFromCity(action.path("Buy_from_City").asText());
								purchaseOrder.setBuyFromAddress(action.path("Buy_from_Address").asText());
								purchaseOrder.setBuyFromAddress2(action.path("Buy_from_Address_2").asText());
								purchaseOrder.setPayToVendorNo(action.path("Pay_to_Vendor_No").asText());
								purchaseOrder.setPayToName(action.path("Pay_to_Name").asText());
								purchaseOrder.setPayToCity(action.path("Pay_to_City").asText());
								purchaseOrder.setPayToAddress(action.path("Pay_to_Address").asText());
								purchaseOrder.setPayToAddress2(action.path("Pay_to_Address_2").asText());
								purchaseOrder.setOrderDate(action.path("Order_Date").asText());
								purchaseOrder.setDocumentDate(action.path("Document_Date").asText());
								purchaseOrder.setExpectedReceiptDate(action.path("Expected_Receipt_Date").asText());
								purchaseOrder.setCurrencyCode(action.path("Currency_Code").asText());
								purchaseOrder.setPurchaserCode(action.path("Purchaser_Code").asText());
								purchaseOrder.setStatus(action.path("Status").asText());
								purchaseOrder.setShipToName(action.path("Ship_to_Name").asText());
								purchaseOrder.setShipToCity(action.path("Ship_to_City").asText());
								purchaseOrder.setShipToAddress(action.path("Ship_to_Address").asText());
								purchaseOrder.setShipToAddress2(action.path("Ship_to_Address_2").asText());
								purchaseOrder.setVendorOrderNo(action.path("Vendor_Order_No").asText());
								purchaseOrder.setVendorShipmentNo(action.path("Vendor_Shipment_No").asText());
								purchaseOrder.setVendorInvoiceNo(action.path("Vendor_Invoice_No").asText());
								purchaseOrder.setCompany(company);

								navPurchaseOrderList.add(purchaseOrder);
							});

							Map<String, PurchaseOrder> persistedPurchaseOrderList = new HashMap<String, PurchaseOrder>();
							purchaseOrderRepository.findByCompany(company).stream()
									.forEach(purchaseOrder -> persistedPurchaseOrderList.put(purchaseOrder.getNumber(),
											purchaseOrder));

							Set<PurchaseOrder> purchaseOrdersToPersist = new HashSet<PurchaseOrder>();
							for (PurchaseOrder navPurchaseOrder : navPurchaseOrderList) {
								if (persistedPurchaseOrderList.containsKey(navPurchaseOrder.getNumber())) {
									navPurchaseOrder.setPurchaseOrderId(persistedPurchaseOrderList
											.get(navPurchaseOrder.getNumber()).getPurchaseOrderId());
									logger.info(navPurchaseOrder.getNumber() + " exists in db, will be updated.");
								} else {
									logger.info(
											"Found new po " + navPurchaseOrder.getNumber() + ", will be persisted.");
								}
								purchaseOrdersToPersist.add(navPurchaseOrder);
							}
							purchaseOrderRepository.save(purchaseOrdersToPersist);
							logger.info("Fetching po list from nav services for company " + company.getName()
									+ ", Success.");
						}

					}
				}

			} catch (Exception exception) {
				logger.error("Error fetching po list", exception);
				methodResponse[0] = "Error";
			}
		});
		return methodResponse[0];
	}
}
