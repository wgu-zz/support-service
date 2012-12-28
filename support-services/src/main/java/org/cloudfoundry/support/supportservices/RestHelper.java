package org.cloudfoundry.support.supportservices;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RestHelper {

	private static RestTemplate restTemplate;

	private static String zendeskCredential;
	private static String pagerDutyServiceKey;

	public void setZendeskCredential(String zendeskCredential) {
		RestHelper.zendeskCredential = zendeskCredential;
	}

	public void setPagerDutyServiceKey(String pagerDutyServiceKey) {
		RestHelper.pagerDutyServiceKey = pagerDutyServiceKey;
	}

	@Autowired
	public void setRestTemplate(RestTemplate restTemplate) {
		RestHelper.restTemplate = restTemplate;
	}

	/**
	 * Call given URL with given entity using the json format and "GET" http
	 * method. The returned response should be also json format which can be
	 * mapped to a Map.
	 *
	 * @param url
	 * @param bodyItems
	 * @param uriViarable
	 * @return
	 */
	public static ResponseEntity<Map> getJsonFromZd(String url, Map bodyItems,
			Object... uriViarable) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Accept", "application/json");
		headers.set("Authorization", zendeskCredential);
		HttpEntity<Map> entity = new HttpEntity<Map>(bodyItems, headers);
		return restTemplate.exchange(url, HttpMethod.GET, entity, Map.class,
				uriViarable);
	}

	public static ResponseEntity<Map> postJsonToPd(String url, Map bodyItems,
			Object... uriViarable) {
		if (bodyItems == null) {
			bodyItems = new HashMap();
		}
		bodyItems.put("service_key", pagerDutyServiceKey);
		return postJson(url, bodyItems, uriViarable);
	}

	public static ResponseEntity<Map> postJson(String url, Map bodyItems,
			Object... uriViarable) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Map> entity = new HttpEntity<Map>(bodyItems, headers);
		return restTemplate.exchange(url, HttpMethod.POST, entity, Map.class,
				uriViarable);
	}

}
