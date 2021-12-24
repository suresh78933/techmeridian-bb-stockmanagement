package com.techmeridian.stockmanagement.soap;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class SoapParam {

	private String soapParamKey;
	private Object soapParamValue;
	private String soapParamType;
}
