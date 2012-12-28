package org.cloudfoundry.support.supportservices.service.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudfoundry.support.supportservices.RestHelper;
import org.cloudfoundry.support.supportservices.UrlHelper;
import org.cloudfoundry.support.supportservices.domain.Ticket;
import org.cloudfoundry.support.supportservices.repository.SettingsRepository;
import org.cloudfoundry.support.supportservices.repository.TicketRepository;
import org.cloudfoundry.support.supportservices.util.SupportServicesUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public abstract class PageJob extends QuartzJobBean {

	protected SettingsRepository settingsRepository;
	protected TicketRepository ticketRepository;

	public void setSettingsRepository(SettingsRepository settingsRepository) {
		this.settingsRepository = settingsRepository;
	}

	public void setTicketRepository(TicketRepository ticketRepository) {
		this.ticketRepository = ticketRepository;
	}

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		// left blank for implementing classes
	}

	protected void page(String description, List<Ticket> tickets) {
		Map<String, Object> requestBody = new HashMap<String, Object>();
		requestBody.put("event_type", "trigger");
		requestBody.put("description", description);
		List<Map<String, Object>> ticketList = new ArrayList<Map<String, Object>>(
				tickets.size());
		List<Integer> ids = new ArrayList<Integer>(tickets.size());
		for (Ticket aTicket : tickets) {
			Map<String, Object> details = new HashMap<String, Object>();
			details.put("id", aTicket.getTicketId());
			details.put("title", aTicket.getTitle());
			details.put("priority", aTicket.getPriority());
			details.put("status", aTicket.getStatus());
			details.put("url", aTicket.getUrl());
			details.put("opened for", SupportServicesUtils
					.getDurationTillNow(aTicket.getLastUpdateDateTime()));
			ticketList.add(details);
			ids.add(aTicket.getTicketId());
		}
		requestBody.put("details", ticketList);
		RestHelper
				.postJsonToPd(UrlHelper.getPagerDutyTriggerUrl(), requestBody);
		for (int id : ids) {
			ticketRepository.setPaged(id);
		}
	}

}
