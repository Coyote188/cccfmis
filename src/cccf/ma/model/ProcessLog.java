package cccf.ma.model;

import java.util.Date;

/**
 * 过程日志 
 */
public class ProcessLog {
	private String id;
	private String boType;// 对象（实体）类型
	private String boId;// 对象（实体）ID
	private String boStatus;// 对象（实体）当前状态
	private String boNextStatus;// 对象（实体）下一步状态状态
	private long processInstanceId;// 流程ID
	private long taskInstanceId; // 任务ID
	private Long previousTaskInstanceId; // 上一不任务ID
	private String taskInstanceName; // 任务名称
	private Date create = null; // 任务创建时间
	private String createrId;// 创建人ID
	private Date end = null; // 任务结束时间
	private String actorId = null; // 任务处理人ID
	private String approveResult;   //任务处理结果（流程流向）
	private String approveMemo;// 备注 
	 
	public ProcessLog(){
			super();
    }
	public ProcessLog(String boType, String boId ){
		super();
		this.boType = boType;
		this.boId = boId; 
	}
	public ProcessLog(String boType, String boId, String boStatus){
		super();
		this.boType = boType;
		this.boId = boId;
		this.boStatus = boStatus;
	}
	public ProcessLog(String boType, String boId, String boStatus,String boNextStatus) {
		super();
		this.boType = boType;
		this.boId = boId;
		this.boStatus = boStatus;
		this.boNextStatus = boNextStatus;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBoType() {
		return boType;
	}
	public void setBoType(String boType) {
		this.boType = boType;
	}
	public String getBoId() {
		return boId;
	}
	public void setBoId(String boId) {
		this.boId = boId;
	}
	public String getBoStatus() {
		return boStatus;
	}
	public void setBoStatus(String boStatus) {
		this.boStatus = boStatus;
	}
	public String getBoNextStatus() {
		return boNextStatus;
	}
	public void setBoNextStatus(String boNextStatus) {
		this.boNextStatus = boNextStatus;
	}
	public long getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public long getTaskInstanceId() {
		return taskInstanceId;
	}
	public void setTaskInstanceId(long taskInstanceId) {
		this.taskInstanceId = taskInstanceId;
	}
	public Long getPreviousTaskInstanceId() {
		return previousTaskInstanceId;
	}
	public void setPreviousTaskInstanceId(Long previousTaskInstanceId) {
		this.previousTaskInstanceId = previousTaskInstanceId;
	}
	public String getTaskInstanceName() {
		return taskInstanceName;
	}
	public void setTaskInstanceName(String taskInstanceName) {
		this.taskInstanceName = taskInstanceName;
	}
	public Date getCreate() {
		return create;
	}
	public void setCreate(Date create) {
		this.create = create;
	}
	public String getCreaterId() {
		return createrId;
	}
	public void setCreaterId(String createrId) {
		this.createrId = createrId;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public String getActorId() {
		return actorId;
	}
	public void setActorId(String actorId) {
		this.actorId = actorId;
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
 
}
