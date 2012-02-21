package cccf.ma.model;

import java.util.Date;
 

/**
 * 合同
 */
public class Contract {
	private String id;
	private String contractNo;// 合同编号
	private String agreedAuditTeams;// 合同信息,是否同意派发审核组
	private String auditYear; // 审核时间年
	private String auditMonth; // 审核时间月
	private String preverify; // 是否预审核
	private String preverifyYear;   //预审时间年
	private String preverifyMonth;//预审时时间月
	private String applyno;// 申请号
	private int status; //合同状态
	private Date createDate;//创建时间
	private String creater;//创建人
	private double feeTotal;//收费合计 

	private ApplicationPublicInfo applicationPublicInfo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getAgreedAuditTeams() {
		return agreedAuditTeams;
	}

	public void setAgreedAuditTeams(String agreedAuditTeams) {
		this.agreedAuditTeams = agreedAuditTeams;
	}

	public String getAuditYear() {
		return auditYear;
	}

	public void setAuditYear(String auditYear) {
		this.auditYear = auditYear;
	}

	public String getAuditMonth() {
		return auditMonth;
	}

	public void setAuditMonth(String auditMonth) {
		this.auditMonth = auditMonth;
	}

	public String getPreverify() {
		return preverify;
	}

	public void setPreverify(String preverify) {
		this.preverify = preverify;
	}

	public String getPreverifyYear() {
		return preverifyYear;
	}

	public void setPreverifyYear(String preverifyYear) {
		this.preverifyYear = preverifyYear;
	}

	public String getPreverifyMonth() {
		return preverifyMonth;
	}

	public void setPreverifyMonth(String preverifyMonth) {
		this.preverifyMonth = preverifyMonth;
	}

	public String getApplyno() {
		return applyno;
	}

	public void setApplyno(String applyno) {
		this.applyno = applyno;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public ApplicationPublicInfo getApplicationPublicInfo() {
		return applicationPublicInfo;
	}

	public void setApplicationPublicInfo(ApplicationPublicInfo applicationPublicInfo) {
		this.applicationPublicInfo = applicationPublicInfo;
	}

	public double getFeeTotal() {
		return feeTotal;
	}

	public void setFeeTotal(double feeTotal) {
		this.feeTotal = feeTotal;
	} 
}
