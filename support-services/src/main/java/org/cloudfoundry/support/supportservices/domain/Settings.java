package org.cloudfoundry.support.supportservices.domain;

public class Settings {

	private long timeoutNoPriority;
	private long timeoutEmailS1;
	private long timeoutPagerDutyS1;
	private long timeoutEmailS2;
	private long timeoutPagerDutyS2;
	private long timeoutEmailS3;
	private long timeoutEmailS4;

	public long getTimeoutEmailS1() {
		return timeoutEmailS1;
	}

	public long getTimeoutPagerDutyS1() {
		return timeoutPagerDutyS1;
	}

	public long getTimeoutEmailS2() {
		return timeoutEmailS2;
	}

	public long getTimeoutPagerDutyS2() {
		return timeoutPagerDutyS2;
	}

	public long getTimeoutEmailS3() {
		return timeoutEmailS3;
	}

	public long getTimeoutEmailS4() {
		return timeoutEmailS4;
	}

	public long getTimeoutNoPriority() {
		return timeoutNoPriority;
	}

	public void setTimeoutEmailS1(long timeoutEmailS1) {
		this.timeoutEmailS1 = timeoutEmailS1;
	}

	public void setTimeoutPagerDutyS1(long timeoutPagerDutyS1) {
		this.timeoutPagerDutyS1 = timeoutPagerDutyS1;
	}

	public void setTimeoutEmailS2(long timeoutEmailS2) {
		this.timeoutEmailS2 = timeoutEmailS2;
	}

	public void setTimeoutPagerDutyS2(long timeoutPagerDutyS2) {
		this.timeoutPagerDutyS2 = timeoutPagerDutyS2;
	}

	public void setTimeoutEmailS3(long timeoutEmailS3) {
		this.timeoutEmailS3 = timeoutEmailS3;
	}

	public void setTimeoutEmailS4(long timeoutEmailS4) {
		this.timeoutEmailS4 = timeoutEmailS4;
	}

	public void setTimeoutNoPriority(long timeoutNoPriority) {
		this.timeoutNoPriority = timeoutNoPriority;
	}

}
