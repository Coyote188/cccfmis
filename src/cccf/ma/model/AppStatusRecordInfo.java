package cccf.ma.model;

import java.math.*;
import openjframework.model.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;

import cccf.myenum.*;

public class AppStatusRecordInfo {
	private String asid;
	private Integer changeType;
	private String changeTypeName;
	private EnterpriseInfo enterprise;
	private java.util.Date applyDate;
	private String reason;
	private Integer status;
	private UserInfo approveUser;
	private java.util.Date approveDate;
	private String statusName;
	private ApplicationInfo application;

	public void setAsid(String value) {
		this.asid = value;
	}

	public void setChangeType(Integer value) {
		this.changeType = value;
	}

	public void setEnterprise(EnterpriseInfo value) {
		this.enterprise = value;
	}

	public void setApplyDate(java.util.Date value) {
		this.applyDate = value;
	}

	public void setReason(String value) {
		this.reason = value;
	}

	public void setStatus(Integer value) {
		this.status = value;
	}

	public void setApproveUser(UserInfo value) {
		this.approveUser = value;
	}

	public void setApproveDate(java.util.Date value) {
		this.approveDate = value;
	}

	public String getAsid() {
		return asid;
	}

	public Integer getChangeType() {
		return changeType;
	}

	public EnterpriseInfo getEnterprise() {
		return enterprise;
	}

	public java.util.Date getApplyDate() {
		return applyDate;
	}

	public String getReason() {
		return reason;
	}

	public Integer getStatus() {
		return status;
	}

	public UserInfo getApproveUser() {
		return approveUser;
	}

	public java.util.Date getApproveDate() {
		return approveDate;
	}

	public String getStatusName() {

		return CommonStatusEnum.values()[status].toString();
	}

	public ApplicationInfo getApplication() {
		return application;
	}

	public void setApplication(ApplicationInfo application) {
		this.application = application;
	}
	
	public String getChangeTypeName() {

		return ApplicationChangeStatus.values()[changeType].toString();
	}

}