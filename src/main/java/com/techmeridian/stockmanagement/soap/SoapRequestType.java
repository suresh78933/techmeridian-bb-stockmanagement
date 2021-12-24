package com.techmeridian.stockmanagement.soap;

public enum SoapRequestType {

	//
	CREATE("<Create xmlns=\"%s\">", "</Create>"),
	//
	CREAT_MULTIPLE("<CreateMultiple xmlns=\"%s\">", "</CreateMultiple>"),
	//
	UPDATE("<Update xmlns=\"%s\">", "</Update>"),
	//
	UPDATE_MULTIPLE("<UpdateMultiple xmlns=\"%s\">", "</UpdateMultiple>"),
	//
	READ("<Read xmlns=\"%s\">", "</Read>"),
	//
	READ_MULTIPLE("<ReadMultiple xmlns=\"%s\">", "</ReadMultiple>");

	private String openTag;
	private String closeTag;

	public String getOpenTag() {
		return this.openTag;
	}

	public String getCloseTag() {
		return this.closeTag;
	}

	private SoapRequestType(String openTag, String closeTag) {
		this.openTag = openTag;
		this.closeTag = closeTag;
	}
}
