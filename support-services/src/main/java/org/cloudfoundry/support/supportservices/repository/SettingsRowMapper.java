package org.cloudfoundry.support.supportservices.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.cloudfoundry.support.supportservices.domain.Settings;
import org.springframework.jdbc.core.RowMapper;

public class SettingsRowMapper implements RowMapper<Settings> {

	@Override
	public Settings mapRow(ResultSet rs, int rowNum) throws SQLException {
		Settings settings = new Settings();
		settings.setTimeoutNoPriority(rs.getLong("timeout_no_priority"));
		settings.setTimeoutEmailS1(rs.getLong("timeout_email_s1"));
		settings.setTimeoutEmailS2(rs.getLong("timeout_email_s2"));
		settings.setTimeoutEmailS3(rs.getLong("timeout_email_s3"));
		settings.setTimeoutEmailS4(rs.getLong("timeout_email_s4"));
		settings.setTimeoutPagerDutyS1(rs.getLong("timeout_pagerduty_s1"));
		settings.setTimeoutPagerDutyS2(rs.getLong("timeout_pagerduty_s2"));
		return settings;
	}

}
