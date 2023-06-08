package com.github.glfrazier.util;

import java.util.Calendar;
import java.util.Date;

public class Timestamp {

	public static String getStringTimestamp() {
		Date d = new Date(System.currentTimeMillis());
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		String timestamp = String.format("%d-%02d-%02d_%02d-%02d-%02d", c.get(Calendar.YEAR), c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
				c.get(Calendar.SECOND));
		return timestamp;
	}

}
