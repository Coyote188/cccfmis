package cccf.ma.model;

/**
 *  申请附件审核
 *
 */
public class AppPubAttachmentApproveInfo {
	private String id;
	private String name;//  审核环节名称
	private ApplicationPublicInfoAttachment applicationPublicInfoAttachment;
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
	public ApplicationPublicInfoAttachment getApplicationPublicInfoAttachment() {
		return applicationPublicInfoAttachment;
	}
	public void setApplicationPublicInfoAttachment(
			ApplicationPublicInfoAttachment applicationPublicInfoAttachment) {
		this.applicationPublicInfoAttachment = applicationPublicInfoAttachment;
	}
	public ApproveInfo getApproveInfo() {
		return approveInfo;
	}
	public void setApproveInfo(ApproveInfo approveInfo) {
		this.approveInfo = approveInfo;
	}
	 
	
}
