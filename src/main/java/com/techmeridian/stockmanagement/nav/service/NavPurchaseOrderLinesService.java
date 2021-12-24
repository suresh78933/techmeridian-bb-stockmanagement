package com.techmeridian.stockmanagement.nav.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

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
import com.techmeridian.stockmanagement.purchaseorder.PurchaseOrder;
import com.techmeridian.stockmanagement.purchaseorder.PurchaseOrderLines;
import com.techmeridian.stockmanagement.purchaseorder.PurchaseOrderLinesRepository;
import com.techmeridian.stockmanagement.purchaseorder.PurchaseOrderRepository;
import com.techmeridian.stockmanagement.soap.SoapParam;
import com.techmeridian.stockmanagement.soap.SoapRequestType;
import com.techmeridian.stockmanagement.soap.SoapUtils;
import com.techmeridian.stockmanagement.user.UserRepository;
import com.techmeridian.stockmanagement.utils.Utility;

@Service
public class NavPurchaseOrderLinesService {
	private Logger logger = Logger.getLogger(NavPurchaseOrderLinesService.class);

	@Autowired
	private NavProperties navProperties;

	@Autowired
	private PurchaseOrderLinesRepository purchaseOrderLinesRepository;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private PurchaseOrderRepository purchaseOrderRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CompanyRepository companyRepository;

	public String getPurchaseOrderLinesList() {
		logger.info("Fetching purchase order lines list from nav services ... ");
		String[] methodResponse = new String[] { "OK" };

		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		HttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();

		credentialsProvider.setCredentials(AuthScope.ANY, new NTCredentials(navProperties.getNavUserName(),
				navProperties.getNavPassword(), navProperties.getNavWorkstation(), navProperties.getNavDomain()));

		companyRepository.findAll().forEach(company -> {
			logger.info("Fetching purchase order line list for company " + company.getName());

			String url = String.format(navProperties.getNavPurchaseOrderLinesReadMultipleSoapURL(),
					company.getName().replaceAll(" ", "%20"));

			purchaseOrderRepository.findByCompany(company).stream().forEach(purchaseOrder -> {
				try {
					List<String> readMultiplePOMainTags = Arrays.asList(new String[] { "filter" });
					List<SoapParam> readMultiplePOParams = new ArrayList<SoapParam>();

					readMultiplePOParams.add(SoapUtils.getSoapParam("Field", "Document_No", "string"));
					readMultiplePOParams.add(SoapUtils.getSoapParam("Criteria", purchaseOrder.getNumber(), "string"));

					// Create a StringEntity for the SOAP XML.
					String soapContent = SoapUtils.getSoapContent(readMultiplePOParams, readMultiplePOMainTags,
							navProperties.getNavPurchaseOrderLinesReadMultipleNamespace(),
							SoapRequestType.READ_MULTIPLE);
					logger.info("Request body: " + soapContent);

					StringEntity stringEntity = new StringEntity(soapContent, "UTF-8");
					stringEntity.setChunked(true);

					// Request parameters and other properties.
					HttpPost httpPost = new HttpPost(url);
					httpPost.setEntity(stringEntity);
					httpPost.addHeader(SoapUtils.ACCEPT, SoapUtils.ACCEPT_VALUE);
					httpPost.addHeader(SoapUtils.CONTENT_TYPE, SoapUtils.ACCEPT_VALUE);
					httpPost.addHeader(SoapUtils.SOAP_ACTION,
							navProperties.getNavPurchaseOrderLinesReadMultipleSoapAction());

					// Execute and get the response.
					HttpResponse response = httpClient.execute(httpPost);
					HttpEntity entity = response.getEntity();

					Map<String, Item> itemMap = new HashMap<String, Item>();

					String strResponse = null;
					Set<PurchaseOrderLines> navPOLinesList = new HashSet<PurchaseOrderLines>();
					if (entity != null) {
						strResponse = EntityUtils.toString(entity);
						logger.info("Response body: " + strResponse);

						if (!strResponse.contains("s:Fault")) {

							JSONObject soapDatainJsonObject = XML.toJSONObject(strResponse);

							ObjectMapper objectMapper = new ObjectMapper();
							JsonNode jsonNode = objectMapper.readTree(soapDatainJsonObject.toString());

							JsonNode poLine = jsonNode.path("Soap:Envelope").path("Soap:Body")
									.path("ReadMultiple_Result").path("ReadMultiple_Result").path("PurchaseOrderLines");
							if (!poLine.isMissingNode() && poLine.isArray()) {
								if (poLine.size() > 0) {
									poLine.spliterator().forEachRemaining(thisPOLine -> {
										try {
											navPOLinesList.add(
													getPurchaseOrderLine(company, purchaseOrder, itemMap, thisPOLine));
										} catch (Exception e) {
											e.printStackTrace();
											throw new RuntimeException(e);
										}
									});
								}
							} else if (!poLine.isMissingNode()) {
								navPOLinesList.add(getPurchaseOrderLine(company, purchaseOrder, itemMap, poLine));
							}
						}
					}

					Map<String, PurchaseOrderLines> persistedPOLinesList = new HashMap<String, PurchaseOrderLines>();
					purchaseOrderLinesRepository.findByCompany(company).stream()
							.forEach(poLine -> persistedPOLinesList.put(getPOLKey(poLine), poLine));

					Set<PurchaseOrderLines> poLinesToPersist = new HashSet<PurchaseOrderLines>();
					for (PurchaseOrderLines navPOLine : navPOLinesList) {
						if (persistedPOLinesList.containsKey(getPOLKey(navPOLine))) {
							PurchaseOrderLines persistedPOL = persistedPOLinesList.get(getPOLKey(navPOLine));

							navPOLine.setPurchaseOrderLineId(persistedPOL.getPurchaseOrderLineId());
							navPOLine.setQuantityMobileNavReceieved(persistedPOL.getQuantityMobileNavReceieved());
							navPOLine.setPushToNav(persistedPOL.getPushToNav());

							if (!Utility.getInt(navPOLine.getQuantityReceived())
									.equals(Utility.getInt(persistedPOL.getQuantityReceived()))) {
								logger.warn(
										"Quantity recieved differ between nav and mobile nav, retaining mobile nav quantity, nav: "
												+ Utility.getInt(navPOLine.getQuantityReceived()) + ", MobileNav: "
												+ Utility.getInt(persistedPOL.getQuantityReceived()));
								navPOLine.setQuantityReceived(persistedPOL.getQuantityReceived());
							}

							if (!Utility.getInt(navPOLine.getQuantityToReceive())
									.equals(Utility.getInt(persistedPOL.getQuantityToReceive()))) {
								logger.warn(
										"Quantity to receive differ between nav and mobile nav, retaining mobile nav quantity, nav: "
												+ Utility.getInt(navPOLine.getQuantityToReceive()) + ", MobileNav: "
												+ Utility.getInt(persistedPOL.getQuantityToReceive()));
								navPOLine.setQuantityToReceive(persistedPOL.getQuantityToReceive());
							}

							logger.info(getPOLKey(navPOLine) + " exists in db, will be updated.");
						} else {
							logger.info("Found new item " + getPOLKey(navPOLine) + ", will be persisted.");
						}
						poLinesToPersist.add(navPOLine);
					}
					purchaseOrderLinesRepository.save(poLinesToPersist);
					logger.info("Fetching purchase order lines list from nav services, Success.");

				} catch (Exception exception) {
					exception.printStackTrace();
					logger.error("Error fetching purchase order lines list", exception);
					methodResponse[0] = "Error";
				}
			});
		});
		return methodResponse[0];
	}

	private PurchaseOrderLines getPurchaseOrderLine(Company company, PurchaseOrder purchaseOrder,
			Map<String, Item> itemMap, JsonNode poLine) throws Exception {
		PurchaseOrderLines purchaseOrderLines = new PurchaseOrderLines();
		purchaseOrderLines.setCompany(company);
		purchaseOrderLines.setDocumentType(poLine.path("Document_Type").asText());
		purchaseOrderLines.setDocumentNo(poLine.path("Document_No").asText());
		purchaseOrderLines.setLineNo(poLine.path("Line_No").asText());
		purchaseOrderLines.setType(poLine.path("Type").asText());
		purchaseOrderLines.setItemNo(poLine.path("No").asText());

		Item item = null;
		if (itemMap.containsKey(purchaseOrderLines.getItemNo())) {
			item = itemMap.get(purchaseOrderLines.getItemNo());
			logger.info("Adding Item: " + item);
		} else {
			item = itemRepository.findByItemNoAndCompany(purchaseOrderLines.getItemNo(), company);
			if (item == null) {
				logger.error("Item " + purchaseOrderLines.getItemNo() + " and company " + company.getName()
						+ " for purchase order " + purchaseOrderLines.getDocumentNo()
						+ " not found in item master, skipping this item. poLine-" + poLine);
				throw new Exception("Item " + purchaseOrderLines.getItemNo() + " and company " + company.getName()
						+ " for purchase order " + purchaseOrderLines.getDocumentNo()
						+ " not found in item master, skipping this item.");
			}
			itemMap.put(purchaseOrderLines.getItemNo(), item);
		}
		purchaseOrderLines.setItem(item);
		purchaseOrderLines.setPurchaseOrder(purchaseOrder);

		purchaseOrderLines.setVariantCode(poLine.path("Variant_Code").asText());
		purchaseOrderLines.setServiceTaxRegistrationNo(poLine.path("Service_Tax_Registration_No").asText());
		purchaseOrderLines.setVatProdPostingGroup(poLine.path("VAT_Prod_Posting_Group").asText());
		purchaseOrderLines.setDescription(poLine.path("Description").asText());
		purchaseOrderLines.setLocationCode(poLine.path("Location_Code").asText());
		purchaseOrderLines.setBinCode(poLine.path("Bin_Code").asText());
		purchaseOrderLines.setUnitOfMeasureCode(poLine.path("Unit_of_Measure_Code").asText());
		purchaseOrderLines.setUnitOfMeasure(poLine.path("Unit_of_Measure").asText());
		purchaseOrderLines.setQuantity(poLine.path("Quantity").asInt());
		purchaseOrderLines.setJobRemainingQty(poLine.path("Job_Remaining_Qty").asInt());
		purchaseOrderLines.setReservedQuantity(poLine.path("Reserved_Quantity").asInt());

		purchaseOrderLines.setQuantityReceived(poLine.path("Quantity_Received").asInt());
		purchaseOrderLines.setQuantityToInvoice(poLine.path("Qty_to_Invoice").asInt());
		purchaseOrderLines.setQuantityToAssign(poLine.path("Qty_to_Assign").asInt());
		purchaseOrderLines.setQuantityAssigned(poLine.path("Qty_Assigned").asInt());

		purchaseOrderLines.setQuantityMobileNavReceieved(poLine.path("Qty_to_Receive").asInt());
		purchaseOrderLines.setQuantityToReceive(getQuantityToReceive(purchaseOrderLines.getQuantity(),
				purchaseOrderLines.getQuantityReceived(), purchaseOrderLines.getQuantityMobileNavReceieved()));

		purchaseOrderLines.setDirectUnitCost(poLine.path("Direct_Unit_Cost").asText());
		purchaseOrderLines.setLineAmount(poLine.path("Line_Amount").asText());
		purchaseOrderLines.setLineDiscountPercent(poLine.path("Line_Discount_Percent").asText());
		purchaseOrderLines.setLineDiscountAmount(poLine.path("Line_Discount_Amount").asText());
		purchaseOrderLines.setRequestedReceiptDate(poLine.path("Requested_Receipt_Date").asText());
		purchaseOrderLines.setPromisedReceiptDate(poLine.path("Promised_Receipt_Date").asText());
		purchaseOrderLines.setPlannedReceiptDate(poLine.path("Planned_Receipt_Date").asText());
		purchaseOrderLines.setExpectedReceiptDate(poLine.path("Expected_Receipt_Date").asText());
		purchaseOrderLines.setOrderDate(poLine.path("Order_Date").asText());
		purchaseOrderLines.setETag(poLine.path("ETag").asText());
		purchaseOrderLines.setPartNo(poLine.path("Part_No").asText());
		if (poLine.has("Key")) {
			purchaseOrderLines.setChangeKey(poLine.path("Key").asText());
		}
		purchaseOrderLines.setUpdatedBy(userRepository.findOne(1));

		return purchaseOrderLines;
	}

	private int getQuantityToReceive(int quantity, int quantityReceived, int quantityToReceive) {
		return Utility.getInt(quantity) - (quantityToReceive + quantityReceived);
	}

	private String getPOLKey(PurchaseOrderLines purchaseOrderLines) {
		return purchaseOrderLines.getDocumentNo() + "-" + purchaseOrderLines.getItemNo() + "-"
				+ purchaseOrderLines.getCompany().getId();
	}

	public String getPurchaseOrderLinesKeys() {
		boolean[] allGood = { true };
		try {
			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			HttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider)
					.build();

			credentialsProvider.setCredentials(AuthScope.ANY, new NTCredentials(navProperties.getNavUserName(),
					navProperties.getNavPassword(), navProperties.getNavWorkstation(), navProperties.getNavDomain()));

			companyRepository.findAll().forEach(company -> {
				logger.info("Fetching po lines keys for company " + company.getName());

				String url = String.format(navProperties.getNavPurchaseOrderLinesReadSoapURL(),
						company.getName().replaceAll(" ", "%20"));

				List<SoapParam> readPurchaseOrderLinesParams = new ArrayList<SoapParam>();

				purchaseOrderLinesRepository.findByCompany(company).forEach(poLine -> {
					logger.info("Reading poLine " + poLine);
					try {
						readPurchaseOrderLinesParams.clear();

						readPurchaseOrderLinesParams
								.add(SoapUtils.getSoapParam("Document_Type", poLine.getDocumentType(), "string"));
						readPurchaseOrderLinesParams
								.add(SoapUtils.getSoapParam("Document_No", poLine.getDocumentNo(), "string"));
						readPurchaseOrderLinesParams
								.add(SoapUtils.getSoapParam("Line_No", poLine.getLineNo(), "string"));

						// Create a StringEntity for the SOAP XML.
						String soapContent = SoapUtils.getSoapContent(readPurchaseOrderLinesParams,
								Collections.emptyList(), navProperties.getNavPurchaseOrderLinesReadNamespace(),
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
								navProperties.getNavPurchaseOrderLinesReadMultipleSoapAction());

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
								poLine.setChangeKey(Utility
										.escapeCharaters(itemNode.path("PurchaseOrderLines").path("Key").asText()));

								purchaseOrderLinesRepository.save(poLine);
							} else {
								logger.info("No record in nav for poLine " + poLine);
							}
						} else {
							logger.info("Null response for fetch key poLine " + poLine);
						}

					} catch (Exception exception) {
						logger.error("Error reading poLine keys from nav for poLine " + poLine, exception);
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
			logger.error("Error fetching POLine keys ", exception);
			throw exception;
		}
	}

	@Transactional(rollbackOn = Throwable.class)
	public String pushToNav(PurchaseOrder purchaseOrder) {
		try {
			boolean[] allGood = { true };
			String[] errorMessage = { "OK" };

			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			HttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider)
					.build();

			credentialsProvider.setCredentials(AuthScope.ANY, new NTCredentials(navProperties.getNavUserName(),
					navProperties.getNavPassword(), navProperties.getNavWorkstation(), navProperties.getNavDomain()));

			List<String> poLinesUpdateMainTags = Arrays.asList(new String[] { "PurchaseOrderLines" });
			List<SoapParam> poLinesUpdateParams = new ArrayList<SoapParam>();

			List<PurchaseOrderLines> purchaseOrderLines = purchaseOrderLinesRepository
					.findByPurchaseOrder(purchaseOrder);

			purchaseOrderLines.stream().filter(poLine -> (poLine.getPushToNav() != null && poLine.getPushToNav()))
					.forEach(poLine -> {
						try {
							poLinesUpdateParams.clear();
							poLinesUpdateParams
									.add(SoapUtils.getSoapParam("Document_Type", poLine.getDocumentType(), "string"));
							poLinesUpdateParams
									.add(SoapUtils.getSoapParam("Document_No", poLine.getDocumentNo(), "string"));
							poLinesUpdateParams.add(SoapUtils.getSoapParam("Line_No", poLine.getLineNo(), "int"));
							poLinesUpdateParams.add(SoapUtils.getSoapParam("No", poLine.getItemNo(), "string"));

							poLinesUpdateParams.add(SoapUtils.getSoapParam("Qty_to_Receive",
									poLine.getQuantityMobileNavReceieved(), "int"));
							poLinesUpdateParams.add(SoapUtils.getSoapParam("Key",
									Utility.unEscapeCharaters(poLine.getChangeKey()), "string"));

							String soapContent = SoapUtils.getSoapContent(poLinesUpdateParams, poLinesUpdateMainTags,
									navProperties.getNavPurchaseOrderLinesUpdateNamespace(), SoapRequestType.UPDATE);
							logger.info("Request body: " + soapContent);

							StringEntity stringEntity = new StringEntity(soapContent, "UTF-8");
							stringEntity.setChunked(true);

							String url = String.format(navProperties.getNavPurchaseOrderLinesUpdateSoapURL(),
									purchaseOrder.getCompany().getName().replaceAll(" ", "%20"));

							// Request parameters and other properties.
							HttpPost httpPost = new HttpPost(url);
							httpPost.setEntity(stringEntity);
							httpPost.addHeader(SoapUtils.ACCEPT, SoapUtils.ACCEPT_VALUE);
							httpPost.addHeader(SoapUtils.CONTENT_TYPE, SoapUtils.ACCEPT_VALUE);
							httpPost.addHeader(SoapUtils.SOAP_ACTION,
									navProperties.getNavPurchaseOrderLinesUpdateSoapAction());

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

										int quantityToReceive = Utility.getInt(poLine.getQuantity())
												- (poLine.getQuantityMobileNavReceieved()
														+ poLine.getQuantityReceived());

										poLine.setQuantityToReceive(quantityToReceive);
										poLine.setPushToNav(false);

										// Set Key
										poLine.setChangeKey(Utility.escapeCharaters(
												itemList.path("PurchaseOrderLines").path("Key").asText()));
										logger.info("Persisting poLine: " + poLine);
										purchaseOrderLinesRepository.save(poLine);

									}
								} else {
									logger.error("Fault code for POL push, request: " + soapContent + ", response: "
											+ strResponse);
									allGood[0] = false;
								}
							}
						} catch (Exception exception) {
							logger.error("Error pushing POLine to nav ", exception);
							if (exception != null && !StringUtils.isEmpty(exception.getMessage())) {
								errorMessage[0] = exception.getMessage();
							}
							allGood[0] = false;
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

	public void reloadPurchaseOrderLines(PurchaseOrder purchaseOrder) throws Exception {
		try {
			Company company = purchaseOrder.getCompany();
			logger.info("Refreshing purchase order lines for purchase order:" + purchaseOrder.getNumber()
					+ ", company :" + company.getName());

			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			HttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider)
					.build();

			credentialsProvider.setCredentials(AuthScope.ANY, new NTCredentials(navProperties.getNavUserName(),
					navProperties.getNavPassword(), navProperties.getNavWorkstation(), navProperties.getNavDomain()));

			String url = String.format(navProperties.getNavPurchaseOrderLinesReadMultipleSoapURL(),
					company.getName().replaceAll(" ", "%20"));
			List<String> readMultiplePOMainTags = Arrays.asList(new String[] { "filter" });
			List<SoapParam> readMultiplePOParams = new ArrayList<SoapParam>();

			readMultiplePOParams.add(SoapUtils.getSoapParam("Field", "Document_No", "string"));
			readMultiplePOParams.add(SoapUtils.getSoapParam("Criteria", purchaseOrder.getNumber(), "string"));

			// Create a StringEntity for the SOAP XML.
			String soapContent = SoapUtils.getSoapContent(readMultiplePOParams, readMultiplePOMainTags,
					navProperties.getNavPurchaseOrderLinesReadMultipleNamespace(), SoapRequestType.READ_MULTIPLE);
			logger.info("Request body: " + soapContent);

			StringEntity stringEntity = new StringEntity(soapContent, "UTF-8");
			stringEntity.setChunked(true);

			// Request parameters and other properties.
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(stringEntity);
			httpPost.addHeader(SoapUtils.ACCEPT, SoapUtils.ACCEPT_VALUE);
			httpPost.addHeader(SoapUtils.CONTENT_TYPE, SoapUtils.ACCEPT_VALUE);
			httpPost.addHeader(SoapUtils.SOAP_ACTION, navProperties.getNavPurchaseOrderLinesReadMultipleSoapAction());

			// Execute and get the response.
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();

			Map<String, Item> itemMap = new HashMap<String, Item>();

			String strResponse = null;
			Set<PurchaseOrderLines> navPOLinesList = new HashSet<PurchaseOrderLines>();
			if (entity != null) {
				strResponse = EntityUtils.toString(entity);
				logger.info("Response body: " + strResponse);

				if (!strResponse.contains("s:Fault")) {

					JSONObject soapDatainJsonObject = XML.toJSONObject(strResponse);

					ObjectMapper objectMapper = new ObjectMapper();
					JsonNode jsonNode = objectMapper.readTree(soapDatainJsonObject.toString());

					JsonNode poLine = jsonNode.path("Soap:Envelope").path("Soap:Body").path("ReadMultiple_Result")
							.path("ReadMultiple_Result").path("PurchaseOrderLines");
					if (!poLine.isMissingNode() && poLine.isArray()) {
						if (poLine.size() > 0) {
							poLine.spliterator().forEachRemaining(thisPOLine -> {
								try {
									navPOLinesList
											.add(getPurchaseOrderLine(company, purchaseOrder, itemMap, thisPOLine));
								} catch (Exception e) {
									e.printStackTrace();
									throw new RuntimeException(e);
								}
							});
						}
					} else if (!poLine.isMissingNode()) {
						navPOLinesList.add(getPurchaseOrderLine(company, purchaseOrder, itemMap, poLine));
					}
				}
			}

			Map<String, PurchaseOrderLines> persistedPOLinesList = new HashMap<String, PurchaseOrderLines>();
			purchaseOrderLinesRepository.findByPurchaseOrderAndCompany(purchaseOrder, company).stream()
					.forEach(poLine -> persistedPOLinesList.put(getPOLKey(poLine), poLine));

			Set<PurchaseOrderLines> poLinesToPersist = new HashSet<PurchaseOrderLines>();
			for (PurchaseOrderLines navPOLine : navPOLinesList) {
				if (persistedPOLinesList.containsKey(getPOLKey(navPOLine))) {
					PurchaseOrderLines persistedPOL = persistedPOLinesList.get(getPOLKey(navPOLine));

					navPOLine.setPurchaseOrderLineId(persistedPOL.getPurchaseOrderLineId());
					navPOLine.setPushToNav(persistedPOL.getPushToNav());

					logger.info(getPOLKey(navPOLine) + " exists in db, will be refreshed.");
				} else {
					logger.info("Found new item " + getPOLKey(navPOLine) + ", will be persisted.");
				}
				poLinesToPersist.add(navPOLine);
			}
			purchaseOrderLinesRepository.save(poLinesToPersist);
			logger.info("Refreshing purchase order lines for purchase order:" + purchaseOrder.getNumber()
					+ ", company :" + company.getName() + ", success.");
		} catch (Exception exception) {
			logger.error("Error refreshing purchase order lines ", exception);
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

			String url = String.format(navProperties.getNavPurchaseOrderLinesReadMultipleSoapURL(),
					company.replaceAll(" ", "%20"));
			List<String> readMultiplePOMainTags = Arrays.asList(new String[] { "filter" });
			List<SoapParam> readMultiplePOParams = new ArrayList<SoapParam>();

			readMultiplePOParams.add(SoapUtils.getSoapParam("Field", "Document_No", "string"));
			readMultiplePOParams.add(SoapUtils.getSoapParam("Criteria", documentNumber, "string"));

			// Create a StringEntity for the SOAP XML.
			String soapContent = SoapUtils.getSoapContent(readMultiplePOParams, readMultiplePOMainTags,
					navProperties.getNavPurchaseOrderLinesReadMultipleNamespace(), SoapRequestType.READ_MULTIPLE);
			logger.info("Request body: " + soapContent);

			StringEntity stringEntity = new StringEntity(soapContent, "UTF-8");
			stringEntity.setChunked(true);

			// Request parameters and other properties.
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(stringEntity);
			httpPost.addHeader(SoapUtils.ACCEPT, SoapUtils.ACCEPT_VALUE);
			httpPost.addHeader(SoapUtils.CONTENT_TYPE, SoapUtils.ACCEPT_VALUE);
			httpPost.addHeader(SoapUtils.SOAP_ACTION, navProperties.getNavPurchaseOrderLinesReadMultipleSoapAction());

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

					JsonNode poLine = jsonNode.path("Soap:Envelope").path("Soap:Body").path("ReadMultiple_Result")
							.path("ReadMultiple_Result").path("PurchaseOrderLines");
					if (!poLine.isMissingNode() && poLine.isArray()) {
						for (JsonNode jsonNode2 : poLine) {
							String lineNoToCheck = jsonNode2.path("Line_No").asText();
							if (lineNoToCheck.equals(lineNo)) {
								int quantityToReceiveToCheck = jsonNode2.path("Qty_to_Receive").asInt();
								int quantityReceivedToCheck = jsonNode2.path("Quantity_Received").asInt();
								if (quantityReceived != quantityReceivedToCheck
										|| quantityToReceive != quantityToReceiveToCheck) {

									logger.error("Quantities does not match after push to nav POL , DocNo: "
											+ documentNumber + ", lineNo: " + lineNo
											+ ", quantityReceived[MobileNav Side]: " + quantityReceived
											+ ", quantityReceived[Nav Side]: " + quantityReceivedToCheck
											+ ", quantityToReceive[MobileNav Side]: " + quantityToReceive
											+ ", quantityToReceive[Nav Side]: " + quantityToReceiveToCheck);
									throw new RuntimeException(
											"Quantities does not match after push to nav POL , DocNo: " + documentNumber
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
					} else if (!poLine.isMissingNode()) {
						String lineNoToCheck = poLine.path("Line_No").asText();
						if (lineNoToCheck.equals(lineNo)) {
							int quantityToReceiveToCheck = poLine.path("Qty_to_Receive").asInt();
							int quantityReceivedToCheck = poLine.path("Quantity_Received").asInt();
							if (quantityReceived != quantityReceivedToCheck
									|| quantityToReceive != quantityToReceiveToCheck) {

								logger.error("Quantities does not match after push to nav POL , DocNo: "
										+ documentNumber + ", lineNo: " + lineNo
										+ ", quantityReceived[MobileNav Side]: " + quantityReceived
										+ ", quantityReceived[Nav Side]: " + quantityReceivedToCheck
										+ ", quantityToReceive[MobileNav Side]: " + quantityToReceive
										+ ", quantityToReceive[Nav Side]: " + quantityToReceiveToCheck);
								throw new RuntimeException("Quantities does not match after push to nav POL , DocNo: "
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
			logger.error("Error checking quatities after push to NAV ", exception);
			throw exception;
		}
		return foundLineItem;
	}
}
