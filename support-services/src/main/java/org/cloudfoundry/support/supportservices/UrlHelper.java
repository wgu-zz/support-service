package org.cloudfoundry.support.supportservices;

import org.springframework.util.StringUtils;

public class UrlHelper {

	private static String zendeskDomain;
	private static String zendeskApiIncrementalTicket;
	private static String zendeskShowUser;
	private static String pagerDutyTriggerUrl;
	private static String sendgridEmailUrl;

	public static String getZendeskDomain() {
		return zendeskDomain;
	}

	public void setZendeskDomain(String zendeskDomain) {
		UrlHelper.zendeskDomain = StringUtils.trimTrailingCharacter(
				zendeskDomain, '/');
	}

	public static String getZendeskApiIncrementalTicket() {
		return zendeskDomain + "/" + zendeskApiIncrementalTicket;
	}

	public void setZendeskApiIncrementalTicket(
			String zendeskApiIncrementalTicket) {
		UrlHelper.zendeskApiIncrementalTicket = StringUtils
				.trimLeadingCharacter(zendeskApiIncrementalTicket, '/');
	}

	public static String getPagerDutyTriggerUrl() {
		return pagerDutyTriggerUrl;
	}

	public void setPagerDutyTriggerUrl(String pagerDutyTriggerUrl) {
		UrlHelper.pagerDutyTriggerUrl = pagerDutyTriggerUrl;
	}

	public static String getZendeskShowUser() {
		return zendeskDomain + "/" + zendeskShowUser;
	}

	public void setZendeskShowUser(String zendeskShowUser) {
		UrlHelper.zendeskShowUser = StringUtils.trimLeadingCharacter(
				zendeskShowUser, '/');
	}

	public static String getSendgridEmailUrl() {
		return sendgridEmailUrl;
	}

	public void setSendgridEmailUrl(String sendgridEmailUrl) {
		UrlHelper.sendgridEmailUrl = sendgridEmailUrl;
	}

}
