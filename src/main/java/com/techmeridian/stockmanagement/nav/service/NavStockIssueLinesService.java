package com.techmeridian.stockmanagement.nav.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import com.techmeridian.stockmanagement.soap.SoapParam;
import com.techmeridian.stockmanagement.soap.SoapRequestType;
import com.techmeridian.stockmanagement.soap.SoapUtils;
import com.techmeridian.stockmanagement.stockissue.StockIssue;
import com.techmeridian.stockmanagement.stockissue.StockIssueLines;
import com.techmeridian.stockmanagement.stockissue.StockIssueLinesRepository;
import com.techmeridian.stockmanagement.stockissue.StockIssueRepository;
import com.techmeridian.stockmanagement.utils.Utility;

@Service
public class NavStockIssueLinesService {

	private Logger logger = Logger.getLogger(NavStockIssueLinesService.class);

	@Autowired
	private NavProperties navProperties;

	@Autowired
	private StockIssueLinesRepository stockIssueLinesRepository;

	@Autowired
	private StockIssueRepository stockIssueRepository;

	@Autowired
	private CompanyRepository companyRepository;

	public String getStockIssueLines() {
		logger.info("Fetching stock issues lines list from nav services ... ");

		String[] methodResponse = new String[] { "OK" };

		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		HttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();

		credentialsProvider.setCredentials(AuthScope.ANY, new NTCredentials(navProperties.getNavUserName(),
				navProperties.getNavPassword(), navProperties.getNavWorkstation(), navProperties.getNavDomain()));

		companyRepository.findAll().forEach(company -> {
			logger.info("Fetching stock issues lines for company " + company.getName());

			String url = String.format(navProperties.getNavStockIssueLinesReadMultipleSoapURL(),
					company.getName().replaceAll(" ", "%20"));

			stockIssueRepository.findByCompany(company).stream().forEach(stockIssue -> {
				try {
					List<String> readMultipleSIMainTags = Arrays.asList(new String[] { "filter" });
					List<SoapParam> readMultipleSIParams = new ArrayList<SoapParam>();

					readMultipleSIParams.add(SoapUtils.getSoapParam("Field", "Issue_Document_No", "string"));
					readMultipleSIParams.add(SoapUtils.getSoapParam("Criteria", stockIssue.getIssueNo(), "string"));

					// Create a StringEntity for the SOAP XML.
					String soapContent = SoapUtils.getSoapContent(readMultipleSIParams, readMultipleSIMainTags,
							navProperties.getNavStockIssueLinesReadMultipleNamespace(), SoapRequestType.READ_MULTIPLE);
					logger.info("Request body: " + soapContent);

					StringEntity stringEntity = new StringEntity(soapContent, "UTF-8");
					stringEntity.setChunked(true);

					// Request parameters and other properties.
					HttpPost httpPost = new HttpPost(url);
					httpPost.setEntity(stringEntity);
					httpPost.addHeader(SoapUtils.ACCEPT, SoapUtils.ACCEPT_VALUE);
					httpPost.addHeader(SoapUtils.CONTENT_TYPE, SoapUtils.ACCEPT_VALUE);
					httpPost.addHeader(SoapUtils.SOAP_ACTION,
							navProperties.getNavStockIssueLinesReadMultipleSoapAction());

					// Execute and get the response.
					HttpResponse response = httpClient.execute(httpPost);
					HttpEntity entity = response.getEntity();

					Map<String, Item> itemMap = new HashMap<String, Item>();

					String strResponse = null;
					Set<StockIssueLines> navSILinesList = new HashSet<StockIssueLines>();
					if (entity != null) {
						strResponse = EntityUtils.toString(entity);
						logger.info("Response body: " + strResponse);

						if (!strResponse.contains("s:Fault")) {

							JSONObject soapDatainJsonObject = XML.toJSONObject(strResponse);

							ObjectMapper objectMapper = new ObjectMapper();
							JsonNode jsonNode = objectMapper.readTree(soapDatainJsonObject.toString());

							JsonNode soLine = jsonNode.path("Soap:Envelope").path("Soap:Body")
									.path("ReadMultiple_Result").path("ReadMultiple_Result").path("IssSub");
							if (!soLine.isMissingNode() && soLine.isArray()) {
								if (soLine.size() > 0) {
									soLine.spliterator().forEachRemaining(thisSILine -> {
										try {
											navSILinesList
													.add(getStockIssueLine(company, stockIssue, itemMap, thisSILine));
										} catch (Exception e) {
											e.printStackTrace();
											throw new RuntimeException(e);
										}
									});
								}
							} else if (!soLine.isMissingNode()) {
								navSILinesList.add(getStockIssueLine(company, stockIssue, itemMap, soLine));
							}
						}

						Map<String, StockIssueLines> persistedStockIssueLines = new HashMap<String, StockIssueLines>();
						stockIssueLinesRepository.findByCompany(company)
								.forEach(siLine -> persistedStockIssueLines.put(getSIKey(siLine), siLine));

						List<StockIssueLines> stockIssueLinesToPersist = new ArrayList<StockIssueLines>();
						navSILinesList.stream().forEach(siLine -> {
							if (persistedStockIssueLines.containsKey(getSIKey(siLine))) {
								StockIssueLines persistedStockIssueLine = persistedStockIssueLines
										.get(getSIKey(siLine));
								siLine.setStockIssueLineId(persistedStockIssueLine.getStockIssueLineId());
								siLine.setMobileNavQuantity(persistedStockIssueLine.getMobileNavQuantity());
								siLine.setPushToNav(persistedStockIssueLine.getPushToNav());

								if (!Utility.getIntFromString(siLine.getQuantityIssued()).equals(
										Utility.getIntFromString(persistedStockIssueLine.getQuantityIssued()))) {
									logger.warn(
											"Quantity issues differ between nav and mobile nav, retaining mobile nav quantity, nav: "
													+ Utility.getIntFromString(siLine.getQuantityIssued())
													+ ", MobileNav: " + Utility.getIntFromString(
															persistedStockIssueLine.getQuantityIssued()));
									siLine.setQuantityIssued(persistedStockIssueLine.getQuantityIssued());
								}

								if (!Utility.getIntFromString(siLine.getQuantityToIssue()).equals(
										Utility.getIntFromString(persistedStockIssueLine.getQuantityToIssue()))) {
									logger.warn(
											"Quantity to issue differ between nav and mobile nav, retaining mobile nav quantity, nav: "
													+ Utility.getIntFromString(siLine.getQuantityToIssue())
													+ ", MobileNav: " + Utility.getIntFromString(
															persistedStockIssueLine.getQuantityToIssue()));
									siLine.setQuantityToIssue(persistedStockIssueLine.getQuantityToIssue());
								}

								logger.info(getSIKey(siLine) + " exists in db, will be updated.");
							} else {
								logger.info("Found stock issue line " + getSIKey(siLine) + ", will be persisted.");
							}
							stockIssueLinesToPersist.add(siLine);
						});

						stockIssueLinesRepository.save(stockIssueLinesToPersist);
						logger.info("Fetching stock issue lines list from nav services, Success.");
					} else {
						logger.info("Http entity is null, Failed.");
					}

				} catch (Exception exception) {
					exception.printStackTrace();
					logger.error("Error fetching stock issue lines list", exception);
					methodResponse[0] = "Error";
				}
			});
		});
		return methodResponse[0];
	}

	public StockIssueLines getStockIssueLine(Company company, StockIssue stockIssue, Map<String, Item> itemMap,
			JsonNode poLine) {

		StockIssueLines stockIssueLines = new StockIssueLines();
		stockIssueLines.setIssueDocumentNo(poLine.path("Issue_Document_No").asText());
		stockIssueLines.setLineNo(poLine.path("Line_No").asInt());
		stockIssueLines.setType(poLine.path("Type").asText());
		stockIssueLines.setNo(poLine.path("No").asText());
		stockIssueLines.setPartNo(poLine.path("Part_No").asText());
		stockIssueLines.setShelfNo(poLine.path("Shelf_No").asText());
		stockIssueLines.setDescription(poLine.path("Description").asText());
		stockIssueLines.setHmrKm(poLine.path("HMR_KM").asText());
		stockIssueLines.setUnitOfMeasure(poLine.path("Unit_of_Measure").asText());
		stockIssueLines.setLocation(poLine.path("Location").asText());

		stockIssueLines.setQuantity(poLine.path("Quantity").asInt());

		stockIssueLines.setShortcutDimension1Code(poLine.path("Shortcut_Dimension_1_Code").asText());
		stockIssueLines.setShortcutDimension2Code(poLine.path("Shortcut_Dimension_2_Code").asText());
		stockIssueLines.setAvailableInventory(poLine.path("Available_inventory").asText());

		stockIssueLines.setQuantityIssued(poLine.path("Quantity_Issued").asText());
		stockIssueLines.setOutstandingQuantity(poLine.path("Outstanding_Quantity").asText());

		stockIssueLines.setMobileNavQuantity(poLine.path("Quantity_to_Issue").asInt());
		stockIssueLines.setQuantityToIssue(getQuantityToIssue(stockIssueLines.getQuantity(),
				poLine.path("Quantity_Issued").asInt(), stockIssueLines.getMobileNavQuantity()));

		stockIssueLines.setUnitAmount(poLine.path("Unit_Amount").asText());
		stockIssueLines.setAmount(poLine.path("Amount").asText());
		stockIssueLines.setQtyPerUnitOfMeasure(poLine.path("Qty_per_Unit_of_Measure").asText());
		stockIssueLines.setTotalInventoryOnIssue(poLine.path("Total_Inventory_on_Issue").asText());
		stockIssueLines.setChangeKey(poLine.path("Key").asText());

		stockIssueLines.setStockIssue(stockIssue);
		stockIssueLines.setCompany(company);
		return stockIssueLines;

	}

	private String getQuantityToIssue(int quantity, int quantityIssued, int quantityToIssue) {
		return String.valueOf(quantity - (quantityToIssue + quantityIssued));
	}

	private String getSIKey(StockIssueLines stockIssueLines) {
		return stockIssueLines.getIssueDocumentNo() + "-" + stockIssueLines.getLineNo() + "-"
				+ stockIssueLines.getCompany().getId();
	}

	public String getStockIssueLineKeys() {
		logger.info("Fetching stock issues line keys from nav services ... ");
		boolean[] allGood = { true };
		try {
			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			HttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider)
					.build();

			credentialsProvider.setCredentials(AuthScope.ANY, new NTCredentials(navProperties.getNavUserName(),
					navProperties.getNavPassword(), navProperties.getNavWorkstation(), navProperties.getNavDomain()));

			companyRepository.findAll().forEach(company -> {
				logger.info("Fetching po lines keys for company " + company.getName());

				String url = String.format(navProperties.getNavStockIssueLinesReadSoapURL(),
						company.getName().replaceAll(" ", "%20"));

				List<SoapParam> readStockIssueLinesParams = new ArrayList<SoapParam>();

				stockIssueLinesRepository.findByCompany(company).forEach(siLine -> {
					logger.info("Reading siLine " + siLine);
					try {
						readStockIssueLinesParams.clear();

						readStockIssueLinesParams.add(
								SoapUtils.getSoapParam("Issue_Document_No", siLine.getIssueDocumentNo(), "string"));
						readStockIssueLinesParams.add(SoapUtils.getSoapParam("Line_No", siLine.getLineNo(), "int"));

						// Create a StringEntity for the SOAP XML.
						String soapContent = SoapUtils.getSoapContent(readStockIssueLinesParams,
								Collections.emptyList(), navProperties.getNavStockIssueLinesReadNamespace(),
								SoapRequestType.READ);
						logger.info("Request body: " + soapContent);

						StringEntity stringEntity = new StringEntity(soapContent, "UTF-8");
						stringEntity.setChunked(true);

						// Request parameters and other properties.
						HttpPost httpPost = new HttpPost(url);
						httpPost.setEntity(stringEntity);
						httpPost.addHeader(SoapUtils.ACCEPT, SoapUtils.ACCEPT_VALUE);
						httpPost.addHeader(SoapUtils.CONTENT_TYPE, SoapUtils.ACCEPT_VALUE);
						httpPost.addHeader(SoapUtils.SOAP_ACTION,
								navProperties.getNavStockIssueLinesReadMultipleSoapAction());

						// Execute and get the response.
						HttpResponse response = httpClient.execute(httpPost);
						HttpEntity entity = response.getEntity();

						String strResponse = null;
						if (entity != null) {
							strResponse = EntityUtils.toString(entity);
							logger.info("Response body: " + strResponse);

							JSONObject soapDatainJsonObject = XML.toJSONObject(strResponse);

							ObjectMapper objectMapper = new ObjectMapper();
							JsonNode jsonNode = objectMapper.readTree(soapDatainJsonObject.toString());

							JsonNode itemNode = jsonNode.path("Soap:Envelope").path("Soap:Body").path("Read_Result");

							if (itemNode != null && !"".equals(itemNode)) {
								// Set Key
								siLine.setChangeKey(
										Utility.escapeCharaters(itemNode.path("IssSub").path("Key").asText()));

								stockIssueLinesRepository.save(siLine);
							} else {
								logger.info("No record in nav for siLine " + siLine);
							}
						} else {
							logger.info("Null response for fetch key siLine " + siLine);
						}

					} catch (Exception exception) {
						logger.error("Error reading siLine keys from nav " + siLine, exception);
						allGood[0] = false;
					}
				});
			});

			if (allGood[0]) {
				return "OK";
			} else {
				return "Error";
			}
		} catch (Exception exception) {
			logger.error("Error fetching siLine keys ", exception);
			throw exception;
		}
	}

	public String pushToNav(StockIssue stockIssue) {
		try {
			boolean[] allGood = { true };
			String[] errorMessage = { "OK" };

			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			HttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider)
					.build();

			credentialsProvider.setCredentials(AuthScope.ANY, new NTCredentials(navProperties.getNavUserName(),
					navProperties.getNavPassword(), navProperties.getNavWorkstation(), navProperties.getNavDomain()));

			List<String> siLinesUpdateMainTags = Arrays.asList(new String[] { "IssSub" });
			List<SoapParam> siLinesUpdateParams = new ArrayList<SoapParam>();

			List<StockIssueLines> stockIssueLines = stockIssueLinesRepository.findByStockIssue(stockIssue);

			stockIssueLines.stream().filter(siLine -> (siLine.getPushToNav() != null && siLine.getPushToNav()))
					.forEach(siLine -> {
						try {
							siLinesUpdateParams.clear();
							siLinesUpdateParams.add(
									SoapUtils.getSoapParam("Issue_Document_No", siLine.getIssueDocumentNo(), "string"));
							siLinesUpdateParams.add(SoapUtils.getSoapParam("Line_No", siLine.getLineNo(), "string"));
							siLinesUpdateParams.add(SoapUtils.getSoapParam("Type", siLine.getType(), "int"));
							siLinesUpdateParams.add(SoapUtils.getSoapParam("No", siLine.getNo(), "string"));

							siLinesUpdateParams.add(
									SoapUtils.getSoapParam("Quantity_to_Issue", siLine.getMobileNavQuantity(), "int"));
							siLinesUpdateParams.add(SoapUtils.getSoapParam("Key",
									Utility.unEscapeCharaters(siLine.getChangeKey()), "string"));

							String soapContent = SoapUtils.getSoapContent(siLinesUpdateParams, siLinesUpdateMainTags,
									navProperties.getNavStockIssueLinesUpdateNamespace(), SoapRequestType.UPDATE);
							logger.info("Request body: " + soapContent);

							StringEntity stringEntity = new StringEntity(soapContent, "UTF-8");
							stringEntity.setChunked(true);

							String url = String.format(navProperties.getNavStockIssueLinesUpdateSoapURL(),
									siLine.getCompany().getName().replaceAll(" ", "%20"));

							// Request parameters and other properties.
							HttpPost httpPost = new HttpPost(url);
							httpPost.setEntity(stringEntity);
							httpPost.addHeader(SoapUtils.ACCEPT, SoapUtils.ACCEPT_VALUE);
							httpPost.addHeader(SoapUtils.CONTENT_TYPE, SoapUtils.ACCEPT_VALUE);
							httpPost.addHeader(SoapUtils.SOAP_ACTION,
									navProperties.getNavStockIssueLinesUpdateSoapAction());

							// Execute and get the response.
							HttpResponse response = httpClient.execute(httpPost);
							HttpEntity entity = response.getEntity();

							String strResponse = "";
							if (entity != null) {
								strResponse = EntityUtils.toString(entity);
								logger.info("Response body: " + strResponse);

								if (!strResponse.contains("s:Fault")) {
									JSONObject soapDatainJsonObject = XML.toJSONObject(strResponse);

									ObjectMapper objectMapper = new ObjectMapper();
									JsonNode jsonNode = objectMapper.readTree(soapDatainJsonObject.toString());

									JsonNode itemList = jsonNode.path("Soap:Envelope").path("Soap:Body")
											.path("Update_Result");

									if (itemList != null) {

										int stockToBeIssued = Utility.getInt(siLine.getQuantity())
												- (Utility.getInt(siLine.getMobileNavQuantity())
														+ Utility.getIntFromString(siLine.getQuantityIssued()));

										siLine.setQuantityToIssue(stockToBeIssued + "");
										siLine.setPushToNav(false);

										// Set Key
										siLine.setChangeKey(Utility
												.escapeCharaters(itemList.path("IssSub").path("Key").asText()));
										logger.info("Persisting siLine: " + siLine);
										stockIssueLinesRepository.save(siLine);
									}
								} else {
									logger.error("Fault code for SIL push, request: " + soapContent + ", response: "
											+ strResponse);
									allGood[0] = false;
								}
							}
						} catch (Exception exception) {
							logger.error("Error pushing siLine to nav ", exception);
							allGood[0] = false;
							if (exception != null && !StringUtils.isEmpty(exception.getMessage())) {
								errorMessage[0] = exception.getMessage();
							}
						}
					});

			if (allGood[0]) {
				return "OK";
			} else {
				if (StringUtils.isEmpty(errorMessage)) {
					return "Error";
				} else {
					return errorMessage[0];
				}
			}
		} catch (Exception exception) {
			logger.error("Error pushing PO to Nav ", exception);
			throw exception;
		}
	}

	public void reloadStockIssueLines(StockIssue stockIssue) throws Exception {
		try {
			Company company = stockIssue.getCompany();
			logger.info("Refreshing stock issue lines for stock issue:" + stockIssue.getIssueNo() + ", company :"
					+ company.getName());

			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			HttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider)
					.build();

			credentialsProvider.setCredentials(AuthScope.ANY, new NTCredentials(navProperties.getNavUserName(),
					navProperties.getNavPassword(), navProperties.getNavWorkstation(), navProperties.getNavDomain()));

			String url = String.format(navProperties.getNavStockIssueLinesReadMultipleSoapURL(),
					company.getName().replaceAll(" ", "%20"));

			List<String> readMultipleSIMainTags = Arrays.asList(new String[] { "filter" });
			List<SoapParam> readMultipleSIParams = new ArrayList<SoapParam>();

			readMultipleSIParams.add(SoapUtils.getSoapParam("Field", "Issue_Document_No", "string"));
			readMultipleSIParams.add(SoapUtils.getSoapParam("Criteria", stockIssue.getIssueNo(), "string"));

			// Create a StringEntity for the SOAP XML.
			String soapContent = SoapUtils.getSoapContent(readMultipleSIParams, readMultipleSIMainTags,
					navProperties.getNavStockIssueLinesReadMultipleNamespace(), SoapRequestType.READ_MULTIPLE);
			logger.info("Request body: " + soapContent);

			StringEntity stringEntity = new StringEntity(soapContent, "UTF-8");
			stringEntity.setChunked(true);

			// Request parameters and other properties.
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(stringEntity);
			httpPost.addHeader(SoapUtils.ACCEPT, SoapUtils.ACCEPT_VALUE);
			httpPost.addHeader(SoapUtils.CONTENT_TYPE, SoapUtils.ACCEPT_VALUE);
			httpPost.addHeader(SoapUtils.SOAP_ACTION, navProperties.getNavStockIssueLinesReadMultipleSoapAction());

			// Execute and get the response.
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();

			Map<String, Item> itemMap = new HashMap<String, Item>();

			String strResponse = null;
			Set<StockIssueLines> navSILinesList = new HashSet<StockIssueLines>();
			if (entity != null) {
				strResponse = EntityUtils.toString(entity);
				logger.info("Response body: " + strResponse);

				if (!strResponse.contains("s:Fault")) {

					JSONObject soapDatainJsonObject = XML.toJSONObject(strResponse);

					ObjectMapper objectMapper = new ObjectMapper();
					JsonNode jsonNode = objectMapper.readTree(soapDatainJsonObject.toString());

					JsonNode soLine = jsonNode.path("Soap:Envelope").path("Soap:Body").path("ReadMultiple_Result")
							.path("ReadMultiple_Result").path("IssSub");
					if (!soLine.isMissingNode() && soLine.isArray()) {
						if (soLine.size() > 0) {
							soLine.spliterator().forEachRemaining(thisSILine -> {
								try {
									navSILinesList.add(getStockIssueLine(company, stockIssue, itemMap, thisSILine));
								} catch (Exception e) {
									e.printStackTrace();
									throw new RuntimeException(e);
								}
							});
						}
					} else if (!soLine.isMissingNode()) {
						navSILinesList.add(getStockIssueLine(company, stockIssue, itemMap, soLine));
					}
				}
			}

			Map<String, StockIssueLines> persistedStockIssueLines = new HashMap<String, StockIssueLines>();
			stockIssueLinesRepository.findByStockIssueAndCompany(stockIssue, company)
					.forEach(siLine -> persistedStockIssueLines.put(getSIKey(siLine), siLine));

			List<StockIssueLines> stockIssueLinesToPersist = new ArrayList<StockIssueLines>();
			navSILinesList.stream().forEach(siLine -> {
				if (persistedStockIssueLines.containsKey(getSIKey(siLine))) {
					StockIssueLines persistedStockIssueLine = persistedStockIssueLines.get(getSIKey(siLine));
					siLine.setStockIssueLineId(persistedStockIssueLine.getStockIssueLineId());
					siLine.setPushToNav(persistedStockIssueLine.getPushToNav());

					logger.info(getSIKey(siLine) + " exists in db, will be updated.");
				} else {
					logger.info("Found stock issue line " + getSIKey(siLine) + ", will be persisted.");
				}
				stockIssueLinesToPersist.add(siLine);
			});

			stockIssueLinesRepository.save(stockIssueLinesToPersist);
			logger.info("Fetching stock issue lines list from nav services, Success.");

		} catch (Exception exception) {
			logger.error("Error refreshing stock issue lines ", exception);
			throw exception;
		}
	}

	public boolean checkQuatitiesAfterPushtoNav_NotRequired(String company, String documentNumber, String lineNo,
			int quantityToReceive, int quantityReceived) throws Exception {
		boolean foundLineItem = false;
		try {
			logger.info("checkQuatitiesAfterPushtoNav:" + documentNumber + ", company :" + company);

			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			HttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider)
					.build();

			credentialsProvider.setCredentials(AuthScope.ANY, new NTCredentials(navProperties.getNavUserName(),
					navProperties.getNavPassword(), navProperties.getNavWorkstation(), navProperties.getNavDomain()));

			String url = String.format(navProperties.getNavStockIssueLinesReadMultipleSoapURL(),
					company.replaceAll(" ", "%20"));

			List<String> readMultipleSIMainTags = Arrays.asList(new String[] { "filter" });
			List<SoapParam> readMultipleSIParams = new ArrayList<SoapParam>();

			readMultipleSIParams.add(SoapUtils.getSoapParam("Field", "Issue_Document_No", "string"));
			readMultipleSIParams.add(SoapUtils.getSoapParam("Criteria", documentNumber, "string"));

			// Create a StringEntity for the SOAP XML.
			String soapContent = SoapUtils.getSoapContent(readMultipleSIParams, readMultipleSIMainTags,
					navProperties.getNavStockIssueLinesReadMultipleNamespace(), SoapRequestType.READ_MULTIPLE);
			logger.info("Request body: " + soapContent);

			StringEntity stringEntity = new StringEntity(soapContent, "UTF-8");
			stringEntity.setChunked(true);

			// Request parameters and other properties.
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(stringEntity);
			httpPost.addHeader(SoapUtils.ACCEPT, SoapUtils.ACCEPT_VALUE);
			httpPost.addHeader(SoapUtils.CONTENT_TYPE, SoapUtils.ACCEPT_VALUE);
			httpPost.addHeader(SoapUtils.SOAP_ACTION, navProperties.getNavStockIssueLinesReadMultipleSoapAction());

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

					JsonNode soLine = jsonNode.path("Soap:Envelope").path("Soap:Body").path("ReadMultiple_Result")
							.path("ReadMultiple_Result").path("IssSub");
					if (!soLine.isMissingNode() && soLine.isArray()) {
						for (JsonNode jsonNode2 : soLine) {
							String lineNoToCheck = jsonNode2.path("Line_No").asText();
							if (lineNoToCheck.equals(lineNo)) {
								int quantityToReceiveToCheck = jsonNode2.path("Qty_to_Receive").asInt();
								int quantityReceivedToCheck = jsonNode2.path("Quantity_Received").asInt();
								if (quantityReceived != quantityReceivedToCheck
										|| quantityToReceive != quantityToReceiveToCheck) {

									logger.error("Quantities does not match after push to nav SIL, DocNo: "
											+ documentNumber + ", lineNo: " + lineNo
											+ ", quantityReceived[MobileNav Side]: " + quantityReceived
											+ ", quantityReceived[Nav Side]: " + quantityReceivedToCheck
											+ ", quantityToReceive[MobileNav Side]: " + quantityToReceive
											+ ", quantityToReceive[Nav Side]: " + quantityToReceiveToCheck);
									throw new RuntimeException(
											"Quantities does not match after push to nav SIL, DocNo: " + documentNumber
													+ ", lineNo: " + lineNo + ", quantityReceived[MobileNav Side]: "
													+ quantityReceived + ", quantityReceived[Nav Side]: "
													+ quantityReceivedToCheck + ", quantityToReceive[MobileNav Side]: "
													+ quantityToReceive + ", quantityToReceive[Nav Side]: "
													+ quantityToReceiveToCheck);
								} else {
									foundLineItem = true;
								}
							}
						}
					} else if (!soLine.isMissingNode()) {
						String lineNoToCheck = soLine.path("Line_No").asText();
						if (lineNoToCheck.equals(lineNo)) {
							int quantityToReceiveToCheck = soLine.path("Qty_to_Receive").asInt();
							int quantityReceivedToCheck = soLine.path("Quantity_Received").asInt();
							if (quantityReceived != quantityReceivedToCheck
									|| quantityToReceive != quantityToReceiveToCheck) {

								logger.error("Quantities does not match after push to nav SIL, DocNo: " + documentNumber
										+ ", lineNo: " + lineNo + ", quantityReceived[MobileNav Side]: "
										+ quantityReceived + ", quantityReceived[Nav Side]: " + quantityReceivedToCheck
										+ ", quantityToReceive[MobileNav Side]: " + quantityToReceive
										+ ", quantityToReceive[Nav Side]: " + quantityToReceiveToCheck);
								throw new RuntimeException("Quantities does not match after push to nav SIL, DocNo: "
										+ documentNumber + ", lineNo: " + lineNo
										+ ", quantityReceived[MobileNav Side]: " + quantityReceived
										+ ", quantityReceived[Nav Side]: " + quantityReceivedToCheck
										+ ", quantityToReceive[MobileNav Side]: " + quantityToReceive
										+ ", quantityToReceive[Nav Side]: " + quantityToReceiveToCheck);
							} else {
								foundLineItem = true;
							}
						}
					}
				}
			}
		} catch (Exception exception) {
			logger.error("Error checking quantities ", exception);
			throw exception;
		}
		return foundLineItem;
	}
}
