package com.techmeridian.stockmanagement.nav.service;

import java.net.URLEncoder;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

public final class NavServiceUtil {

	public final SoapObject getDataFromNav(String namespace, String url, String soap_action, String method_name,
			String userName, String password, String domain, String workstation) throws Exception {
		SoapObject request = new SoapObject(namespace, method_name);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);

		NtlmTransport ntlm = new NtlmTransport();
		ntlm.debug = false;
		userName = URLEncoder.encode(userName, "UTF-8");
		ntlm.setCredentials(url, userName, password, domain, workstation);
		ntlm.call(soap_action, envelope);

		if (envelope.bodyIn != null) {
			return (SoapObject) envelope.getResponse();
		}

		if (envelope.bodyIn instanceof SoapFault) {
			throw new Exception(((SoapFault) envelope.bodyIn).faultstring);
		} else {
			SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
			return resultsRequestSOAP;
		}
	}

	public static NavServiceUtil getInstance() {
		if (navService == null) {
			navService = new NavServiceUtil();
		}
		return navService;
	}

	private static NavServiceUtil navService;

	private NavServiceUtil() {
	}
}
