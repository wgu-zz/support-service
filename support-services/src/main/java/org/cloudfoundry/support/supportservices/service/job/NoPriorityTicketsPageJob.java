package org.cloudfoundry.support.supportservices.service.job;

import java.util.List;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.cloudfoundry.support.supportservices.domain.Ticket;
import org.cloudfoundry.support.supportservices.util.SupportServicesUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoPriorityTicketsPageJob extends PageJob {

	private static final Logger logger = LoggerFactory
			.getLogger(NoPriorityTicketsPageJob.class);

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		long timeout = settingsRepository.getSettings().getTimeoutNoPriority();
		List<Ticket> ticketsWithoutPriority = ticketRepository
				.getTicketsWithoutPriority(timeout);
		if (ticketsWithoutPriority.isEmpty()) {
			return;
		}
		String timeoutString = DurationFormatUtils.formatDurationWords(
				timeout * 1000, true, true);
		StringBuilder description = new StringBuilder();
		if (ticketsWithoutPriority.size() > 1) {
			description.append(
					"Multiple zendesk tickets have no priority set in ")
					.append(timeoutString);
		} else {
			Ticket ticket = ticketsWithoutPriority.get(0);
			description
					.append("Zendesk ticket #")
					.append(ticket.getTicketId())
					.append(" \"")
					.append(ticket.getTitle())
					.append("\" has no priority set in ")
					.append(SupportServicesUtils.getDurationTillNow(ticket
							.getLastUpdateDateTime()));
		}
		page(description.toString(), ticketsWithoutPriority);
	}

}
