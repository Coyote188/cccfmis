package cccf.ma.model;

import java.util.Date;

/**
 * 技术评定报告 
 *
 */
public class TechnicalEvaluationReport {
	private String id;
	private String reportNo;//评定报告 编号 
    private String certificateValid ;//证书有效期  ： 无、三年、五年、原证书数截止日期
	private String entTollCode;//企业人数代码  ： M、S、L
	private String operateType;//操作类型 ：发证 、发通知   
	
	private String toSymposium;//上会讨论
	private String symposiumDate;//上会日期
	private String participant;//参与人
	private String opinion;//意见
	
	private String applyno;// 申请号
	private int status; // 状态
	private Date createDate;//创建时间
	private String creater;//创建人 
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getReportNo() {
		return reportNo;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	public String getCertificateValid() {
		return certificateValid;
	}
	public void setCertificateValid(String certificateValid) {
		this.certificateValid = certificateValid;
	}
	public String getEntTollCode() {
		return entTollCode;
	}
	public void setEntTollCode(String entTollCode) {
		this.entTollCode = entTollCode;
	}
	public String getOperateType() {
		return operateType;
	}
	public void setOperateType(String operateType) {
		this.operateType = operateType;
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
	public String getToSymposium() {
		return toSymposium;
	}
	public void setToSymposium(String toSymposium) {
		this.toSymposium = toSymposium;
	}
	public String getSymposiumDate() {
		return symposiumDate;
	}
	public void setSymposiumDate(String symposiumDate) {
		this.symposiumDate = symposiumDate;
	}
	public String getParticipant() {
		return participant;
	}
	public void setParticipant(String participant) {
		this.participant = participant;
	}
	public String getOpinion() {
		return opinion;
	}
	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}
	
}
