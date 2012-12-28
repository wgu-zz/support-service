package org.cloudfoundry.support.supportservices.repository.impl;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

public class TicketRepositoryImplTest {

	@Mock
	private JdbcTemplate jdbcTemplate;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testUpdateOrInsertInsert() {
		when(jdbcTemplate.queryForList(any(String.class), any(Integer.class)))
				.thenReturn(new ArrayList<Map<String, Object>>());
		TicketRepositoryImpl dao = new TicketRepositoryImpl();
		dao.setJdbcTemplate(jdbcTemplate);
		Date now = Calendar.getInstance().getTime();
		dao.updateOrInsert(1, "DummyTitle", "Low", "New", 1, "DummyURL", now, now);
		verify(jdbcTemplate).update(startsWith("INSERT INTO"), eq(1),
				eq("DummyTitle"), eq("Low"), eq("New"), eq("DummyAssignee"),
				eq(now), eq(now));
	}

	@Test
	public void testUpdateOrInsertUpdate() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", 1);
		list.add(map);
		when(jdbcTemplate.queryForList(any(String.class), any(Integer.class)))
				.thenReturn(list);
		TicketRepositoryImpl dao = new TicketRepositoryImpl();
		dao.setJdbcTemplate(jdbcTemplate);
		Date now = Calendar.getInstance().getTime();
		dao.updateOrInsert(1, "DummyTitle", "Low", "New", 1, "DummyURL", now, now);
		verify(jdbcTemplate).update(startsWith("UPDATE"), eq("DummyTitle"),
				eq("Low"), eq("New"), eq("DummyAssignee"), eq(now), eq(now),
				eq(1));
	}

}
