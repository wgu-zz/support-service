package org.cloudfoundry.support.supportservices.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.cloudfoundry.support.supportservices.domain.Ticket;
import org.springframework.jdbc.core.RowMapper;

public class TicketRowMapper implements RowMapper<Ticket> {

	@Override
	public Ticket mapRow(ResultSet rs, int rowNum) throws SQLException {
		Ticket ticket = new Ticket();
		ticket.setAssigneeId(rs.getInt("assignee_id"));
		ticket.setCreateDateTime(rs.getDate("create_datetime"));
		ticket.setDeleted(rs.getBoolean("is_deleted"));
		ticket.setEmailed(rs.getBoolean("is_emailed"));
		ticket.setId(rs.getInt("id"));
		ticket.setLastUpdateDateTime(rs.getDate("last_update_datetime"));
		ticket.setPaged(rs.getBoolean("is_paged"));
		ticket.setPriority(rs.getString("priority"));
		ticket.setStatus(rs.getString("status"));
		ticket.setTicketId(rs.getInt("ticket_id"));
		ticket.setTitle(rs.getString("title"));
		ticket.setUrl(rs.getString("url"));
		return ticket;
	}

}
