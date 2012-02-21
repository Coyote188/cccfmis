package cccf.ma.model;

import java.util.Date;

/**
 * 技术评定报告  评定人意见
 */
public class TechnicalEvaluationReportOpinion {
	private String id;
    private TechnicalEvaluationReport technicalEvaluationReport; 
    private String phase;//评定阶段 ：报告生成、技术初评、技术复评、技术评定 
    private String approveResult;//评定结果
	private String opinionContent;// 意见内容
	private String memo;//备   注
	private String evaluationPersonId;//评定人
	private Date evaluationTime;//评定时间  
	private int sn;//排列顺序
	private int status;//状态 0未执行，1：已评定
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public TechnicalEvaluationReport getTechnicalEvaluationReport() {
		return technicalEvaluationReport;
	}
	public void setTechnicalEvaluationReport(
			TechnicalEvaluationReport technicalEvaluationReport) {
		this.technicalEvaluationReport = technicalEvaluationReport;
	}
	public String getPhase() {
		return phase;
	}
	public void setPhase(String phase) {
		this.phase = phase;
	}
	public String getOpinionContent() {
		return opinionContent;
	}
	public void setOpinionContent(String opinionContent) {
		this.opinionContent = opinionContent;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getEvaluationPersonId() {
		return evaluationPersonId;
	}
	public void setEvaluationPersonId(String evaluationPersonId) {
		this.evaluationPersonId = evaluationPersonId;
	}
	public Date getEvaluationTime() {
		return evaluationTime;
	}
	public void setEvaluationTime(Date evaluationTime) {
		this.evaluationTime = evaluationTime;
	}
	public int getSn() {
		return sn;
	}
	public void setSn(int sn) {
		this.sn = sn;
	}
	public String getApproveResult() {
		return approveResult;
	}
	public void setApproveResult(String approveResult) {
		this.approveResult = approveResult;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
