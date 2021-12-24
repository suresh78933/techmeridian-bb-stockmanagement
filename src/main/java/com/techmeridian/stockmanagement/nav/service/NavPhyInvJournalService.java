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
import com.techmeridian.stockmanagement.item.ItemRepository;
import com.techmeridian.stockmanagement.journal.PhyJournal;
import com.techmeridian.stockmanagement.journal.PhyJournalItem;
import com.techmeridian.stockmanagement.journal.PhyJournalItemRepository;
import com.techmeridian.stockmanagement.journal.PhyJournalRepository;
import com.techmeridian.stockmanagement.journal.PhyJournalTemplate;
import com.techmeridian.stockmanagement.journal.PhyJournalTemplateRepository;
import com.techmeridian.stockmanagement.soap.SoapParam;
import com.techmeridian.stockmanagement.soap.SoapRequestType;
import com.techmeridian.stockmanagement.soap.SoapUtils;
import com.techmeridian.stockmanagement.utils.Utility;

@Service
public class NavPhyInvJournalService {

	private Logger logger = Logger.getLogger(NavPhyInvJournalService.class);

	@Autowired
	private NavProperties navProperties;

	@Autowired
	PhyJournalRepository phyJournalRepository;

	@Autowired
	PhyJournalItemRepository phyJournalItemRepository;

	@Autowired
	PhyJournalTemplateRepository phyJournalTemplateRepository;

	@Autowired
	ItemRepository itemRepository;

	@Autowired
	private CompanyRepository companyRepository;

	public String getJournals() throws Exception {

		logger.info("Fetching journal list ... ");
		String[] methodResponse = new String[] { "OK" };

		try {
			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			HttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider)
					.build();

			credentialsProvider.setCredentials(AuthScope.ANY, new NTCredentials(navProperties.getNavUserName(),
					navProperties.getNavPassword(), navProperties.getNavWorkstation(), navProperties.getNavDomain()));

			companyRepository.findAll().forEach(company -> {
				logger.info("Fetching journal list for company " + company.getName());

				String url = String.format(navProperties.getNavJournallistSoapurl(),
						company.getName().replaceAll(" ", "%20"));

				Iterable<PhyJournalTemplate> journalTemplates = phyJournalTemplateRepository.findByCompany(company);

				Set<String> templates = new HashSet<String>();
				journalTemplates.spliterator().forEachRemaining(template -> templates.add(template.getName()));
				List<String> readJournalMainTags = Arrays.asList(new String[] { "filter" });
				List<SoapParam> readJournalParams = new ArrayList<SoapParam>();

				templates.stream().forEach(template -> {
					try {
						logger.info("Fetching journals for template " + template);
						readJournalParams.clear();

						readJournalParams.add(SoapUtils.getSoapParam("Field", "Journal_Template_Name", "string"));
						readJournalParams.add(SoapUtils.getSoapParam("Criteria", template, "string"));
						// Create a StringEntity for the SOAP XML.
						String soapContent = SoapUtils.getSoapContent(readJournalParams, readJournalMainTags,
								navProperties.getNavJournallistNamespace(), SoapRequestType.READ_MULTIPLE);
						logger.info("Request body: " + soapContent);

						StringEntity stringEntity = new StringEntity(soapContent, "UTF-8");
						stringEntity.setChunked(true);

						// Request parameters and other properties.
						HttpPost httpPost = new HttpPost(url);
						httpPost.setEntity(stringEntity);
						httpPost.addHeader(SoapUtils.ACCEPT, SoapUtils.ACCEPT_VALUE);
						httpPost.addHeader(SoapUtils.CONTENT_TYPE, SoapUtils.ACCEPT_VALUE);
						httpPost.addHeader(SoapUtils.SOAP_ACTION, navProperties.getNavJournallistSoapaction());

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

							JsonNode journalList = jsonNode.path("Soap:Envelope").path("Soap:Body")
									.path("ReadMultiple_Result").path("ReadMultiple_Result").path("ItemJournalBatches");

							if (journalList.size() > 0) {
								journalList.spliterator().forEachRemaining(action -> {
									PhyJournal phyJournal = phyJournalRepository
											.findByNameAndCompany(action.path("Name").asText(), company);
									if (phyJournal == null) {
										phyJournalRepository.save(getPhyJournal(action, company));
										logger.info("Phy Journal " + action.path("Name").asText() + " for company "
												+ company.getName() + " saved.");
									} else {
										logger.info("Phy Journal " + phyJournal.getName() + " company "
												+ company.getName() + ", allready exist.");
									}
								});
							} else {
								logger.info("No Phy Journals found for template " + template + ", company "
										+ company.getName());
							}

						} else {
							logger.info("Null response for template " + template + ", company " + company.getName());
						}
					} catch (Exception exception) {
						logger.error("Error fetching journal list for template " + template + ", company "
								+ company.getName(), exception);
						methodResponse[0] = "Error";
					}
				});
			});
		} catch (Exception exception) {
			methodResponse[0] = "Error";
			logger.error("Error fetching journal list ", exception);
			throw exception;
		}
		return methodResponse[0];
	}

	private PhyJournal getPhyJournal(JsonNode journal, Company company) {
		PhyJournal phyJournal = new PhyJournal();
		phyJournal.setJournalTemplateName(journal.path("Journal_Template_Name").asText());
		phyJournal.setName(journal.path("Name").asText());
		phyJournal.setDescription(
				StringUtils.isEmpty(journal.path("Description")) ? "" : journal.path("Description").asText());
		phyJournal
				.setNoSeries(StringUtils.isEmpty(journal.path("No_Series")) ? "" : journal.path("No_Series").asText());
		phyJournal.setLocationCode(
				StringUtils.isEmpty(journal.path("Location_Code")) ? "" : journal.path("Location_Code").asText());
		phyJournal.setCompany(company);
		return phyJournal;
	}

	public String getJournalItems() {
		boolean[] allGood = { true };
		logger.info("Fetching journal items ... ");
		try {
			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			HttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider)
					.build();

			credentialsProvider.setCredentials(AuthScope.ANY, new NTCredentials(navProperties.getNavUserName(),
					navProperties.getNavPassword(), navProperties.getNavWorkstation(), navProperties.getNavDomain()));

			companyRepository.findAll().forEach(company -> {
				logger.info("Fetching journal item list for company " + company.getName());

				String url = String.format(navProperties.getNavPhyInvJournalReadMultipleSoapurl(),
						company.getName().replaceAll(" ", "%20"));

				Iterable<PhyJournal> phyJournals = phyJournalRepository.findByCompany(company);

				phyJournals.spliterator().forEachRemaining(phyJournal -> {
					try {
						logger.info("Fetching item for journal:" + phyJournal.getName() + ", template: "
								+ phyJournal.getJournalTemplateName() + ", company: " + company.getName());

						StringBuilder fieldContent = new StringBuilder();
						Map<String, String> filterCritiria = new HashMap<String, String>();
						filterCritiria.put("Field", "Location_Code");
						filterCritiria.put("Criteria", phyJournal.getLocationCode());
						fieldContent.append(SoapUtils.getFilterContent(filterCritiria));

						filterCritiria.put("Field", "Journal_Template_Name");
						filterCritiria.put("Criteria", phyJournal.getJournalTemplateName());
						fieldContent.append(SoapUtils.getFilterContent(filterCritiria));

						filterCritiria.put("Field", "Journal_Batch_Name");
						filterCritiria.put("Criteria", phyJournal.getName());
						fieldContent.append(SoapUtils.getFilterContent(filterCritiria));

						String soapContent = SoapUtils.getSoapContent(fieldContent.toString(), Collections.emptyList(),
								navProperties.getNavPhyInvJournalReadMultipleNamespace(),
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
								navProperties.getNavPhyInvJournalReadMultipleNamespace());

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

							JsonNode itemList = jsonNode.path("Soap:Envelope").path("Soap:Body")
									.path("ReadMultiple_Result").path("ReadMultiple_Result").path("PhyInvJournal");

							if (itemList != null) {
								if (itemList.isArray()) {
									if (itemList.size() > 0) {
										itemList.spliterator().forEachRemaining(itemNode -> {
											phyInJrnlItemHandler(phyJournal, itemNode, company);
										});
									} else {
										logger.info("No items found for journal " + phyJournal.getName());
									}
								} else {
									phyInJrnlItemHandler(phyJournal, itemList, company);
								}
							}
						} else {
							logger.info("Null response for journal " + phyJournal.getName());
						}
					} catch (Exception exception) {
						logger.error("Error fetching journal item list for journal " + phyJournal.getName(), exception);
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
			logger.error("Error fetching journal item list ", exception);
			throw exception;
		}
	}

	private void phyInJrnlItemHandler(PhyJournal phyJournal, JsonNode itemNode, Company company) {
		if (itemNode.path("Journal_Batch_Name").asText().equals(phyJournal.getName())
				&& itemNode.path("Journal_Template_Name").asText().equals(phyJournal.getJournalTemplateName())
				&& itemNode.path("Location_Code").asText().equals(phyJournal.getLocationCode())) {

			PhyJournalItem phyJournalItem = phyJournalItemRepository
					.findByItemNoAndJournalBatchNameAndLocationCodeAndLineNoAndCompany(
							itemNode.path("Item_No").asText(), itemNode.path("Journal_Batch_Name").asText(),
							itemNode.path("Location_Code").asText(),
							Utility.getIntFromString(itemNode.path("Line_No").asText()), company);

			if (phyJournalItem == null) {
				logger.info("Journal item not found in db, creating new ... {Item: " + itemNode.path("Item_No").asText()
						+ ", JournalBatchName: " + itemNode.path("Journal_Batch_Name").asText() + ", Location_Code: "
						+ itemNode.path("Location_Code").asText() + ", Company: " + company.getName() + "}");
				Item item = itemRepository.findByItemNoAndCompany(itemNode.path("Item_No").asText(), company);
				phyJournalItemRepository.save(getPhyJournalItem(itemNode, phyJournal, item, null, company));
			} else {
				logger.info("Journal found in db, updating ... {Item: " + itemNode.path("Item_No").asText()
						+ ", JournalBatchName: " + itemNode.path("Journal_Batch_Name").asText() + ", Location_Code: "
						+ itemNode.path("Location_Code").asText() + ", Company: " + company.getName() + "}");
				phyJournalItemRepository.save(getPhyJournalItem(itemNode, null, null, phyJournalItem, company));
			}
		} else {
			logger.warn(itemNode + ", does not have items included in the filter request " + phyJournal);
		}
	}

	private PhyJournalItem getPhyJournalItem(JsonNode phyJournalItemNode, PhyJournal phyJournal, Item item,
			PhyJournalItem phyJournalItem, Company company) {
		if (phyJournalItem == null) {
			phyJournalItem = new PhyJournalItem();
		}
		if (phyJournal != null) {
			phyJournalItem.setPhyJournal(phyJournal);
		}
		if (item != null) {
			phyJournalItem.setItem(item);
		}

		phyJournalItem.setJournalTemplateName(phyJournalItemNode.path("Journal_Template_Name").asText());
		phyJournalItem.setJournalBatchName(phyJournalItemNode.path("Journal_Batch_Name").asText());
		phyJournalItem.setLineNo(getValue("Line_No", phyJournalItemNode.path("Line_No").asText()));
		phyJournalItem.setPostingDate(phyJournalItemNode.path("Posting_Date").asText());
		phyJournalItem.setDocumentDate(phyJournalItemNode.path("Document_Date").asText());
		phyJournalItem.setEntryType(phyJournalItemNode.path("Entry_Type").asText());
		phyJournalItem.setDocumentNo(phyJournalItemNode.path("Document_No").asText());
		phyJournalItem.setItemNo(phyJournalItemNode.path("Item_No").asText());
		phyJournalItem.setVariantCode(phyJournalItemNode.path("Variant_Code").asText());
		phyJournalItem.setDescription(phyJournalItemNode.path("Description").asText());
		phyJournalItem.setLocationCode(phyJournalItemNode.path("Location_Code").asText());
		phyJournalItem.setBinCode(phyJournalItemNode.path("Bin_Code").asText());
		phyJournalItem.setSalespersPurchCode(phyJournalItemNode.path("Salespers_Purch_Code").asText());
		phyJournalItem.setGenBusPostingGroup(phyJournalItemNode.path("Gen_Bus_Posting_Group").asText());
		phyJournalItem.setGenProdPostingGroup(phyJournalItemNode.path("Gen_Prod_Posting_Group").asText());
		phyJournalItem.setQtyCalculated(getValue("Qty_Calculated", phyJournalItemNode.path("Qty_Calculated").asText()));
		phyJournalItem.setQtyPhysInventory(
				getValue("Qty_Phys_Inventory", phyJournalItemNode.path("Qty_Phys_Inventory").asText()));
		phyJournalItem.setQuantity(getValue("Quantity", phyJournalItemNode.path("Quantity").asText()));
		phyJournalItem.setUnitOfMeasureCode(phyJournalItemNode.path("Unit_of_Measure_Code").asText());
		phyJournalItem.setUnitAmount(phyJournalItemNode.path("Unit_Amount").asText());
		phyJournalItem.setAmount(phyJournalItemNode.path("Amount").asText());
		phyJournalItem.setIndirectCostPercent(phyJournalItemNode.path("Indirect_Cost_Percent").asText());
		phyJournalItem.setUnitCost(phyJournalItemNode.path("Unit_Cost").asText());
		phyJournalItem
				.setAppliesToEntry(getValue("Applies_to_Entry", phyJournalItemNode.path("Applies_to_Entry").asText()));
		phyJournalItem.setReasonCode(phyJournalItemNode.path("Reason_Code").asText());
		phyJournalItem.setPhysInventory(phyJournalItemNode.path("Phys_Inventory").asText());
		phyJournalItem.setItemDescription(phyJournalItemNode.path("ItemDescription").asText());
		phyJournalItem.setCompany(company);

		return phyJournalItem;
	}

	private int getValue(String key, String value) {
		try {
			return StringUtils.isEmpty(value) ? 0 : Integer.parseInt(value);
		} catch (Exception exception) {
			logger.error("Not able to convert to int for key: " + key + ", value: " + value, exception);
		}
		return 0;
	}

	public String pushToNav(PhyJournal phyJournal) throws Exception {
		logger.info("Pushing journal to Nav, " + phyJournal);
		boolean[] allGood = { true };
		try {
			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			HttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider)
					.build();

			credentialsProvider.setCredentials(AuthScope.ANY, new NTCredentials(navProperties.getNavUserName(),
					navProperties.getNavPassword(), navProperties.getNavWorkstation(), navProperties.getNavDomain()));
			List<String> pushToNavJournalItemsMainTags = Arrays.asList(new String[] { "PhyInvJournal" });
			List<SoapParam> pushToNavJournalItemsParams = new ArrayList<SoapParam>();

			List<PhyJournalItem> phyJournalItems = phyJournalItemRepository.findByPhyJournal(phyJournal);

			phyJournalItems.stream().filter(phyJournalItem -> Utility.getInt(phyJournalItem.getMobileNavQuantity()) > 0)
					.forEach(phyJournalItem -> {
						try {
							boolean isUpdate = Utility.isStringNotEmpty(phyJournalItem.getChangeKey());

							pushToNavJournalItemsParams.clear();
							pushToNavJournalItemsParams.add(SoapUtils.getSoapParam("Journal_Template_Name",
									phyJournalItem.getJournalTemplateName(), "string"));
							pushToNavJournalItemsParams.add(SoapUtils.getSoapParam("Journal_Batch_Name",
									phyJournalItem.getJournalBatchName(), "string"));
							pushToNavJournalItemsParams
									.add(SoapUtils.getSoapParam("Line_No", phyJournalItem.getLineNo(), "int"));
							pushToNavJournalItemsParams
									.add(SoapUtils.getSoapParam("Entry_Type", "Positive_Adjmt", "string"));

							PhyJournalItem journalItemReadFromNav = null;
							if (isUpdate) {
								pushToNavJournalItemsParams.add(SoapUtils.getSoapParam("Key",
										Utility.unEscapeCharaters(phyJournalItem.getChangeKey()), "string"));

								journalItemReadFromNav = getJournalItem(phyJournalItem);
							}

							if (isUpdate) {
								pushToNavJournalItemsParams.add(SoapUtils.getSoapParam("Document_No",
										phyJournalItem.getDocumentNo(), "string"));
							} else {
								String documentId = "DOC-" + phyJournalItem.getLineNo();
								phyJournalItem.setDocumentNo(documentId);
								pushToNavJournalItemsParams
										.add(SoapUtils.getSoapParam("Document_No", documentId, "string"));
							}
							pushToNavJournalItemsParams
									.add(SoapUtils.getSoapParam("Item_No", phyJournalItem.getItemNo(), "string"));

							int quantity = 0;
							if (isUpdate) {
								// Update
								if (journalItemReadFromNav == null || journalItemReadFromNav.getQuantity() == 0) {
									// Nav side, this item is posted
									quantity = Utility.getInt(phyJournalItem.getMobileNavQuantity());
								} else if (journalItemReadFromNav.getQuantity() > 0) {
									// Nav side, this item is not yet posted
									quantity = Utility.getInt(phyJournalItem.getMobileNavQuantity())
											+ Utility.getInt(phyJournalItem.getQuantity());
								}
							} else {
								// Create
								quantity = Utility.getInt(phyJournalItem.getMobileNavQuantity())
										+ Utility.getInt(phyJournalItem.getQuantity());
							}
							pushToNavJournalItemsParams.add(SoapUtils.getSoapParam("Quantity", quantity, "int"));

							pushToNavJournalItemsParams.add(SoapUtils.getSoapParam("Unit_of_Measure_Code",
									phyJournalItem.getUnitOfMeasureCode(), "string"));
							pushToNavJournalItemsParams.add(SoapUtils.getSoapParam("Location_Code",
									phyJournalItem.getLocationCode(), "string"));
							pushToNavJournalItemsParams.add(SoapUtils.getSoapParam("From_Mobile", "True", "boolean"));

							// Set posting date
							phyJournalItem.setPostingDate(Utility.getNavDate());
							pushToNavJournalItemsParams.add(
									SoapUtils.getSoapParam("Posting_Date", phyJournalItem.getPostingDate(), "date"));

							String soapContent = SoapUtils.getSoapContent(pushToNavJournalItemsParams,
									pushToNavJournalItemsMainTags,
									(isUpdate ? navProperties.getNavPhyInvJournalUpdateNamespace()
											: navProperties.getNavPhyInvJournalCreateNamespace()),
									(isUpdate ? SoapRequestType.UPDATE : SoapRequestType.CREATE));
							logger.info("Request body: " + soapContent);

							StringEntity stringEntity = new StringEntity(soapContent, "UTF-8");
							stringEntity.setChunked(true);

							String url = isUpdate ? navProperties.getNavPhyInvJournalUpdateSoapurl()
									: navProperties.getNavPhyInvJournalCreateSoapurl();

							url = String.format(url, phyJournal.getCompany().getName().replaceAll(" ", "%20"));

							// Request parameters and other properties.
							HttpPost httpPost = new HttpPost(url);
							httpPost.setEntity(stringEntity);
							httpPost.addHeader(SoapUtils.ACCEPT, SoapUtils.ACCEPT_VALUE);
							httpPost.addHeader(SoapUtils.CONTENT_TYPE, SoapUtils.ACCEPT_VALUE);
							httpPost.addHeader(SoapUtils.SOAP_ACTION,
									isUpdate ? navProperties.getNavPhyInvJournalUpdateSoapaction()
											: navProperties.getNavPhyInvJournalCreateSoapaction());

							// Execute and get the response.
							HttpResponse response = httpClient.execute(httpPost);
							HttpEntity entity = response.getEntity();

							String strResponse = "";
							if (entity != null) {
								strResponse = EntityUtils.toString(entity);
								logger.info("Response body: " + strResponse);

								if (!strResponse.contains("s:Fault")) {
									logger.info("Push to nav success for journal, " + phyJournal);
									JSONObject soapDatainJsonObject = XML.toJSONObject(strResponse);

									ObjectMapper objectMapper = new ObjectMapper();
									JsonNode jsonNode = objectMapper.readTree(soapDatainJsonObject.toString());

									JsonNode itemList = null;
									if (isUpdate) {
										itemList = jsonNode.path("Soap:Envelope").path("Soap:Body")
												.path("Update_Result");
									} else {
										itemList = jsonNode.path("Soap:Envelope").path("Soap:Body")
												.path("Create_Result");
									}
									if (itemList != null) {
										phyJournalItem.setQuantity(quantity);
										phyJournalItem.setMobileNavQuantity(0);

										// Set Key
										phyJournalItem.setChangeKey(Utility
												.escapeCharaters(itemList.path("PhyInvJournal").path("Key").asText()));
										logger.info("Persisting journal item: " + phyJournalItem);
										phyJournalItemRepository.save(phyJournalItem);
									}
								} else {
									allGood[0] = false;
								}
							}

						} catch (Exception exception) {
							logger.error("Error pushing journal to nav ", exception);
							allGood[0] = false;
						}
					});
			if (allGood[0]) {
				return "OK";
			} else {
				return "Error";
			}
		} catch (Exception exception) {
			logger.error("Error pushing journal to nav ", exception);
			throw exception;
		}
	}

	public String getJournalItemKeys() {

		boolean[] allGood = { true };
		try {
			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			HttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider)
					.build();

			credentialsProvider.setCredentials(AuthScope.ANY, new NTCredentials(navProperties.getNavUserName(),
					navProperties.getNavPassword(), navProperties.getNavWorkstation(), navProperties.getNavDomain()));

			companyRepository.findAll().forEach(company -> {
				logger.info("Fetching journal item list for company " + company.getName());

				String url = String.format(navProperties.getNavPhyInvJournalReadSoapurl(),
						company.getName().replaceAll(" ", "%20"));

				List<SoapParam> readJournalParams = new ArrayList<SoapParam>();

				phyJournalItemRepository.findByCompany(company).forEach(item -> {
					logger.info("Reading item " + item);
					try {
						readJournalParams.clear();

						readJournalParams.add(SoapUtils.getSoapParam("Journal_Template_Name",
								item.getJournalTemplateName(), "string"));
						readJournalParams.add(
								SoapUtils.getSoapParam("Journal_Batch_Name", item.getJournalBatchName(), "string"));
						readJournalParams.add(SoapUtils.getSoapParam("Line_No", item.getLineNo(), "string"));

						// Create a StringEntity for the SOAP XML.
						String soapContent = SoapUtils.getSoapContent(readJournalParams, Collections.emptyList(),
								navProperties.getNavPhyInvJournalReadNamespace(), SoapRequestType.READ);
						logger.info("Request body: " + soapContent);

						StringEntity stringEntity = new StringEntity(soapContent, "UTF-8");
						stringEntity.setChunked(true);

						// Request parameters and other properties.
						HttpPost httpPost = new HttpPost(url);
						httpPost.setEntity(stringEntity);
						httpPost.addHeader(SoapUtils.ACCEPT, SoapUtils.ACCEPT_VALUE);
						httpPost.addHeader(SoapUtils.CONTENT_TYPE, SoapUtils.ACCEPT_VALUE);
						httpPost.addHeader(SoapUtils.SOAP_ACTION, navProperties.getNavPhyInvJournalReadSoapaction());

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
								item.setChangeKey(
										Utility.escapeCharaters(itemNode.path("PhyInvJournal").path("Key").asText()));

								phyJournalItemRepository.save(item);
							} else {
								logger.info("No record in nav for item " + item);
							}
						} else {
							logger.info("Null response for template " + item);
						}

					} catch (Exception exception) {
						logger.error("Error reading item keys from nav ", exception);
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
			logger.error("Error reading journal item keys ", exception);
			throw exception;
		}
	}

	public PhyJournalItem getJournalItem(PhyJournalItem journalItemToRead) {

		PhyJournalItem journalItemRead = null;
		try {
			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			HttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider)
					.build();

			credentialsProvider.setCredentials(AuthScope.ANY, new NTCredentials(navProperties.getNavUserName(),
					navProperties.getNavPassword(), navProperties.getNavWorkstation(), navProperties.getNavDomain()));

			logger.info("Fetching journal item " + journalItemToRead);

			String url = String.format(navProperties.getNavPhyInvJournalReadSoapurl(),
					journalItemToRead.getCompany().getName().replaceAll(" ", "%20"));

			List<SoapParam> readJournalParams = new ArrayList<SoapParam>();

			try {
				readJournalParams.clear();

				readJournalParams.add(SoapUtils.getSoapParam("Journal_Template_Name",
						journalItemToRead.getJournalTemplateName(), "string"));
				readJournalParams.add(SoapUtils.getSoapParam("Journal_Batch_Name",
						journalItemToRead.getJournalBatchName(), "string"));
				readJournalParams.add(SoapUtils.getSoapParam("Line_No", journalItemToRead.getLineNo(), "string"));

				// Create a StringEntity for the SOAP XML.
				String soapContent = SoapUtils.getSoapContent(readJournalParams, Collections.emptyList(),
						navProperties.getNavPhyInvJournalReadNamespace(), SoapRequestType.READ);
				logger.info("Request body: " + soapContent);

				StringEntity stringEntity = new StringEntity(soapContent, "UTF-8");
				stringEntity.setChunked(true);

				// Request parameters and other properties.
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(stringEntity);
				httpPost.addHeader(SoapUtils.ACCEPT, SoapUtils.ACCEPT_VALUE);
				httpPost.addHeader(SoapUtils.CONTENT_TYPE, SoapUtils.ACCEPT_VALUE);
				httpPost.addHeader(SoapUtils.SOAP_ACTION, navProperties.getNavPhyInvJournalReadSoapaction());

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
						journalItemRead = getPhyJournalItem(itemNode.path("PhyInvJournal"), null, null, null, null);
					} else {
						logger.info("No record in nav for item " + journalItemToRead);
					}
				} else {
					logger.info("Null response for template " + journalItemToRead);
				}

			} catch (Exception exception) {
				logger.error("Error reading item keys from nav ", exception);
			}
		} catch (Exception exception) {
			logger.error("Error reading journal item keys ", exception);
			throw exception;
		}
		return journalItemRead;
	}
}
