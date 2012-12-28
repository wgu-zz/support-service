package org.cloudfoundry.support.supportservices.repository;

import java.util.Date;
import java.util.List;

import org.cloudfoundry.support.supportservices.domain.Settings;
import org.cloudfoundry.support.supportservices.domain.Ticket;

public interface TicketRepository {

	public void updateOrInsert(int ticketId, String title, String priority,
			String status, int assignee_id, String url, Date createDateTime,
			Date lastUpdateDateTime);

	public void deleteByTicketId(int ticketId);

	public List<Ticket> getTicketsForEmail(Settings settings);

	public List<Ticket> getTicketsForPage(Settings settings);

	public void setEmailed(long id);

	public void setPaged(long id);

	public List<Ticket> getTicketsWithoutPriority(long timeout);

}
