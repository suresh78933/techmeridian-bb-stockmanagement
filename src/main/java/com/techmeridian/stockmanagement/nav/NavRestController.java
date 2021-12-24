package com.techmeridian.stockmanagement.nav;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.techmeridian.stockmanagement.nav.service.NavCompanyService;
import com.techmeridian.stockmanagement.nav.service.NavItemService;
import com.techmeridian.stockmanagement.nav.service.NavLocationService;
import com.techmeridian.stockmanagement.nav.service.NavPhyInvJournalService;
import com.techmeridian.stockmanagement.nav.service.NavProperties;
import com.techmeridian.stockmanagement.nav.service.NavPurchaseOrderLinesService;
import com.techmeridian.stockmanagement.nav.service.NavPurchaseOrderService;
import com.techmeridian.stockmanagement.nav.service.NavStockIssueLinesService;
import com.techmeridian.stockmanagement.nav.service.NavStockIssueService;
import com.techmeridian.stockmanagement.nav.service.NavUnitOfMeasureService;
import com.techmeridian.stockmanagement.nav.service.NavUserService;
import com.techmeridian.stockmanagement.nav.service.NavVendorService;
import com.techmeridian.stockmanagement.session.SessionUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/nav")
@Api(value = "Nav Controller", description = "Contains operations related NAV services")
public class NavRestController {

	private Logger logger = Logger.getLogger(NavRestController.class);

	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private NavProperties navProperties;

	@Autowired
	NavItemService navItemListService;

	@Autowired
	NavPurchaseOrderService navPurchaseOrderService;

	@Autowired
	NavPurchaseOrderLinesService navPurchaseOrderLinesService;

	@Autowired
	NavUnitOfMeasureService navUnitOfMeasureService;

	@Autowired
	NavStockIssueService navStockIssueService;

	@Autowired
	NavVendorService navVendorListService;

	@Autowired
	NavStockIssueLinesService navStockIssueLinesService;

	@Autowired
	NavPhyInvJournalService navPhyInvJournalService;

	@Autowired
	NavLocationService navLocationService;

	@Autowired
	NavCompanyService navCompanyService;

	@Autowired
	NavUserService navUserService;

	@ApiOperation(value = "List service details", produces = "application/json")
	@GetMapping("/list")
	@ResponseBody
	public String listEndPointDetails() throws Exception {
		JsonNode rootNode = mapper.createObjectNode();

		((ObjectNode) rootNode).put("uname", navProperties.getNavUserName());

		// Unit of measure
		JsonNode unitOfMeasure = mapper.createObjectNode();
		((ObjectNode) unitOfMeasure).put("nameSpace", navProperties.getNavUnitOfMeasureNamespace());
		((ObjectNode) unitOfMeasure).put("methodName", navProperties.getNavUnitOfMeasureMethodName());
		((ObjectNode) unitOfMeasure).put("soapAction", navProperties.getNavUnitOfMeasureSoapAction());
		((ObjectNode) unitOfMeasure).put("soapUrl", navProperties.getNavUnitOfMeasureSoapURL());
		((ObjectNode) rootNode).set("unitOfMeasureProps", unitOfMeasure);

		// Item list
		JsonNode iteListNode = mapper.createObjectNode();
		((ObjectNode) iteListNode).put("nameSpace", navProperties.getNavItemListNamespace());
		((ObjectNode) iteListNode).put("methodName", navProperties.getNavItemListMethodName());
		((ObjectNode) iteListNode).put("soapAction", navProperties.getNavItemListSoapAction());
		((ObjectNode) iteListNode).put("soapUrl", navProperties.getNavItemListSoapURL());
		((ObjectNode) rootNode).set("itemListProps", iteListNode);

		// Vendor list
		JsonNode vendorList = mapper.createObjectNode();
		((ObjectNode) vendorList).put("nameSpace", navProperties.getNavVendorListNamespace());
		((ObjectNode) vendorList).put("methodName", navProperties.getNavVendorListMethodName());
		((ObjectNode) vendorList).put("soapAction", navProperties.getNavVendorListSoapAction());
		((ObjectNode) vendorList).put("soapUrl", navProperties.getNavVendorListSoapURL());
		((ObjectNode) rootNode).set("vendorListProps", vendorList);

		// Phy Journal read multiple
		JsonNode pijrmp = mapper.createObjectNode();
		((ObjectNode) pijrmp).put("nameSpace", navProperties.getNavPhyInvJournalReadMultipleNamespace());
		((ObjectNode) pijrmp).put("methodName", navProperties.getNavPhyInvJournalReadMultipleMethodname());
		((ObjectNode) pijrmp).put("soapAction", navProperties.getNavPhyInvJournalReadMultipleSoapaction());
		((ObjectNode) pijrmp).put("soapUrl", navProperties.getNavPhyInvJournalReadMultipleSoapurl());
		((ObjectNode) rootNode).set("phyInvJournalReadMultipleProps", pijrmp);

		// Phy Journal read
		JsonNode pijrp = mapper.createObjectNode();
		((ObjectNode) pijrp).put("nameSpace", navProperties.getNavPhyInvJournalReadNamespace());
		((ObjectNode) pijrp).put("methodName", navProperties.getNavPhyInvJournalReadMethodname());
		((ObjectNode) pijrp).put("soapAction", navProperties.getNavPhyInvJournalReadSoapaction());
		((ObjectNode) pijrp).put("soapUrl", navProperties.getNavPhyInvJournalReadSoapurl());
		((ObjectNode) rootNode).set("phyInvJournalReadProps", pijrp);

		// Phy Journal read create
		JsonNode pijcp = mapper.createObjectNode();
		((ObjectNode) pijcp).put("nameSpace", navProperties.getNavPhyInvJournalCreateNamespace());
		((ObjectNode) pijcp).put("methodName", navProperties.getNavPhyInvJournalCreateMethodname());
		((ObjectNode) pijcp).put("soapAction", navProperties.getNavPhyInvJournalCreateSoapaction());
		((ObjectNode) pijcp).put("soapUrl", navProperties.getNavPhyInvJournalCreateSoapurl());
		((ObjectNode) rootNode).set("phyInvJournalCreateProps", pijcp);

		// Phy Journal read update
		JsonNode pijup = mapper.createObjectNode();
		((ObjectNode) pijup).put("nameSpace", navProperties.getNavPhyInvJournalUpdateNamespace());
		((ObjectNode) pijup).put("methodName", navProperties.getNavPhyInvJournalUpdateMethodname());
		((ObjectNode) pijup).put("soapAction", navProperties.getNavPhyInvJournalUpdateSoapaction());
		((ObjectNode) pijup).put("soapUrl", navProperties.getNavPhyInvJournalUpdateSoapurl());
		((ObjectNode) rootNode).set("phyInvJournalUpdateProps", pijup);

		// Purchase order list
		JsonNode purchaseOrdersList = mapper.createObjectNode();
		((ObjectNode) purchaseOrdersList).put("nameSpace", navProperties.getNavPurchaseOrderListNamespace());
		((ObjectNode) purchaseOrdersList).put("methodName", navProperties.getNavPurchaseOrderListMethodName());
		((ObjectNode) purchaseOrdersList).put("soapAction", navProperties.getNavPurchaseOrderListSoapAction());
		((ObjectNode) purchaseOrdersList).put("soapUrl", navProperties.getNavPurchaseOrderListSoapURL());
		((ObjectNode) rootNode).set("purchaseOrdersListProps", purchaseOrdersList);

		// Purchase order lines Read Multiple
		JsonNode polrmp = mapper.createObjectNode();
		((ObjectNode) polrmp).put("nameSpace", navProperties.getNavPurchaseOrderLinesReadMultipleNamespace());
		((ObjectNode) polrmp).put("methodName", navProperties.getNavPurchaseOrderLinesReadMultipleMethodName());
		((ObjectNode) polrmp).put("soapAction", navProperties.getNavPurchaseOrderLinesReadMultipleSoapAction());
		((ObjectNode) polrmp).put("soapUrl", navProperties.getNavPurchaseOrderLinesReadMultipleSoapURL());
		((ObjectNode) rootNode).set("purchaseOrderLinesReadMultipleProps", polrmp);

		// Purchase order lines Read
		JsonNode polrp = mapper.createObjectNode();
		((ObjectNode) polrp).put("nameSpace", navProperties.getNavPurchaseOrderLinesReadNamespace());
		((ObjectNode) polrp).put("methodName", navProperties.getNavPurchaseOrderLinesReadMethodName());
		((ObjectNode) polrp).put("soapAction", navProperties.getNavPurchaseOrderLinesReadSoapAction());
		((ObjectNode) polrp).put("soapUrl", navProperties.getNavPurchaseOrderLinesReadSoapURL());
		((ObjectNode) rootNode).set("purchaseOrderLinesReadProps", polrp);

		// Purchase order lines Update
		JsonNode polup = mapper.createObjectNode();
		((ObjectNode) polup).put("nameSpace", navProperties.getNavPurchaseOrderLinesUpdateNamespace());
		((ObjectNode) polup).put("methodName", navProperties.getNavPurchaseOrderLinesUpdateMethodName());
		((ObjectNode) polup).put("soapAction", navProperties.getNavPurchaseOrderLinesUpdateSoapAction());
		((ObjectNode) polup).put("soapUrl", navProperties.getNavPurchaseOrderLinesUpdateSoapURL());
		((ObjectNode) rootNode).set("purchaseOrderLinesUpdateProps", polup);

		// Stock issue
		JsonNode stockIssueList = mapper.createObjectNode();
		((ObjectNode) stockIssueList).put("nameSpace", navProperties.getNavStockIssueListNamespace());
		((ObjectNode) stockIssueList).put("methodName", navProperties.getNavStockIssueListMethodName());
		((ObjectNode) stockIssueList).put("soapAction", navProperties.getNavStockIssueListSoapAction());
		((ObjectNode) stockIssueList).put("soapUrl", navProperties.getNavStockIssueListSoapURL());
		((ObjectNode) rootNode).set("stockIssueListProps", stockIssueList);

		// Comapny list
		JsonNode companyList = mapper.createObjectNode();
		((ObjectNode) companyList).put("nameSpace", navProperties.getNavCompanyListNamespace());
		((ObjectNode) companyList).put("methodName", navProperties.getNavCompanyListMethodName());
		((ObjectNode) companyList).put("soapAction", navProperties.getNavCompanyListSoapAction());
		((ObjectNode) companyList).put("soapUrl", navProperties.getNavCompanyListSoapURL());
		((ObjectNode) rootNode).set("stockIssueListProps", companyList);

		// User list
		JsonNode userList = mapper.createObjectNode();
		((ObjectNode) userList).put("nameSpace", navProperties.getNavUserListNamespace());
		((ObjectNode) userList).put("methodName", navProperties.getNavUserListMethodName());
		((ObjectNode) userList).put("soapAction", navProperties.getNavUserListSoapAction());
		((ObjectNode) userList).put("soapUrl", navProperties.getNavUserListSoapURL());
		((ObjectNode) rootNode).set("userListProps", userList);

		String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);

		logger.info("json converted " + jsonString);
		return jsonString;
	}

	@ApiOperation(value = "Refresh unit of measure list", produces = "application/json")
	@GetMapping("/refreshUnitOfMeasureList")
	@ResponseBody
	public String refreshUOMList(@RequestHeader("id") String sessionId) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		logger.info("Refreshing UOM list .. ");
		JsonNode rootNode = mapper.createObjectNode();
		((ObjectNode) rootNode).put("message", navUnitOfMeasureService.getUOMList());

		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
	}

	@ApiOperation(value = "Refresh vendor list", produces = "application/json")
	@GetMapping("/refreshVendorList")
	@ResponseBody
	public String refreshVendorList(@RequestHeader("id") String sessionId) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		logger.info("Refreshing vendor list .. ");
		JsonNode rootNode = mapper.createObjectNode();
		((ObjectNode) rootNode).put("message", navVendorListService.getVendorList());

		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
	}

	@ApiOperation(value = "Refresh item list", produces = "application/json")
	@GetMapping("/refreshItemList")
	@ResponseBody
	public String refreshItemList(@RequestHeader("id") String sessionId) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		logger.info("Refreshing item list .. ");
		JsonNode rootNode = mapper.createObjectNode();
		((ObjectNode) rootNode).put("message", navItemListService.getItemList());

		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
	}

	@ApiOperation(value = "Refresh location and template list", produces = "application/json")
	@GetMapping("/refreshLocationList")
	@ResponseBody
	public String refreshLocationList(@RequestHeader("id") String sessionId) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		logger.info("Refreshing location list .. ");
		JsonNode rootNode = mapper.createObjectNode();
		((ObjectNode) rootNode).put("message", navLocationService.getLocationList());

		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
	}

	@ApiOperation(value = "Refresh journal list", produces = "application/json")
	@GetMapping("/refreshJournalList")
	@ResponseBody
	public String refreshJournalList(@RequestHeader("id") String sessionId) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		logger.info("Refreshing journal list .. ");
		JsonNode rootNode = mapper.createObjectNode();
		((ObjectNode) rootNode).put("message", navPhyInvJournalService.getJournals());

		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
	}

	@ApiOperation(value = "Refresh phy journal item list", produces = "application/json")
	@GetMapping("/refreshJrnlItemList")
	@ResponseBody
	public String refreshJtnlItemList(@RequestHeader("id") String sessionId) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		logger.info("Refreshing journal item list .. ");
		JsonNode rootNode = mapper.createObjectNode();
		((ObjectNode) rootNode).put("message", navPhyInvJournalService.getJournalItems());

		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
	}

	@ApiOperation(value = "Refresh phy journal items for key", produces = "application/json")
	@GetMapping("/refreshJrnlItemListForKey")
	@ResponseBody
	public String refreshJrnlItemListForKey(@RequestHeader("id") String sessionId) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		logger.info("Refreshing journal item list for key .. ");
		JsonNode rootNode = mapper.createObjectNode();
		((ObjectNode) rootNode).put("message", navPhyInvJournalService.getJournalItemKeys());

		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
	}

	@ApiOperation(value = "Refresh purchase order details", produces = "application/json")
	@GetMapping("/refreshPurchaseOrderList")
	@ResponseBody
	public String refreshPurchaseOrderList(@RequestHeader("id") String sessionId) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		logger.info("Refreshing purchase order list .. ");
		JsonNode rootNode = mapper.createObjectNode();
		((ObjectNode) rootNode).put("message", navPurchaseOrderService.getPurchaseOrderList());

		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
	}

	@ApiOperation(value = "Refresh purchase order lines details", produces = "application/json")
	@GetMapping("/refreshPurchaseOrderLinesList")
	@ResponseBody
	public String refreshPurchaseOrderLineList(@RequestHeader("id") String sessionId) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		logger.info("Refreshing purchase order lines list .. ");
		JsonNode rootNode = mapper.createObjectNode();
		((ObjectNode) rootNode).put("message", navPurchaseOrderLinesService.getPurchaseOrderLinesList());

		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
	}

	@ApiOperation(value = "Refresh POLines keys", produces = "application/json")
	@GetMapping("/refreshPOLineKeys")
	@ResponseBody
	public String refreshPOLineKeys(@RequestHeader("id") String sessionId) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		logger.info("Refreshing POLines keys .. ");
		JsonNode rootNode = mapper.createObjectNode();
		((ObjectNode) rootNode).put("message", navPurchaseOrderLinesService.getPurchaseOrderLinesKeys());

		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
	}

	@ApiOperation(value = "Refresh stock issue list", produces = "application/json")
	@GetMapping("/refreshStockIssueList")
	@ResponseBody
	public String refreshStockIssueList(@RequestHeader("id") String sessionId) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		logger.info("Refreshing stock issue list .. ");
		JsonNode rootNode = mapper.createObjectNode();
		((ObjectNode) rootNode).put("message", navStockIssueService.getStockIssueList());

		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
	}

	@ApiOperation(value = "Refresh stock issue lines", produces = "application/json")
	@GetMapping("/refreshStockIssueLines")
	@ResponseBody
	public String refreshStockIssueLines(@RequestHeader("id") String sessionId) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		logger.info("Refreshing stock issue lines .. ");
		JsonNode rootNode = mapper.createObjectNode();
		((ObjectNode) rootNode).put("message", navStockIssueLinesService.getStockIssueLines());

		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
	}

	@ApiOperation(value = "Refresh stock issue line Keys", produces = "application/json")
	@GetMapping("/refreshStockIssueLineKeys")
	@ResponseBody
	public String refreshStockIssueLineKeys(@RequestHeader("id") String sessionId) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		logger.info("Refreshing stock issue line keys .. ");
		JsonNode rootNode = mapper.createObjectNode();
		((ObjectNode) rootNode).put("message", navStockIssueLinesService.getStockIssueLineKeys());

		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
	}

	@ApiOperation(value = "Refresh company list", produces = "application/json")
	@GetMapping("/refreshCompanyList")
	@ResponseBody
	public String refreshCompanyList(@RequestHeader("id") String sessionId) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		logger.info("Refreshing Company list .. ");
		JsonNode rootNode = mapper.createObjectNode();
		((ObjectNode) rootNode).put("message", navCompanyService.getCompanyList());

		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
	}

	@ApiOperation(value = "Refresh user list", produces = "application/json")
	@GetMapping("/refreshUserList")
	@ResponseBody
	public String refreshUserList(@RequestHeader("id") String sessionId) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		logger.info("Refreshing user list .. ");
		JsonNode rootNode = mapper.createObjectNode();
		((ObjectNode) rootNode).put("message", navUserService.fetchUsers());

		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
	}
}
