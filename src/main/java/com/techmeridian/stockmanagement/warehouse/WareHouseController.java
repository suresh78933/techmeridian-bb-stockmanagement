package com.techmeridian.stockmanagement.warehouse;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techmeridian.stockmanagement.session.SessionUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/wr")
@Api(value = "Warehouse Controller", description = "Contains operations related to a ware house")
public class WareHouseController {

	private Logger logger = Logger.getLogger(WareHouseController.class);

	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private WareHouseService wareHouseService;

	@ApiOperation(value = "Get list of ware houses for a company", produces = "application/json")
	@GetMapping("/list")
	@ResponseBody
	public String getWareHouses(@RequestHeader("id") String sessionId, @RequestHeader("cid") Integer cid)
			throws Exception {
		SessionUtils.getInstance().isSessionValid(sessionId);
		List<WareHouse> houses = wareHouseService.getWareHousesForCompany(cid);
		logger.debug(houses);
		String jsonString = mapper.writeValueAsString(houses);
		logger.debug(jsonString);
		return jsonString;
	}
}
