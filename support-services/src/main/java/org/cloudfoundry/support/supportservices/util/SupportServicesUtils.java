package org.cloudfoundry.support.supportservices.util;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DurationFormatUtils;

public class SupportServicesUtils {

	public static String getDurationTillNow(Date lastUpdateDateTime) {
		long now = Calendar.getInstance().getTimeInMillis();
		long millis = now - lastUpdateDateTime.getTime();
		return DurationFormatUtils.formatDurationWords(millis, true, true);
	}

}
