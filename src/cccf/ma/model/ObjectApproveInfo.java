package cccf.ma.model;

import java.util.Date;

/**
 * 对象审批 、审核内容、结果 
 */
public class ObjectApproveInfo {
    private String id;  
    private String objectType;//对象（实体）类型
    private String objectId;//对象（实体）ID
    private String approveCode;//编码、标识
    private String approverID;//审批 、审核人ID
    private String approverName;//审批 、审核人姓名
    private String approveResult;//审批 、审核结论
    private String approveMemo;//备注
    private Date approveDate;//审批 、审核时间
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getObjectType() {
		return objectType;
	}
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public String getApproveCode() {
		return approveCode;
	}
	public void setApproveCode(String approveCode) {
		this.approveCode = approveCode;
	}
	public String getApproverID() {
		return approverID;
	}
	public void setApproverID(String approverID) {
		this.approverID = approverID;
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
	public String getApproverName() {
		return approverName;
	}
	public void setApproverName(String approverName) {
		this.approverName = approverName;
	} 
}
