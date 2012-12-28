package org.cloudfoundry.support.supportservices.repository;

import org.cloudfoundry.support.supportservices.domain.Settings;

public interface SettingsRepository {

	public long getIncrementalTicketStartTime();

	public void updateIncrementalTicketStartTime(int nextStartTime);

	public Settings getSettings();

	public void updateSettings(Settings settings);

}
