package com.techmeridian.stockmanagement.stockissue;

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
@RequestMapping("/si")
@Api(value = "Stock Issue Controller", description = "Contains operations related to a Stock Issue")
public class StockIssueController {

	private Logger logger = Logger.getLogger(StockIssueController.class);

	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private StockIssueService stockIssueService;

	@ApiOperation(value = "Get list of stock issues", produces = "application/json")
	@GetMapping("/list")
	@ResponseBody
	public String getStockIssuings(@RequestHeader("id") String sessionId, @RequestHeader("cid") Integer cid)
			throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		List<StockIssue> stockIssues = stockIssueService.getStockIssues(cid);
		logger.debug("selected " + stockIssues);
		String jsonString = mapper.writeValueAsString(stockIssues);
		logger.info("json converted " + jsonString);
		return jsonString;
	}

	@ApiOperation(value = "Get a stock issue by stock issue number", produces = "application/json")
	@GetMapping("/get/number/{siNo}")
	@ResponseBody
	public String getStockIssueByNumber(@RequestHeader("id") String sessionId, @RequestHeader("cid") Integer cid,
			@PathVariable("siNo") String siNo) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		StockIssue stockIssue = stockIssueService.getStockIssue(cid, URLDecoder.decode(siNo, "UTF-8"));
		logger.debug("selected " + stockIssue);
		String jsonString = mapper.writeValueAsString(stockIssue);
		logger.info("json converted " + jsonString);
		return jsonString;
	}

	@ApiOperation(value = "Get a stock issue by stock issue id", produces = "application/json")
	@GetMapping("/get/id/{siId}")
	@ResponseBody
	public String getStockIssueById(@RequestHeader("id") String sessionId, @RequestHeader("cid") Integer cid,
			@PathVariable("siId") Integer siId) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		StockIssue stockIssue = stockIssueService.getStockIssue(cid, siId);
		logger.debug("selected " + stockIssue);
		String jsonString = mapper.writeValueAsString(stockIssue);
		logger.info("json converted " + jsonString);
		return jsonString;
	}

	@ApiOperation(value = "Fetch stock issue items by si number", produces = "application/json")
	@GetMapping("/items/number/{siNo}")
	@ResponseBody
	public String getItemsInStockIssueByNumber(@RequestHeader("id") String sessionId, @RequestHeader("cid") Integer cid,
			@PathVariable("siNo") String siNo) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		List<StockIssueLines> stockIssueLines = stockIssueService.getStockIssueLines(cid,
				URLDecoder.decode(siNo, "UTF-8"));
		logger.debug("selected " + stockIssueLines);
		String jsonString = mapper.writeValueAsString(stockIssueLines);
		logger.info("json converted " + jsonString);
		return jsonString;
	}

	@ApiOperation(value = "Fetch stock issue items by si number", produces = "application/json")
	@GetMapping("/items/id/{siId}")
	@ResponseBody
	public String getItemsInStockIssueById(@RequestHeader("id") String sessionId, @RequestHeader("cid") Integer cid,
			@PathVariable("siId") Integer siId) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		List<StockIssueLines> stockIssueLines = stockIssueService.getStockIssueLines(cid, siId);
		logger.debug("selected " + stockIssueLines);
		String jsonString = mapper.writeValueAsString(stockIssueLines);
		logger.info("json converted " + jsonString);
		return jsonString;
	}

	@ApiOperation(value = "Add items to stock issue", produces = "application/json")
	@PostMapping("/items")
	@ResponseBody
	public String addItemToStockIssue(@RequestHeader("id") String sessionId, @RequestHeader("cid") Integer cid,
			@RequestBody AddItemReq addItemReq) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		if (addItemReq == null || StringUtils.isEmpty(addItemReq.getItem())
				|| StringUtils.isEmpty(addItemReq.getStockIssueId())) {
			throw new Exception("Mandatory fields are missing, please give proper input.");
		}
		List<StockIssueLines> stockIssueLines = stockIssueService.addItems(cid, addItemReq,
				SessionUtils.getInstance().getUser(sessionId));
		logger.debug("selected " + stockIssueLines);
		String jsonString = mapper.writeValueAsString(stockIssueLines);
		logger.info("json converted " + jsonString);
		return jsonString;
	}

	@ApiOperation(value = "Push to Nav", consumes = "application/json", produces = "application/json")
	@PostMapping("/push")
	@ResponseBody
	public String pushToNav(@RequestHeader("id") String sessionId, @RequestHeader("cid") Integer cid,
			@RequestBody StockIssue stockIssue) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		logger.info("input " + stockIssue);
		if (stockIssue != null && stockIssue.getStockIssueId() != null) {
			String resp = stockIssueService.pushToNav(cid, stockIssue, SessionUtils.getInstance().getUser(sessionId));
			return "{\"status\":\"" + resp + "\"}";
		} else {
			throw new Exception("Mandatory fields may be missing, please give proper input.");
		}
	}

	@ApiOperation(value = "reload stock issue lines from NAV by si id", produces = "application/json")
	@GetMapping("/reload/{siId}/id")
	@ResponseBody
	public String reloadStockIssueLines(@RequestHeader("id") String sessionId, @RequestHeader("cid") Integer cid,
			@PathVariable("siId") Integer siId) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		List<StockIssueLines> stockIssueLines = stockIssueService.reloadStockIssueAndReturnItems(cid, siId);
		logger.debug("selected " + stockIssueLines);
		String jsonString = mapper.writeValueAsString(stockIssueLines);
		logger.info("json converted " + jsonString);
		return jsonString;
	}

	@ApiOperation(value = "reload stock issue lines from NAV by si number", produces = "application/json")
	@GetMapping("/reload/{siNo}/number")
	@ResponseBody
	public String reloadStockIssueLines(@RequestHeader("id") String sessionId, @RequestHeader("cid") Integer cid,
			@PathVariable("siNo") String siNo) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		List<StockIssueLines> stockIssueLines = stockIssueService.reloadStockIssueAndReturnItems(cid,
				URLDecoder.decode(siNo, "UTF-8"));
		logger.debug("selected " + stockIssueLines);
		String jsonString = mapper.writeValueAsString(stockIssueLines);
		logger.info("json converted " + jsonString);
		return jsonString;
	}
}
