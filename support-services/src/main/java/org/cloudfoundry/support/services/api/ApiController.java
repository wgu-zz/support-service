package org.cloudfoundry.support.services.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/api")
public class ApiController {

	private static final Logger logger = LoggerFactory
			.getLogger(ApiController.class);

	private String xBsAuthToken;
	private RestTemplate uaaRestTemplate;
	private RestTemplate restTemplate;
	private String portalUrl;
	private String zendeskCredential;
	private String zendeskApiUpdateTicketUrl;

	public void setXBsAuthToken(String xBsAuthToken) {
		this.xBsAuthToken = xBsAuthToken;
	}

	public void setUaaRestTemplate(RestTemplate uaaRestTemplate) {
		this.uaaRestTemplate = uaaRestTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public void setPortalUrl(String portalUrl) {
		this.portalUrl = portalUrl;
	}

	public void setZendeskCredential(String zendeskCredential) {
		this.zendeskCredential = zendeskCredential;
	}

	public void setZendeskApiUpdateTicketUrl(String zendeskApiUpdateTicketUrl) {
		this.zendeskApiUpdateTicketUrl = zendeskApiUpdateTicketUrl;
	}

	@RequestMapping("paymentInfo")
	@ResponseBody
	public void paymentInfo(@RequestParam String input) {
		String[] inputs = input.split(",");
		String ticketId = inputs[0];
		String userExternalId = inputs[1];
		String tag = getPaymentInfoFromPortal(userExternalId);
		updateZdTicketTag(ticketId, tag);
	}

	private void updateZdTicketTag(String ticketId, String tag) {
		HttpHeaders zdHeaders = new HttpHeaders();
		zdHeaders.set("Accept", "application/json");
		zdHeaders.set("Authorization", zendeskCredential);
		zdHeaders.setContentType(MediaType.APPLICATION_JSON);
		Map<String, String> items = new HashMap<String, String>();
		items.put("tags", tag);
		Map<String, Map<String, String>> ticket = new HashMap<String, Map<String, String>>();
		ticket.put("ticket", items);
		HttpEntity<Map> zdEntity = new HttpEntity<Map>(ticket, zdHeaders);
		ResponseEntity<String> zdResponse = restTemplate.exchange(
				zendeskApiUpdateTicketUrl, HttpMethod.PUT, zdEntity,
				String.class, ticketId);
		logger.debug("Response code from zendesk: "
				+ zdResponse.getStatusCode().toString());
	}

	private String getPaymentInfoFromPortal(String userExternalId) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-BS-Auth-Token", xBsAuthToken);
		ResponseEntity<Map> result = uaaRestTemplate.exchange(portalUrl,
				HttpMethod.GET, new HttpEntity<String>(headers), Map.class,
				userExternalId);
		logger.debug("Response code from portal: "
				+ result.getStatusCode().toString());
		List<Map<String, ?>> orgs = (List<Map<String, ?>>) result.getBody()
				.get("organizations");
		for (Map<String, ?> org : orgs) {
			String type = (String) org.get("type");
			if ("paid".equalsIgnoreCase(type)) {
				return "paid";
			}
		}
		return "free";
	}

}
