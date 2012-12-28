package org.cloudfoundry.support.supportservices.service.job;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.cloudfoundry.support.supportservices.repository.SettingsRepository;
import org.cloudfoundry.support.supportservices.repository.TicketRepository;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class TicketSlaTimerJob extends QuartzJobBean {

	private static final Logger logger = LoggerFactory
			.getLogger(TicketSlaTimerJob.class);

	enum Status {
		New, Open, Pending, Solved, Closed, Deleted
	}

	private SettingsRepository settingsRepository;
	private TicketRepository ticketRepository;

	@Autowired
	public void setSettingsRepository(SettingsRepository settingsRepository) {
		this.settingsRepository = settingsRepository;
	}

	@Autowired
	public void setTicketRepository(TicketRepository ticketRepository) {
		this.ticketRepository = ticketRepository;
	}

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		// ResponseEntity<Map> response = RestHelper.getJsonFromZd(
		// UrlHelper.getZendeskApiIncrementalTicket(), null,
		// settingsRepository.getIncrementalTicketStartTime());
		// Map responseMap = response.getBody();
		// logger.info("Response from zd incremental ticket API:\n"
		// + responseMap.toString());
		// List<Map> results = (List<Map>) responseMap.get("results");
		// try {
		// processUpdatedTickets(results);
		// } catch (ParseException e) {
		// logger.error(
		// "Error processing zendesk response: \n"
		// + results.toString(), e);
		// throw new JobExecutionException(e);
		// }
		// Object nextStartTime = responseMap.get("end_time");
		// if (nextStartTime != null) {
		// settingsRepository
		// .updateIncrementalTicketStartTime((Integer) nextStartTime + 1);
		// }
		try {
			Scheduler scheduler = context.getScheduler();
			scheduler.triggerJob(new JobKey("noPriorityTicketsPageJobDetail"));
			scheduler.triggerJob(new JobKey("timeoutTicketsEmailJobDetail"));
			scheduler.triggerJob(new JobKey("timeoutTicketsPageJobDetail"));
		} catch (SchedulerException e) {
			logger.error("Failed to run schedule jobs!", e);
		}
	}

	private void processUpdatedTickets(List<Map> results) throws ParseException {
		if (results == null) {
			return;
		}
		for (Map result : results) {
			int ticketId = (Integer) result.get("id");
			String status = (String) result.get("status");
			Status currentStatus = Status.valueOf(status);
			if (currentStatus == Status.Solved
					|| currentStatus == Status.Closed
					|| currentStatus == Status.Deleted) {
				ticketRepository.deleteByTicketId(ticketId);
				continue;
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
			Date createDateTime = sdf.parse((String) result.get("created_at"));
			Date lastUpdateDateTime = sdf.parse((String) result
					.get("updated_at"));
			Object assigneeIdObj = result.get("assignee_id");
			int assigneeId;
			try {
				assigneeId = Integer.valueOf((String) assigneeIdObj);
			} catch (NumberFormatException e) {
				logger.warn("Failed to get assignee id for value: "
						+ assigneeIdObj + ". Ticket ID: " + ticketId);
				assigneeId = 0;
			}
			ticketRepository.updateOrInsert(ticketId,
					(String) result.get("subject"),
					(String) result.get("priority"), status, assigneeId,
					(String) result.get("url"), createDateTime,
					lastUpdateDateTime);
		}
	}

}
