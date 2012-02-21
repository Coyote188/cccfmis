package cccf.ma.model;

/**
 * 产品型号附件审核 
 *
 */
public class ApplProductModelApproveInfo {
	private String id;
	private String name;//  审核环节名称
	private ApplicationInfoProductModel applicationInfoProductModel;
	private ApproveInfo approveInfo;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ApplicationInfoProductModel getApplicationInfoProductModel() {
		return applicationInfoProductModel;
	}
	public void setApplicationInfoProductModel(
			ApplicationInfoProductModel applicationInfoProductModel) {
		this.applicationInfoProductModel = applicationInfoProductModel;
	}
	public ApproveInfo getApproveInfo() {
		return approveInfo;
	}
	public void setApproveInfo(ApproveInfo approveInfo) {
		this.approveInfo = approveInfo;
	} 
}
