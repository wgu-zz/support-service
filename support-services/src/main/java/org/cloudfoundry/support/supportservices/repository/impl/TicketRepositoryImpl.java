package org.cloudfoundry.support.supportservices.repository.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.cloudfoundry.support.supportservices.domain.Settings;
import org.cloudfoundry.support.supportservices.domain.Ticket;
import org.cloudfoundry.support.supportservices.repository.TicketRepository;
import org.cloudfoundry.support.supportservices.repository.TicketRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class TicketRepositoryImpl implements TicketRepository {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void updateOrInsert(int ticketId, String title, String priority,
			String status, int assignee_id, String url, Date createDateTime,
			Date lastUpdateDateTime) {
		String querySql = "SELECT id FROM ticket WHERE ticket_id = ?";
		String insertSql = "INSERT INTO ticket (ticket_id, title, priority, status, assignee_id, url, create_datetime, last_update_datetime) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		String updateSql = "UPDATE ticket SET title = ?, priority = ?, status = ?, assignee_id = ?, url = ?, create_datetime = ?, last_update_datetime = ?, is_emailed = 0, is_paged = 0 WHERE id = ?";
		List<Map<String, Object>> id = jdbcTemplate.queryForList(querySql,
				ticketId);
		if (id.isEmpty()) {
			jdbcTemplate.update(insertSql, ticketId, title, priority, status,
					assignee_id, url, createDateTime, lastUpdateDateTime);
			return;
		}
		jdbcTemplate.update(updateSql, title, priority, status, assignee_id,
				url, createDateTime, lastUpdateDateTime, id.get(0).get("id"));
	}

	public void deleteByTicketId(int ticketId) {
		String sql = "UPDATE ticket SET is_deleted = 1 WHERE ticket_id = ?";
		jdbcTemplate.update(sql);
	}

	public List<Ticket> getTicketsForEmail(Settings settings) {
		String sql = "SELECT * "
				+ "FROM   ticket "
				+ "WHERE  (( Unix_timestamp() - Unix_timestamp(last_update_datetime) > ? "
				+ "AND priority = 'Urgent' ) "
				+ "OR ( Unix_timestamp() - Unix_timestamp(last_update_datetime) > ? "
				+ "AND priority = 'High' ) "
				+ "OR ( Unix_timestamp() - Unix_timestamp(last_update_datetime) > ? "
				+ "AND priority = 'Normal' ) "
				+ "OR ( Unix_timestamp() - Unix_timestamp(last_update_datetime) > ? "
				+ "AND priority = 'Low' )) AND status != 'Pending' "
				+ "AND is_emailed <= 0 AND is_deleted <= 0";
		return jdbcTemplate.query(sql, new TicketRowMapper(),
				settings.getTimeoutEmailS1(), settings.getTimeoutEmailS2(),
				settings.getTimeoutEmailS3(), settings.getTimeoutEmailS4());
	}

	public List<Ticket> getTicketsWithoutPriority(long timeout) {
		String sql = "SELECT * "
				+ "FROM ticket "
				+ "WHERE (Unix_timestamp() - Unix_timestamp(last_update_datetime)) > ? "
				+ "AND (priority = '-' OR priority = NULL) "
				+ "AND is_deleted <= 0 AND is_paged <= 0";
		return jdbcTemplate.query(sql, new TicketRowMapper(), timeout);
	}

	public List<Ticket> getTicketsForPage(Settings settings) {
		String sql = "SELECT * "
				+ "FROM   ticket "
				+ "WHERE  (( Unix_timestamp() - Unix_timestamp(last_update_datetime) > ? "
				+ "AND priority = 'Urgent' ) "
				+ "OR ( Unix_timestamp() - Unix_timestamp(last_update_datetime) > ? "
				+ "AND priority = 'High' )) " + "AND status != 'Pending' "
				+ "AND is_paged <= 0 AND is_deleted <= 0";
		return jdbcTemplate.query(sql, new TicketRowMapper(),
				settings.getTimeoutPagerDutyS1(),
				settings.getTimeoutPagerDutyS2());
	}

	public void setEmailed(long id) {
		String sql = "UPDATE ticket SET is_emailed = 1 WHERE id = ?";
		jdbcTemplate.update(sql, id);
	}

	public void setPaged(long id) {
		String sql = "UPDATE ticket SET is_paged = 1 WHERE id = ?";
		jdbcTemplate.update(sql, id);
	}

}
