package com.techmeridian.stockmanagement.soap;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class SoapUtils {

	public static final String SOAP_ACTION = "SOAPAction";

	public static final String CONTENT_TYPE = "Content-Type";

	public static final String ACCEPT_VALUE = "application/xml; charset=utf-8";

	public static final String ACCEPT = "Accept";

	private static final String EMPTY = "";
	private static final String SPACE = " ";
	private static final String CLOSE_TAG_SYMB = ">";
	private static final String OPEN_TAG_SYMB = "<";
	private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	private static final String TAG_ENVELOP_OPEN = "<v:Envelope xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\" "
			+ "xmlns:d=\"http://www.w3.org/2001/XMLSchema\" xmlns:c=\"http://schemas.xmlsoap.org/soap/encoding/\" "
			+ "xmlns:v=\"http://schemas.xmlsoap.org/soap/envelope/\">";

	private static final String TAG_BODY_OPEN = "<v:Body>";

	private static final String TAG_BODY_CLOSE = "</v:Body>";
	private static final String TAG_ENVELOP_CLOSE = "</v:Envelope>";

	public final static String getSoapContent(List<SoapParam> soapParams, List<String> mainTags, String targetNameSpace,
			SoapRequestType soapRequestType) {
		StringBuilder soapContent = new StringBuilder(XML_HEADER);
		soapContent.append(TAG_ENVELOP_OPEN).append(TAG_BODY_OPEN)
				.append(String.format(soapRequestType.getOpenTag(), targetNameSpace));
		if (!mainTags.isEmpty()) {
			setMainTags(mainTags, soapContent, true);
		}
		for (SoapParam soapParam : soapParams) {
			// <Journal_Template_Name
			// i:type=\"d:string\">PHYS.INV.</Journal_Template_Name>"

			soapContent.append(OPEN_TAG_SYMB).append(soapParam.getSoapParamKey()).append(SPACE).append("i:type=\"d:")
					.append(soapParam.getSoapParamType()).append("\">").append(soapParam.getSoapParamValue())
					.append("</").append(soapParam.getSoapParamKey()).append(CLOSE_TAG_SYMB);

		}
		if (!mainTags.isEmpty()) {
			Collections.reverse(mainTags);
			setMainTags(mainTags, soapContent, false);
		}
		soapContent.append(soapRequestType.getCloseTag()).append(TAG_BODY_CLOSE).append(TAG_ENVELOP_CLOSE);
		return soapContent.toString();
	}

	public final static String getSoapContent(String soapBody, List<String> mainTags, String targetNameSpace,
			SoapRequestType soapRequestType) {
		StringBuilder soapContent = new StringBuilder(XML_HEADER);
		soapContent.append(TAG_ENVELOP_OPEN).append(TAG_BODY_OPEN)
				.append(String.format(soapRequestType.getOpenTag(), targetNameSpace));
		if (!mainTags.isEmpty()) {
			setMainTags(mainTags, soapContent, true);
		}
		soapContent.append(soapBody);
		if (!mainTags.isEmpty()) {
			Collections.reverse(mainTags);
			setMainTags(mainTags, soapContent, false);
		}
		soapContent.append(soapRequestType.getCloseTag()).append(TAG_BODY_CLOSE).append(TAG_ENVELOP_CLOSE);
		return soapContent.toString();
	}

	private final static void setMainTags(List<String> mainTags, StringBuilder soapContent, boolean isOpening) {
		mainTags.stream().forEach(action -> soapContent.append(OPEN_TAG_SYMB).append(isOpening ? EMPTY : "/")
				.append(action).append(isOpening ? " i:type=\"d:anyType\"" : EMPTY).append(CLOSE_TAG_SYMB));
	}

	public final static String getField(List<String> mainTags, List<SoapParam> soapParams) {
		StringBuilder fieldContent = new StringBuilder();
		if (!mainTags.isEmpty()) {
			setMainTags(mainTags, fieldContent, true);
		}
		soapParams.stream().forEach(soapParam -> {
			fieldContent.append(OPEN_TAG_SYMB).append(soapParam.getSoapParamKey()).append(SPACE).append("i:type=\"d:")
					.append(soapParam.getSoapParamType()).append("\">").append(soapParam.getSoapParamValue())
					.append("</").append(soapParam.getSoapParamKey()).append(CLOSE_TAG_SYMB);
		});
		if (!mainTags.isEmpty()) {
			Collections.reverse(mainTags);
			setMainTags(mainTags, fieldContent, false);
		}
		return fieldContent.toString();
	}

	public final static String getFilterContent(Map<String, String> fieldCritirias) {
		StringBuilder fieldContent = new StringBuilder();
		List<String> filter = Arrays.asList(new String[] { "filter" });
		setMainTags(filter, fieldContent, true);
		fieldCritirias.entrySet().stream().forEach(field -> {
			fieldContent.append(OPEN_TAG_SYMB).append(field.getKey()).append(SPACE).append("i:type=\"d:")
					.append("string").append("\">").append(field.getValue()).append("</").append(field.getKey())
					.append(CLOSE_TAG_SYMB);
		});
		setMainTags(filter, fieldContent, false);
		return fieldContent.toString();
	}

	public static SoapParam getSoapParam(String key, Object value, String type) {
		SoapParam soapParam = new SoapParam();

		soapParam.setSoapParamKey(key);
		soapParam.setSoapParamValue(value);
		soapParam.setSoapParamType(type);

		return soapParam;
	}

}
