package com.techmeridian.stockmanagement.nav.service;

import org.ksoap2.serialization.SoapObject;

public class ServiceUtil {

	public static String getValue(String key, SoapObject soapObject) {
		try {
			return soapObject.getProperty(key).toString();
		} catch (Exception exception) {
			return "";
		}
	}

	public static Integer getIntValue(String key, SoapObject soapObject) {
		try {
			String value = soapObject.getProperty(key).toString();
			return Integer.parseInt(value);
		} catch (Exception exception) {
			return 0;
		}
	}
}
