package org.cloudfoundry.support.services.api;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudfoundry.support.services.api.ApiController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ApiControllerTest {

	private String xBsAuthToken = "xBsAuthToken";
	private String portalUrl = "portalUrl";
	private String zendeskCredential = "zendeskCredential";
	private String zendeskApiUpdateTicketUrl = "zendeskApiUpdateTicketUrl";

	@Mock
	private RestTemplate uaaRestTemplate;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testPaymentInfo() {
		String userExternalId = "external1";
		String ticketId = "1";
		String paidTag = "paid";
		// Mock the information got from portal
		Map<String, String> orgMap = new HashMap<String, String>();
		orgMap.put("type", paidTag);
		List<Map<String, String>> orgsList = new ArrayList<Map<String, String>>();
		orgsList.add(orgMap);
		Map portalReturnMap = new HashMap();
		portalReturnMap.put("organizations", orgsList);
		ResponseEntity<Map> portalReturnValue = new ResponseEntity<Map>(
				portalReturnMap, HttpStatus.OK);
		when(
				uaaRestTemplate.exchange(eq(portalUrl), eq(HttpMethod.GET),
						isA(HttpEntity.class), eq(Map.class),
						eq(userExternalId))).thenReturn(portalReturnValue);

		// Mock the value to be sent to zendesk based on the info got from
		// portal
		Map<String, String> items = new HashMap<String, String>();
		items.put("tags", paidTag);
		Map<String, Map<String, String>> ticket = new HashMap<String, Map<String, String>>();
		ticket.put("ticket", items);
		ResponseEntity<String> zdReturnValue = new ResponseEntity<String>(
				"{\"ticket\": {{\"id\": 1, \"tags\": \"paid\"}}}",
				HttpStatus.OK);
		when(
				uaaRestTemplate.exchange(eq(zendeskApiUpdateTicketUrl),
						eq(HttpMethod.PUT), isA(HttpEntity.class),
						eq(String.class), eq(ticketId))).thenReturn(
				zdReturnValue);
		// Set the mocked object and run the method
		ApiController restController = new ApiController();
		restController.setXBsAuthToken(xBsAuthToken);
		restController.setPortalUrl(portalUrl);
		restController.setUaaRestTemplate(uaaRestTemplate);
		restController.setRestTemplate(uaaRestTemplate);
		restController.setZendeskApiUpdateTicketUrl(zendeskApiUpdateTicketUrl);
		restController.setZendeskCredential(zendeskCredential);
		restController.paymentInfo(ticketId + "," + userExternalId);
		// verify methods called
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-BS-Auth-Token", xBsAuthToken);
		verify(uaaRestTemplate).exchange(eq(portalUrl), eq(HttpMethod.GET),
				refEq(new HttpEntity<String>(headers)), eq(Map.class),
				eq(userExternalId));
		verify(uaaRestTemplate).exchange(eq(zendeskApiUpdateTicketUrl),
				eq(HttpMethod.PUT), isA(HttpEntity.class), eq(String.class),
				eq(ticketId));
	}

}
