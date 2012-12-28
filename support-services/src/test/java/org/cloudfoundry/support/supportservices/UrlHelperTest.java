package org.cloudfoundry.support.supportservices;

import static org.junit.Assert.*;

import org.cloudfoundry.support.supportservices.UrlHelper;
import org.junit.Test;

public class UrlHelperTest {

	@Test
	public void testGetZendeskApiIncrementalTicket() {
		UrlHelper urlHelper = new UrlHelper();
		urlHelper.setZendeskApiIncrementalTicket("/dummySub");
		urlHelper.setZendeskDomain("http://dummy/");
		assertEquals("http://dummy/dummySub",
				UrlHelper.getZendeskApiIncrementalTicket());
	}

}
