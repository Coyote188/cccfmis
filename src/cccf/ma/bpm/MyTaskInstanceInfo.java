package cccf.ma.bpm;

import java.util.Date;

import cccf.ma.model.ApplicationInfo;
import openjframework.bpm.model.TaskInstanceInfo;

public class MyTaskInstanceInfo extends TaskInstanceInfo {

	private ApplicationInfo applicationInfo;
	private Date dueDate;
	private boolean isExpire;// 是否到期
	private String simpleTaskName;// 简称
	private int simpleTaskNameLen = 10;// 简称默认6个字

	public ApplicationInfo getApplicationInfo() {
		return applicationInfo;
	}

	public void setApplicationInfo(ApplicationInfo applicationInfo) {
		this.applicationInfo = applicationInfo;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public boolean isExpire() {
		Date today = new Date();
		boolean k = false;
		if (dueDate != null) {
			if (dueDate.before(today))
				k = true;
		}
		return k;
	}

	public String getSimpleTaskName() {
		if (getTaskName() != null) {
			if (getTaskName().length() > simpleTaskNameLen)
				simpleTaskName = (new StringBuilder(String
						.valueOf(getTaskName().substring(0, simpleTaskNameLen))))
						.toString();
			else
				simpleTaskName = getTaskName();
		}
		return simpleTaskName;
	}

	public int getSimpleTaskNameLen() {
		return simpleTaskNameLen;
	}

	public void setSimpleTaskNameLen(int simpleNameLen) {
		this.simpleTaskNameLen = simpleNameLen;
	}
}
