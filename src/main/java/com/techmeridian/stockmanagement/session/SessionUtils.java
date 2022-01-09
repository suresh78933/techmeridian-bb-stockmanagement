package com.techmeridian.stockmanagement.session;

import javax.servlet.http.HttpSession;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;

import com.techmeridian.stockmanagement.user.User;

public final class SessionUtils {

	public void isSessionValid(String sessionId) throws Exception {
		String sCurrentSessionId=RequestContextHolder.currentRequestAttributes().getSessionId();
		if (StringUtils.isEmpty(sCurrentSessionId)) {
			throw new Exception("Your identity please !!");
		}
		if (!StockSessionListener.contains(sCurrentSessionId)) {
			throw new Exception("Am not sure who you are, get your identity and ping me back !!");
		}
	}

	public User getUser(String sessionId) throws Exception {
		isSessionValid(sessionId);
		HttpSession httpSession = StockSessionListener.getSession(sessionId);
		return (User) httpSession.getAttribute("user");
	}

	public void logout(String sessionId) throws Exception {
		isSessionValid(sessionId);
		StockSessionListener.removeSession(sessionId);
	}

	private static SessionUtils sessionUtils;

	private SessionUtils() {

	}

	public final static SessionUtils getInstance() {
		if (sessionUtils == null) {
			sessionUtils = new SessionUtils();
		}
		return sessionUtils;
	}

	@Override
	public Object clone() {
		return getInstance();
	}
}
