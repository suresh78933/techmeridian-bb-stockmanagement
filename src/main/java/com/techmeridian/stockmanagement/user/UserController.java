package com.techmeridian.stockmanagement.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/user")
@Api(value = "User Controller", description = "Contains operations related to user")
public class UserController {

	private Logger logger = Logger.getLogger(UserController.class);

	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private HttpServletResponse response;

	@Autowired
	private UserService userService;

	@Autowired
	private HttpServletRequest request;

	@ApiOperation(value = "Authenticate user", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "user details", response = User.class) })
	@PostMapping("/authenticate")
	@ResponseBody
	public String authenticate(@RequestBody User user) throws Exception {
		logger.info("input " + user);
		User authUser = userService.authenticate(user);
		if (authUser != null) {
			HttpSession httpSession = request.getSession();
			httpSession.setAttribute("user", authUser);
			response.setHeader("id", httpSession.getId());

			// TODO: this is just for testing
			// StockSessionListener.addSession(httpSession);
		}

		logger.debug("returning " + authUser);
		String jsonString = mapper.writeValueAsString(authUser);
		logger.info("json converted " + jsonString);
		return jsonString;
	}

	@ApiOperation(value = "Authenticate user", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "user details", response = User.class) })
	@GetMapping("/logout")
	@ResponseBody
	public String logout(@RequestHeader("id") String sessionId) throws Exception {
		SessionUtils.getInstance().logout(sessionId);
		return "{\"message\":\"OK\"}";
	}

	@ApiOperation(value = "Change password", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Change password", response = User.class) })
	@PostMapping("/cp")
	@ResponseBody
	public String changePassword(@RequestHeader("id") String sessionId, @RequestBody User user) throws Exception {
		logger.info("cp input " + user);
		SessionUtils.getInstance().isSessionValid(sessionId);
		String message = userService.changePassword(user, sessionId);
		return "{\"message\":\"" + message + "\"}";
	}

	@ApiOperation(value = "Reset password", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Reset password", response = User.class) })
	@PostMapping("/rp")
	@ResponseBody
	public String resetPassword(@RequestBody User user) throws Exception {
		logger.info("reset input " + user);
		String message = userService.resetPassword(user);
		return "{\"message\":\"" + message + "\"}";
	}
}
