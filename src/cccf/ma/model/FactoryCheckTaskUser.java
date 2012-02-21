package cccf.ma.model;

/**
 * 工厂检查任务人员
 * 
 */
public class FactoryCheckTaskUser {
	private String id;
	private FactoryCheckTask assignBatchTask;
	private FactoryCheckUser factoryCheckUser;//工厂检查人员
	private int headmanSign;//组长标记，1：组长，0：组员 
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public FactoryCheckTask getAssignBatchTask() {
		return assignBatchTask;
	}
	public void setAssignBatchTask(FactoryCheckTask assignBatchTask) {
		this.assignBatchTask = assignBatchTask;
	}
	public FactoryCheckUser getFactoryCheckUser() {
		return factoryCheckUser;
	}
	public void setFactoryCheckUser(FactoryCheckUser factoryCheckUser) {
		this.factoryCheckUser = factoryCheckUser;
	}
	public int getHeadmanSign() {
		return headmanSign;
	}
	public void setHeadmanSign(int headmanSign) {
		this.headmanSign = headmanSign;
	}
	
}
