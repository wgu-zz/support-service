package org.cloudfoundry.support.supportservices.repository.impl;

import java.util.List;
import java.util.Map;

import org.cloudfoundry.support.supportservices.domain.Settings;
import org.cloudfoundry.support.supportservices.repository.SettingsRepository;
import org.cloudfoundry.support.supportservices.repository.SettingsRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class SettingsRepositoryImpl implements SettingsRepository {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public long getIncrementalTicketStartTime() {
		String sql = "SELECT incremental_ticket_start_time FROM settings";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
		return result.isEmpty() ? 0 : (Long) result.get(0).get(
				"incremental_ticket_start_time");
	}

	public void updateIncrementalTicketStartTime(int nextStartTime) {
		String sql = "UPDATE settings SET incremental_ticket_start_time = ?";
		jdbcTemplate.update(sql, nextStartTime);
	}

	public Settings getSettings() {
		String sql = "SELECT * FROM settings";
		List<Settings> settings = jdbcTemplate.query(sql,
				new SettingsRowMapper());
		return settings.get(0);
	}

	public void updateSettings(Settings settings) {
		String sql = "UPDATE settings SET timout_no_priority = ?, timeout_email_s1 = ?, timeout_pagerduty_s1 = ?, timeout_email_s2 = ?, timeout_pagerduty_s2 = ?, timeout_email_s3 = ?, timeout_email_s4 = ?";
		jdbcTemplate.update(sql, settings.getTimeoutNoPriority(),
				settings.getTimeoutEmailS1(), settings.getTimeoutPagerDutyS1(),
				settings.getTimeoutEmailS2(), settings.getTimeoutPagerDutyS2(),
				settings.getTimeoutEmailS3(), settings.getTimeoutEmailS4());
	}

}
