package org.cloudfoundry.support.supportservices.service.job;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.cloudfoundry.support.supportservices.RestHelper;
import org.cloudfoundry.support.supportservices.UrlHelper;
import org.cloudfoundry.support.supportservices.domain.Settings;
import org.cloudfoundry.support.supportservices.domain.Ticket;
import org.cloudfoundry.support.supportservices.util.SupportServicesUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

public class TimeoutTicketsEmailJob extends EmailJob {

	private static final Logger logger = Logger
			.getLogger(TimeoutTicketsEmailJob.class);

	private static final String EMAIL_SUBJECT = "[ACTION REQUIRED] Zendesk Ticket SLA Notification";

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		Settings settings = settingsRepository.getSettings();
		List<Ticket> emailResults = ticketRepository
				.getTicketsForEmail(settings);
		for (Ticket result : emailResults) {
			int assigneeId = result.getAssigneeId();
			ResponseEntity<Map> response;
			try {
				response = RestHelper.getJsonFromZd(
						UrlHelper.getZendeskShowUser(), null, assigneeId);
			} catch (RestClientException e) {
				logger.error(
						"Error retrieving user info for id: " + assigneeId, e);
				continue;
			}
			Map<String, String> user = (Map<String, String>) response.getBody()
					.get("user");
			StringBuilder sb = new StringBuilder();
			sb.append("<p><h3>")
					.append("The following ticket has been open for ")
					.append(SupportServicesUtils.getDurationTillNow(result
							.getLastUpdateDateTime())).append("</h3></p>");
			sb.append("<p>").append("Ticket ID: ").append(result.getTicketId())
					.append("</p>");
			sb.append("<p>").append("Title: ").append(result.getTitle())
					.append("</p>");
			sb.append("<p>").append("Priority: ").append(result.getPriority())
					.append("</p>");
			sb.append("<p>").append("URL: ").append(result.getUrl())
					.append("</p>");
			try {
				RestHelper.postJson(UrlHelper.getSendgridEmailUrl(), null,
						user.get("email"), user.get("name"), EMAIL_SUBJECT,
						sb.toString());
				ticketRepository.setEmailed(result.getId());
			} catch (RestClientException e) {
				logger.error("Error sending email via Sendgird!", e);
			}
		}
	}

}
