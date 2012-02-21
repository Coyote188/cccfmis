package cccf.ma.model;

import java.util.Date;

/**
 * 审核审批对象
 * 
 */
public class ApproveInfo {
	private String id;
	private String approverID;// 审批 、审核人ID
	private String approverName;// 审批 、审核人姓名
	private String approveResult;// 审批 、审核结论
	private String approveMemo;// 备注
	private Date approveDate;// 审批 、审核时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApproverID() {
		return approverID;
	}

	public void setApproverID(String approverID) {
		this.approverID = approverID;
	}

	public String getApproverName() {
		return approverName;
	}

	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}

	public String getApproveResult() {
		return approveResult;
	}

	public void setApproveResult(String approveResult) {
		this.approveResult = approveResult;
	}

	public String getApproveMemo() {
		return approveMemo;
	}

	public void setApproveMemo(String approveMemo) {
		this.approveMemo = approveMemo;
	}

	public Date getApproveDate() {
		return approveDate;
	}

	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}
}
