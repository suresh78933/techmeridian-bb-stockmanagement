package com.techmeridian.stockmanagement.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.util.StringUtils;

public final class Utility {

	public static final Integer getIntFromString(String value) {
		int resp = 0;
		try {
			if (!StringUtils.isEmpty(value)) {
				resp = Integer.parseInt(value);
			}
		} catch (Exception e) {
			resp = 0;
		}
		return resp;
	}

	public static final Integer getInt(Integer value) {
		return value != null ? value : 0;
	}

	public static final boolean isStringEmpty(String value) {
		return value == null || "".equals(value.trim());
	}

	public static final boolean isStringNotEmpty(String value) {
		return !isStringEmpty(value);
	}

	public static final String escapeCharaters(String value) {
		return value.replaceAll(";", "\\\\;");
	}

	public static final String unEscapeCharaters(String value) {
		return value.replaceAll("\\\\;", ";");
	}

	public static final String getNavDate() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormat.format(Calendar.getInstance().getTime());
	}

	public static final String replaceBackSlashWithHiphen(String value) {
		return value.replaceAll("\\\\", "-");
	}
}
