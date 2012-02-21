package cccf.ma.model;

import java.util.Date;

/**
 * 技术评定 评定人意见
 */
public class TechnicalEvaluationOpinion {
	private String id;
    private TechnicalEvaluation technicalEvaluation; 
    private String evaluationPhase;//评定阶段 ：报告生成、技术初评、技术复评、技术评定 
	private String opinionContent;// 意见内容
	private String memo;//备   注
	private String evaluationPersonId;//评定人
	private Date evaluationTime;//评定时间
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public TechnicalEvaluation getTechnicalEvaluation() {
		return technicalEvaluation;
	}
	public void setTechnicalEvaluation(TechnicalEvaluation technicalEvaluation) {
		this.technicalEvaluation = technicalEvaluation;
	}
	public String getEvaluationPhase() {
		return evaluationPhase;
	}
	public void setEvaluationPhase(String evaluationPhase) {
		this.evaluationPhase = evaluationPhase;
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
	
}
