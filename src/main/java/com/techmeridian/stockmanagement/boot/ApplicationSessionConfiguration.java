package com.techmeridian.stockmanagement.boot;

import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionListener;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.techmeridian.stockmanagement.context.AppContextListener;
import com.techmeridian.stockmanagement.session.StockSessionListener;

@Configuration
public class ApplicationSessionConfiguration {
	@Bean
	public ServletListenerRegistrationBean<HttpSessionListener> sessionListener() {
		return new ServletListenerRegistrationBean<HttpSessionListener>(new StockSessionListener());
	}

	@Bean
	public ServletListenerRegistrationBean<ServletContextListener> contextListener() {
		return new ServletListenerRegistrationBean<ServletContextListener>(new AppContextListener());
	}
}