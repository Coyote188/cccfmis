package cccf.ma.model;

import java.util.Date;
import java.util.List;

/**
 *  派组批次
 *
 */
public class AssignBatch {
	private String id;
	private String batchNo;//批次编号
	private String batchName;//批次名称
	private String createrId;//创建人ID
	private Date createDate;//创建时间
	private int productCount;//产品数量
	private int status;   //状态
	private String memo ;//备注
	
	private List<FactoryCheckTask> checkTasks;//任务列表
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public int getProductCount() {
		return productCount;
	}
	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getBatchName() {
		return batchName;
	}
	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}
	public String getCreaterId() {
		return createrId;
	}
	public void setCreaterId(String createrId) {
		this.createrId = createrId;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public List<FactoryCheckTask> getCheckTasks() {
		return checkTasks;
	}
	public void setCheckTasks(List<FactoryCheckTask> checkTasks) {
		this.checkTasks = checkTasks;
	}
	
	
}
