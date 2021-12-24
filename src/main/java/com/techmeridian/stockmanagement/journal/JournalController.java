package com.techmeridian.stockmanagement.journal;

import java.net.URLDecoder;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techmeridian.stockmanagement.session.SessionUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/journal")
@Api(value = "Journal Controller", description = "Contains operations related to a journal")
public class JournalController {

	private Logger logger = Logger.getLogger(JournalController.class);

	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private JournalService journalService;

	@ApiOperation(value = "Get journal info", produces = "application/json")
	@GetMapping("/get/{journalId}")
	@ResponseBody
	public String getJournalInfo(@RequestHeader("id") String sessionId, @RequestHeader("cid") Integer cid,
			@PathVariable("journalId") Integer journalId) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		PhyJournal journal = journalService.getPhyJournal(cid, journalId);
		logger.debug("selected " + journal);
		String jsonString = mapper.writeValueAsString(journal);
		logger.info("json converted " + jsonString);
		return jsonString;
	}

	@ApiOperation(value = "Get list of journals for a ware house", produces = "application/json")
	@GetMapping("/list/{wareHouseCode}")
	@ResponseBody
	public String getJournalsForWareHouse(@RequestHeader("id") String sessionId, @RequestHeader("cid") Integer cid,
			@PathVariable("wareHouseCode") String code) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		logger.info("input " + code);
		List<PhyJournal> journals = journalService.getJournalForAWareHouse(cid, URLDecoder.decode(code, "UTF-8"));
		logger.debug("selected " + journals);
		String jsonString = mapper.writeValueAsString(journals);
		logger.info("json converted " + jsonString);
		return jsonString;
	}

	@ApiOperation(value = "Add items to journal, item may be item number or part number", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Items list in this journal", response = List.class) })
	@PostMapping("/additem")
	@ResponseBody
	public String addItemsToJournal(@RequestHeader("id") String sessionId, @RequestHeader("cid") Integer cid,
			@RequestBody AddItemReq addItemReq) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		logger.info("input " + addItemReq);
		if (addItemReq != null && addItemReq.getItem() != null && addItemReq.getJournalId() != null
				&& addItemReq.getJournalId() > 0) {
			List<PhyJournalItem> journalItems = journalService.addItem(cid, addItemReq,
					SessionUtils.getInstance().getUser(sessionId));
			logger.debug("returning " + journalItems);
			String jsonString = mapper.writeValueAsString(journalItems);
			logger.info("json converted " + jsonString);
			return jsonString;
		} else {
			throw new Exception("Mandatory fields are missing, please give proper input.");
		}
	}

	@ApiOperation(value = "Push to Nav", consumes = "application/json", produces = "application/json")
	@PostMapping("/push")
	@ResponseBody
	public String pushToNav(@RequestHeader("id") String sessionId, @RequestHeader("cid") Integer cid,
			@RequestBody PhyJournal journal) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		logger.info("input " + journal);
		if (journal != null && journal.getPhyJournalId() != null) {
			String resp = journalService.pushToNav(cid, journal, SessionUtils.getInstance().getUser(sessionId));
			return "{\"status\":\"" + resp + "\"}";
		} else {
			throw new Exception("Mandatory fields may be missing, please give proper input.");
		}
	}

	@ApiOperation(value = "Get Item list for a journal", produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "List in this JournalItems", response = List.class) })
	@GetMapping("/{journalId}/itemlist")
	@ResponseBody
	public String getJournals(@RequestHeader("id") String sessionId, @RequestHeader("cid") Integer cid,
			@PathVariable("journalId") Integer journalId) throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		List<PhyJournalItem> journalItems = journalService.getPhyJournalItems(cid, journalId);
		logger.debug("selected " + journalItems);
		String jsonString = mapper.writeValueAsString(journalItems);
		logger.info("json converted " + jsonString);
		return jsonString;
	}
}
