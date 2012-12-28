package org.cloudfoundry.support.supportservices.service.job;

import java.util.List;

import org.cloudfoundry.support.supportservices.domain.Settings;
import org.cloudfoundry.support.supportservices.domain.Ticket;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TimeoutTicketsPageJob extends PageJob {

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		Settings settings = settingsRepository.getSettings();
		List<Ticket> pagerDutyResults = ticketRepository
				.getTicketsForPage(settings);
		if (pagerDutyResults.isEmpty()) {
			return;
		}
		StringBuilder description = new StringBuilder();
		if (pagerDutyResults.size() > 1) {
			description
					.append("Multiple zendesk tickets have hit the SLA target time!");
		} else {
			Ticket ticket = pagerDutyResults.get(0);
			description.append("Zendesk ticket #").append(ticket.getTicketId())
					.append(" \"").append(ticket.getTitle())
					.append("\" hit the SLA target time!");
		}
		page(description.toString(), pagerDutyResults);
	}

}
