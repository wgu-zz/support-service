package org.cloudfoundry.identity.support.web;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudfoundry.identity.uaa.openid.OpenIdUserDetails;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class HomeControllerTest {

	private MockHttpServletRequest request;
	private TestableHomeController controller;
	private Map<String, Object> model;
	private Principal principal;

	@Mock
	private JdbcTemplate jdbcTemplate;

	@Before
	public void setUp() {

		MockitoAnnotations.initMocks(this);

		//Creating the Principal Object
		List<GrantedAuthority> authorities = Arrays.<GrantedAuthority> asList(new SimpleGrantedAuthority("ROLE_USER"));

		OpenIdUserDetails user = new OpenIdUserDetails("marissa", authorities);
		user.setName("Marissa Bloggs");
		user.setEmail("marissa@test.org");
		user.setId("12");

		principal = new UsernamePasswordAuthenticationToken(user, null, authorities);

		//Set SecurityContext
		SecurityContextHolder.getContext().setAuthentication( new UsernamePasswordAuthenticationToken(principal, null, authorities));

		request = new MockHttpServletRequest();
		request.addHeader("x-cluster-client-ip", "127.0.0.1");

		controller = new TestableHomeController();
		model = new HashMap<String, Object>();

		controller.setJdbcTemplate(jdbcTemplate);
		controller.setReturnTo("https://cloudfoundry1330047343.zendesk.com/access/remote");
		controller.setToken("HUfMLdWneZPVkM1fjGq21tWdwK76H4I926yiK2SUShsZYjCN");
	}

	@Test
	public void homeTest() throws Exception {

		assertEquals(principal, SecurityContextHolder.getContext().getAuthentication().getPrincipal());

		String redirect = controller.home(request, principal, model);

		assertEquals("redirect:https://cloudfoundry1330047343.zendesk.com/access/remote", redirect);
		assertEquals("marissa@test.org", model.get("email"));
		assertEquals("12", model.get("external_id"));
		assertEquals("Marissa Bloggs", model.get("name"));
		assertEquals("dd459a6c87620ffbd5e5ca737803a91e", model.get("hash"));
		assertEquals((long) 10, model.get("timestamp"));
		assertEquals(null, SecurityContextHolder.getContext().getAuthentication());

		verify(jdbcTemplate).update(eq("INSERT INTO users (EMAIL, IP, TIMESTMP) VALUES (?, ?, ?)"), eq("marissa@test.org"), eq("127.0.0.1"), eq(new Timestamp(10000)));
	}

	@Test
	public void historySaveFailsTest() throws Exception {

		assertEquals(principal, SecurityContextHolder.getContext().getAuthentication().getPrincipal());

		when(jdbcTemplate.update(anyString(), anyString(), anyString(), any(Timestamp.class))).thenThrow(new RuntimeException("unable to update"));

		String redirect = controller.home(request, principal, model);

		assertEquals("redirect:https://cloudfoundry1330047343.zendesk.com/access/remote", redirect);
		assertEquals("marissa@test.org", model.get("email"));
		assertEquals("Marissa Bloggs", model.get("name"));
		assertEquals("dd459a6c87620ffbd5e5ca737803a91e", model.get("hash"));
		assertEquals((long) 10, model.get("timestamp"));
		assertEquals(null, SecurityContextHolder.getContext().getAuthentication());

		verify(jdbcTemplate).update(eq("INSERT INTO users (EMAIL, IP, TIMESTMP) VALUES (?, ?, ?)"), eq("marissa@test.org"), eq("127.0.0.1"), eq(new Timestamp(10000)));
	}
}

class TestableHomeController extends HomeController {
	@Override
	protected long getTime() {
		return 10000;
	}
}