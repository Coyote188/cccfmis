package cccf.ma.model;

import java.util.Date;
import java.util.List;

/**
 * 批次任务
 * 
 */
public class FactoryCheckTask {
	private String id;
	private String taskNo;// 任务号
	private String applyNo;//申请号
	private String batchNo;//批次号 
	private AssignBatch assignBatch; // 批次
	private ApplicationPublicInfo applicationPublicInfo;// 申请 
    private Date assignDate;//派组日期
	private double assignNum;//派组天数
	private int status;//状态
	private String memo;//备注
	
	private List<FactoryCheckTaskUser> checkUsers;//检查人员列表
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTaskNo() {
		return taskNo;
	}
	public void setTaskNo(String taskNo) {
		this.taskNo = taskNo;
	}
	public String getApplyNo() {
		return applyNo;
	}
	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public AssignBatch getAssignBatch() {
		return assignBatch;
	}
	public void setAssignBatch(AssignBatch assignBatch) {
		this.assignBatch = assignBatch;
	}
	public ApplicationPublicInfo getApplicationPublicInfo() {
		return applicationPublicInfo;
	}
	public void setApplicationPublicInfo(ApplicationPublicInfo applicationPublicInfo) {
		this.applicationPublicInfo = applicationPublicInfo;
	}
	public Date getAssignDate() {
		return assignDate;
	}
	public void setAssignDate(Date assignDate) {
		this.assignDate = assignDate;
	}
	public double getAssignNum() {
		return assignNum;
	}
	public void setAssignNum(double assignNum) {
		this.assignNum = assignNum;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public List<FactoryCheckTaskUser> getCheckUsers() {
		return checkUsers;
	}
	public void setCheckUsers(List<FactoryCheckTaskUser> checkUsers) {
		this.checkUsers = checkUsers;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	} 
	
}
