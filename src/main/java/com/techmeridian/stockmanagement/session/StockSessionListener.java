package com.techmeridian.stockmanagement.session;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class StockSessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent httpSessionEvent) {
		HttpSession httpSession = httpSessionEvent.getSession();
		SESSIONS.put(httpSession.getId(), httpSession);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
		HttpSession httpSession = httpSessionEvent.getSession();
		SESSIONS.remove(httpSession.getId());
	}

	
	  public static void addSession(HttpSession httpSession){
	  SESSIONS.put(httpSession.getId(), httpSession); 
	  }
	 

	private static Map<String, HttpSession> SESSIONS = new HashMap<String, HttpSession>();

	public static HttpSession getSession(String sessionId) {
		return SESSIONS.get(sessionId);
	}

	public static boolean contains(String sessionId) {
		return SESSIONS.containsKey(sessionId);
	}

	public static void removeSession(String sessionId) {
		if (SESSIONS.containsKey(sessionId)) {
			HttpSession httpSession = SESSIONS.get(sessionId);
			if (httpSession != null) {
				httpSession.invalidate();
				if (SESSIONS.containsKey(sessionId)) {
					SESSIONS.remove(sessionId);
				}
			}
		}
	}
}
