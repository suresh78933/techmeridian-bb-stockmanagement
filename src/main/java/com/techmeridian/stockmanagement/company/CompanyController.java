package com.techmeridian.stockmanagement.company;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techmeridian.stockmanagement.session.SessionUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/company")
@Api(value = "Company Controller", description = "Contains operations related to a company")
public class CompanyController {

	private Logger logger = Logger.getLogger(CompanyController.class);

	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private HttpServletRequest request;

	@ApiOperation(value = "Get list of companies", produces = "application/json")
	@GetMapping("/list")
	@ResponseBody
	public String getCompanies(@RequestHeader("id") String sessionId) throws Exception {		
		SessionUtils.getInstance().isSessionValid(sessionId);
		List<Company> companies = companyService.getCompanies();
		logger.debug("selected " + companies);
		String jsonString = mapper.writeValueAsString(companies);
		logger.info("json converted " + jsonString);
		return jsonString;
	}
}
