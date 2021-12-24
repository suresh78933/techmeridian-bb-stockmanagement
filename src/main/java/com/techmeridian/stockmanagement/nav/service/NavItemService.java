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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techmeridian.stockmanagement.company.Company;
import com.techmeridian.stockmanagement.company.CompanyRepository;
import com.techmeridian.stockmanagement.item.Item;
import com.techmeridian.stockmanagement.item.ItemRepository;
import com.techmeridian.stockmanagement.soap.SoapParam;
import com.techmeridian.stockmanagement.soap.SoapRequestType;
import com.techmeridian.stockmanagement.soap.SoapUtils;
import com.techmeridian.stockmanagement.vendor.VendorList;
import com.techmeridian.stockmanagement.vendor.VendorListRepository;

@Service
public class NavItemService {

	private Logger logger = Logger.getLogger(NavItemService.class);

	@Autowired
	private NavProperties navProperties;

	@Autowired
	ItemRepository itemRepository;

	@Autowired
	VendorListRepository vendorListRepository;

	@Autowired
	private CompanyRepository companyRepository;

	public String getItemList() {
		logger.info("Fetching item list ... ");
		String[] methodResponse = new String[] { "OK" };

		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		HttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();

		credentialsProvider.setCredentials(AuthScope.ANY, new NTCredentials(navProperties.getNavUserName(),
				navProperties.getNavPassword(), navProperties.getNavWorkstation(), navProperties.getNavDomain()));

		companyRepository.findAll().forEach(company -> {
			logger.info("Fetching item list for company " + company.getName());
			try {
				String url = String.format(navProperties.getNavItemListSoapURL(),
						company.getName().replaceAll(" ", "%20"));

				List<String> readMultipleSIMainTags = Arrays.asList(new String[] {});
				List<SoapParam> readMultipleSIParams = new ArrayList<SoapParam>();

				// Create a StringEntity for the SOAP XML.
				String soapContent = SoapUtils.getSoapContent(readMultipleSIParams, readMultipleSIMainTags,
						navProperties.getNavItemListNamespace(), SoapRequestType.READ_MULTIPLE);
				logger.info("Request body: " + soapContent);

				StringEntity stringEntity = new StringEntity(soapContent, "UTF-8");
				stringEntity.setChunked(true);

				// Request parameters and other properties.
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(stringEntity);
				httpPost.addHeader(SoapUtils.ACCEPT, SoapUtils.ACCEPT_VALUE);
				httpPost.addHeader(SoapUtils.CONTENT_TYPE, SoapUtils.ACCEPT_VALUE);
				httpPost.addHeader(SoapUtils.SOAP_ACTION, navProperties.getNavItemListSoapAction());

				// Execute and get the response.
				HttpResponse response = httpClient.execute(httpPost);
				HttpEntity entity = response.getEntity();

				Set<Item> navItemList = new HashSet<Item>();
				Map<String, String> vendorMap = new HashMap<String, String>();

				String strResponse = null;
				if (entity != null) {
					strResponse = EntityUtils.toString(entity);
					logger.info("Response body: " + strResponse);

					if (!strResponse.contains("s:Fault")) {

						JSONObject soapDatainJsonObject = XML.toJSONObject(strResponse);

						ObjectMapper objectMapper = new ObjectMapper();
						JsonNode jsonNode = objectMapper.readTree(soapDatainJsonObject.toString());

						JsonNode jsonItem = jsonNode.path("Soap:Envelope").path("Soap:Body").path("ReadMultiple_Result")
								.path("ReadMultiple_Result").path("ItemList");
						if (!jsonItem.isMissingNode() && jsonItem.isArray()) {
							if (jsonItem.size() > 0) {
								jsonItem.spliterator().forEachRemaining(thisSILine -> {
									try {
										navItemList.add(getItem(company, vendorMap, thisSILine));
									} catch (Exception e) {
										e.printStackTrace();
										throw new RuntimeException(e);
									}
								});
							}
						} else if (!jsonItem.isMissingNode()) {
							navItemList.add(getItem(company, vendorMap, jsonItem));
						}
					}

					Map<String, Item> persistedItemList = new HashMap<String, Item>();
					itemRepository.findByCompany(company)
							.forEach(item -> persistedItemList.put(item.getItemNo(), item));

					Set<Item> itemsToPersist = new HashSet<Item>();
					for (Item navItem : navItemList) {
						if (persistedItemList.containsKey(navItem.getItemNo())) {
							navItem.setItemId(persistedItemList.get(navItem.getItemNo()).getItemId());
							logger.info(navItem.getItemNo() + " exists in db, will be updated.");
						} else {
							logger.info("Found new item " + navItem.getItemNo() + ", will be persisted.");
						}
						itemsToPersist.add(navItem);
					}
					itemRepository.save(itemsToPersist);
					logger.info("Fetching item list for company " + company.getName() + ", Success.");
				} else {
					logger.info("Http entity is null, Failed.");
				}
			} catch (Exception exception) {
				logger.error("Error fetching item list for company " + company.getName(), exception);
				methodResponse[0] = "Error";
			}
		});
		logger.info("Fetching item list, " + methodResponse[0]);
		return methodResponse[0];
	}

	private Item getItem(Company company, Map<String, String> vendorMap, JsonNode jsonItem) {
		Item item = new Item();

		item.setItemNo(jsonItem.path("No").asText());
		item.setItemDescription(jsonItem.path("Description").asText());
		item.setBaseUnitOfMeasure(jsonItem.path("Base_Unit_of_Measure").asText());
		item.setShelfNo(jsonItem.path("Shelf_No").asText());
		item.setStandardCost(jsonItem.path("Standard_Cost").asText());
		item.setUnitCost(jsonItem.path("Unit_Cost").asText());
		item.setBlocked(jsonItem.path("Blocked").asText());
		item.setPurchaseUnitOfMeasure(jsonItem.path("Purch_Unit_of_Measure").asText());
		item.setETag(jsonItem.path("ETag").asText());
		item.setProductionBomNumber(jsonItem.path("Production_BOM_No").asText());
		item.setRoutingNo(jsonItem.path("Routing_No").asText());
		item.setPartNo(jsonItem.path("Part_No").asText());
		item.setInventory(jsonItem.path("Inventory").asText());
		item.setCompany(company);

		String vendorNo = jsonItem.path("Vendor_No").asText();
		if (!StringUtils.isEmpty(vendorNo)) {
			if (vendorMap.containsKey(vendorNo)) {
				item.setVendor(vendorMap.get(vendorNo));
			} else {
				VendorList vendorList = vendorListRepository.findByNumberAndCompany(vendorNo, company);
				if (vendorList != null) {
					item.setVendor(vendorList.getName());
					vendorMap.put(vendorNo, vendorList.getName());
				}
			}
		}
		return item;
	}

	public void reloadItem(Item item) throws Exception {
		try {
			Company company = item.getCompany();
			logger.info("Refreshing item:" + item.getItemNo() + ", company :" + company.getName());

			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			HttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider)
					.build();

			credentialsProvider.setCredentials(AuthScope.ANY, new NTCredentials(navProperties.getNavUserName(),
					navProperties.getNavPassword(), navProperties.getNavWorkstation(), navProperties.getNavDomain()));

			String url = String.format(navProperties.getNavItemListSoapURL(), company.getName().replaceAll(" ", "%20"));

			List<String> readMultipleSIMainTags = Arrays.asList(new String[] { "filter" });
			List<SoapParam> readMultipleSIParams = new ArrayList<SoapParam>();

			readMultipleSIParams.add(SoapUtils.getSoapParam("Field", "No", "string"));
			readMultipleSIParams.add(SoapUtils.getSoapParam("Criteria", item.getItemNo(), "string"));

			// Create a StringEntity for the SOAP XML.
			String soapContent = SoapUtils.getSoapContent(readMultipleSIParams, readMultipleSIMainTags,
					navProperties.getNavItemListNamespace(), SoapRequestType.READ_MULTIPLE);
			logger.info("Request body: " + soapContent);

			StringEntity stringEntity = new StringEntity(soapContent, "UTF-8");
			stringEntity.setChunked(true);

			// Request parameters and other properties.
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(stringEntity);
			httpPost.addHeader(SoapUtils.ACCEPT, SoapUtils.ACCEPT_VALUE);
			httpPost.addHeader(SoapUtils.CONTENT_TYPE, SoapUtils.ACCEPT_VALUE);
			httpPost.addHeader(SoapUtils.SOAP_ACTION, navProperties.getNavItemListSoapAction());

			// Execute and get the response.
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();

			Item navItem = null;
			Map<String, String> vendorMap = new HashMap<String, String>();

			String strResponse = null;
			if (entity != null) {
				strResponse = EntityUtils.toString(entity);
				logger.info("Response body: " + strResponse);

				if (!strResponse.contains("s:Fault")) {

					JSONObject soapDatainJsonObject = XML.toJSONObject(strResponse);

					ObjectMapper objectMapper = new ObjectMapper();
					JsonNode jsonNode = objectMapper.readTree(soapDatainJsonObject.toString());

					JsonNode jsonItem = jsonNode.path("Soap:Envelope").path("Soap:Body").path("ReadMultiple_Result")
							.path("ReadMultiple_Result").path("ItemList");
					if (!jsonItem.isMissingNode()) {
						navItem = getItem(company, vendorMap, jsonItem);
					}
				}

				Item dbItem = itemRepository.findByItemNoAndCompany(item.getItemNo(), company);
				if (dbItem != null) {
					navItem.setItemId(dbItem.getItemId());
					itemRepository.save(navItem);
					logger.info("Fetching item list for company " + company.getName() + ", Success.");
				} else {
					logger.error("Not able to find item " + item.getItemNo());
				}
			} else {
				logger.info("Http entity is null, Failed.");
			}

		} catch (Exception exception) {
			logger.error("Error refreshing stock issue lines ", exception);
			throw exception;
		}
	}

}
