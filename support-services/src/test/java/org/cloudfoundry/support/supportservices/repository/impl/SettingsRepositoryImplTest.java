package org.cloudfoundry.support.supportservices.repository.impl;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

public class SettingsRepositoryImplTest {

	@Mock
	private JdbcTemplate jdbcTemplate;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetIncrementalTicketStartTime() {
		when(jdbcTemplate.queryForList(any(String.class))).thenReturn(
				new ArrayList<Map<String, Object>>());
		SettingsRepositoryImpl dao = new SettingsRepositoryImpl();
		dao.setJdbcTemplate(jdbcTemplate);
		assertEquals(0, dao.getIncrementalTicketStartTime());
	}

}
