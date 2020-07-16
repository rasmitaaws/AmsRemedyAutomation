package com.cpa.automation.demo.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

	static String ISO_DATE_FORMAT_ZERO_OFFSET = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	static String UTC_TIMEZONE_NAME = "UTC";

	static SimpleDateFormat provideDateFormat() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ISO_DATE_FORMAT_ZERO_OFFSET);
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone(UTC_TIMEZONE_NAME));
		return simpleDateFormat;
	}

	static int getDateValueFromStringJson(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateUtil.ISO_DATE_FORMAT_ZERO_OFFSET);
		LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
		return dateTime.getDayOfYear();
	}

}
