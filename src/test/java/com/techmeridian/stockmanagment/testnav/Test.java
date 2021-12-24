package com.techmeridian.stockmanagment.testnav;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class Test implements KvmSerializable {

	private String code;
	private String description;

	public Test(String code, String description) {
		this.code = code;
		this.description = description;
	}

	@Override
	public Object getProperty(int index) {
		switch (index) {
		case 0:
			return code;
		case 1:
			return description;
		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		return 2;
	}

	@Override
	public void setProperty(int index, Object value) {
		switch (index) {
		case 0:
			code = value != null ? value.toString() : "";
			break;
		case 1:
			description = value != null ? value.toString() : "";
			break;
		default:
			break;
		}
	}

	@Override
	public void getPropertyInfo(int index, Hashtable properties, PropertyInfo info) {
		switch (index) {
		case 0:
			if (code == null || "".equals(code.trim())) {
				info = null;
			} else {
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "Code";
			}
			break;
		case 1:
			if (description == null || "".equals(description.trim())) {
				info = null;
			} else {
				info.type = PropertyInfo.STRING_CLASS;
				info.name = "Description";
			}
			break;
		default:
			break;
		}
	}

}
