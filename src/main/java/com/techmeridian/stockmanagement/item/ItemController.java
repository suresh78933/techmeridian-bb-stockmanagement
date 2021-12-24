package com.techmeridian.stockmanagement.item;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techmeridian.stockmanagement.session.SessionUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/item")
@Api(value = "Item Controller", description = "Contains operations related to a item")
public class ItemController {

	private Logger logger = Logger.getLogger(ItemController.class);

	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private ItemService itemService;

	@ApiOperation(value = "Get list of items", produces = "application/json")
	@GetMapping("/list")
	@ResponseBody
	public String getItemList(@RequestHeader("id") String sessionId) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		List<Item> items = itemService.getItems();
		logger.debug("selected " + items);
		String jsonString = mapper.writeValueAsString(items);
		logger.info("json converted " + jsonString);
		return jsonString;
	}

	@ApiOperation(value = "Get list of items for a company", produces = "application/json")
	@GetMapping("/list/company")
	@ResponseBody
	public String getItemListForACompany(@RequestHeader("id") String sessionId, @RequestHeader("cid") Integer cid)
			throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		List<Item> items = itemService.getItemsByCompany(cid);
		logger.debug("selected " + items);
		String jsonString = mapper.writeValueAsString(items);
		logger.info("json converted " + jsonString);
		return jsonString;
	}

	@ApiOperation(value = "Reload Item from NAV by item no", produces = "application/json")
	@GetMapping("/reload/{itNo}/no")
	@ResponseBody
	public String reloadItem(@RequestHeader("id") String sessionId, @RequestHeader("cid") Integer cid,
			@PathVariable("itNo") String itNo) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		Item item = itemService.reloadItem(cid, itNo);
		logger.debug("selected " + item);
		String jsonString = mapper.writeValueAsString(item);
		logger.info("json converted " + jsonString);
		return jsonString;
	}

	@ApiOperation(value = "Reload Item from NAV by item id", produces = "application/json")
	@GetMapping("/reload/{itId}/id")
	@ResponseBody
	public String reloadItem(@RequestHeader("id") String sessionId, @RequestHeader("cid") Integer cid,
			@PathVariable("itId") Integer itId) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		Item item = itemService.reloadItem(cid, itId);
		logger.debug("selected " + item);
		String jsonString = mapper.writeValueAsString(item);
		logger.info("json converted " + jsonString);
		return jsonString;
	}

	@ApiOperation(value = "Fetch item by item number or part no", produces = "application/json")
	@GetMapping("/get/{itNo}/no")
	@ResponseBody
	public String getItemByNo(@RequestHeader("id") String sessionId, @RequestHeader("cid") Integer cid,
			@PathVariable("itNo") String itNo) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		Item item = itemService.getItemByNoOrPartNo(cid, itNo);
		logger.debug("selected " + item);
		String jsonString = mapper.writeValueAsString(item);
		if (jsonString == null || "".equals(jsonString) || "null".equals(jsonString)) {
			jsonString = "{}";
		}
		logger.info("json converted " + jsonString);
		return jsonString;
	}

}
