package com.techmeridian.stockmanagement.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.techmeridian.stockmanagement.scheduler.NavScheduler;

public class AppContextListener implements ServletContextListener {

	private NavScheduler navScheduler;

	@Override
	public void contextInitialized(ServletContextEvent sce) {

		WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
		this.navScheduler = (NavScheduler) ctx.getBean("NavScheduler");

		navScheduler.start();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		navScheduler.stop();
	}

}
