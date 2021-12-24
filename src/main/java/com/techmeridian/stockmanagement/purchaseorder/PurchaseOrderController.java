package com.techmeridian.stockmanagement.purchaseorder;

import java.net.URLDecoder;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techmeridian.stockmanagement.journal.AddItemReq;
import com.techmeridian.stockmanagement.session.SessionUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/po")
@Api(value = "Pucrhase Order Controller", description = "Contains operations related to a Purchase Order")
public class PurchaseOrderController {

	private Logger logger = Logger.getLogger(PurchaseOrderController.class);

	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private PurchaseOrderService purchaseOrderService;

	@ApiOperation(value = "Get list of puchase order", produces = "application/json")
	@GetMapping("/list")
	@ResponseBody
	public String getPurchaseOrders(@RequestHeader("id") String sessionId, @RequestHeader("cid") Integer cid)
			throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		List<PurchaseOrder> purchaseOrders = purchaseOrderService.getPurchaseOrders(cid);
		logger.debug("selected " + purchaseOrders);
		String jsonString = mapper.writeValueAsString(purchaseOrders);
		logger.info("json converted " + jsonString);
		return jsonString;
	}

	@ApiOperation(value = "Get a puchase order by po number", produces = "application/json")
	@GetMapping("/get/number/{poNo}")
	@ResponseBody
	public String getPurchaseOrderByNumber(@RequestHeader("id") String sessionId, @RequestHeader("cid") Integer cid,
			@PathVariable("poNo") String poNo) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		PurchaseOrder purchaseOrder = purchaseOrderService.getPurchaseOrder(cid, URLDecoder.decode(poNo, "UTF-8"));
		logger.debug("selected " + purchaseOrder);
		String jsonString = mapper.writeValueAsString(purchaseOrder);
		logger.info("json converted " + jsonString);
		return jsonString;
	}

	@ApiOperation(value = "Get a puchase order by po id", produces = "application/json")
	@GetMapping("/get/{poId}")
	@ResponseBody
	public String getPurchaseOrder(@RequestHeader("id") String sessionId, @RequestHeader("cid") Integer cid,
			@PathVariable("poId") Integer poId) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		PurchaseOrder purchaseOrder = purchaseOrderService.getPurchaseOrder(cid, poId);
		logger.debug("selected " + purchaseOrder);
		String jsonString = mapper.writeValueAsString(purchaseOrder);
		logger.info("json converted " + jsonString);
		return jsonString;
	}

	@ApiOperation(value = "Fetch puchase order items by po number", produces = "application/json")
	@GetMapping("/items/{poNo}/number")
	@ResponseBody
	public String getItemsInPurchaseOrderByNumber(@RequestHeader("id") String sessionId,
			@RequestHeader("cid") Integer cid, @PathVariable("poNo") String poNo) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		List<PurchaseOrderLines> purchaseOrderLines = purchaseOrderService.getPurchaseOrderItems(cid,
				URLDecoder.decode(poNo, "UTF-8"));
		logger.debug("selected " + purchaseOrderLines);
		String jsonString = mapper.writeValueAsString(purchaseOrderLines);
		logger.info("json converted " + jsonString);
		return jsonString;
	}

	@ApiOperation(value = "Fetch puchase order items by po id", produces = "application/json")
	@GetMapping("/items/{poId}/id")
	@ResponseBody
	public String getItemsInPurchaseOrder(@RequestHeader("id") String sessionId, @RequestHeader("cid") Integer cid,
			@PathVariable("poId") Integer poId) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		List<PurchaseOrderLines> purchaseOrderLines = purchaseOrderService.getPurchaseOrderItems(cid, poId);
		logger.debug("selected " + purchaseOrderLines);
		String jsonString = mapper.writeValueAsString(purchaseOrderLines);
		logger.info("json converted " + jsonString);
		return jsonString;
	}

	@ApiOperation(value = "Add items to purchase order", produces = "application/json")
	@PostMapping("/items")
	@ResponseBody
	public String addItemToPurchaseOrder(@RequestHeader("id") String sessionId, @RequestHeader("cid") Integer cid,
			@RequestBody AddItemReq addItemReq) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		if (addItemReq == null || StringUtils.isEmpty(addItemReq.getItem())
				|| StringUtils.isEmpty(addItemReq.getPurchaseOrderId())) {
			throw new Exception("Mandatory fields are missing, please give proper input.");
		}
		List<PurchaseOrderLines> itemsInPurchaseOrder = purchaseOrderService.addItems(cid, addItemReq,
				SessionUtils.getInstance().getUser(sessionId));
		logger.debug("selected " + itemsInPurchaseOrder);
		String jsonString = mapper.writeValueAsString(itemsInPurchaseOrder);
		logger.info("json converted " + jsonString);
		return jsonString;
	}

	@ApiOperation(value = "Push to Nav", consumes = "application/json", produces = "application/json")
	@PostMapping("/push")
	@ResponseBody
	public String pushToNav(@RequestHeader("id") String sessionId, @RequestHeader("cid") Integer cid,
			@RequestBody PurchaseOrder purchaseOrder) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		logger.info("input " + purchaseOrder);
		if (purchaseOrder != null && purchaseOrder.getPurchaseOrderId() != null) {
			String resp = purchaseOrderService.pushToNav(cid, purchaseOrder,
					SessionUtils.getInstance().getUser(sessionId));
			return "{\"status\":\"" + resp + "\"}";
		} else {
			throw new Exception("Mandatory fields may be missing, please give proper input.");
		}
	}

	@ApiOperation(value = "reload purchase order lines from NAV by po id", produces = "application/json")
	@GetMapping("/reload/{poId}/id")
	@ResponseBody
	public String reloadPurchaseOrderLines(@RequestHeader("id") String sessionId, @RequestHeader("cid") Integer cid,
			@PathVariable("poId") Integer poId) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		List<PurchaseOrderLines> purchaseOrderLines = purchaseOrderService.reloadPurchaseOrderAndReturnItems(cid, poId);
		logger.debug("selected " + purchaseOrderLines);
		String jsonString = mapper.writeValueAsString(purchaseOrderLines);
		logger.info("json converted " + jsonString);
		return jsonString;
	}

	@ApiOperation(value = "reload purchase order lines from NAV by po number", produces = "application/json")
	@GetMapping("/reload/{poNo}/number")
	@ResponseBody
	public String reloadPurchaseOrderLines(@RequestHeader("id") String sessionId, @RequestHeader("cid") Integer cid,
			@PathVariable("poNo") String poNo) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		List<PurchaseOrderLines> purchaseOrderLines = purchaseOrderService.reloadPurchaseOrderAndReturnItems(cid,
				URLDecoder.decode(poNo, "UTF-8"));
		logger.debug("selected " + purchaseOrderLines);
		String jsonString = mapper.writeValueAsString(purchaseOrderLines);
		logger.info("json converted " + jsonString);
		return jsonString;
	}
}
