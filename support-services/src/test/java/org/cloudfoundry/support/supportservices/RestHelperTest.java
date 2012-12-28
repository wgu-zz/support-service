package org.cloudfoundry.support.supportservices;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

public class RestHelperTest {

	@Mock
	private RestTemplate restTemplate;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetJsonFromZd() {
		new RestHelper().setRestTemplate(restTemplate);
		RestHelper.getJsonFromZd("dummyUrl", null);
		verify(restTemplate).exchange(eq("dummyUrl"), eq(HttpMethod.GET),
				isA(HttpEntity.class), eq(Map.class));
	}

	@Test
	public void testPostJsonToPd() {
		new RestHelper().setRestTemplate(restTemplate);
		RestHelper.postJsonToPd("dummyUrl", null);
		verify(restTemplate).exchange(eq("dummyUrl"), eq(HttpMethod.POST),
				isA(HttpEntity.class), eq(Map.class));
	}

}
