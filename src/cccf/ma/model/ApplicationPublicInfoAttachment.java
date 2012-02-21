package cccf.ma.model;
/**
 * @author 周金雄
 * @E-mail JOINYO@YEAH.NET
 * @date 创建于2011-5-17上午10:26:24
 * @version 1.0

 * @describe
 * ALL RIGHTS RESERVED,COPYRIGHT(C) FCH LIMITED 2011
 */
public class ApplicationPublicInfoAttachment {
	private String id;
    private ApplicationPublicInfo applicationPublicInfo;
    private Attachment attachment;
    private String name;    //附件名称
    private int sn;//附件序号
    private String status;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ApplicationPublicInfo getApplicationPublicInfo() {
		return applicationPublicInfo;
	}
	public void setApplicationPublicInfo(ApplicationPublicInfo applicationPublicInfo) {
		this.applicationPublicInfo = applicationPublicInfo;
	}
	public Attachment getAttachment() {
		return attachment;
	}
	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSn() {
		return sn;
	}
	public void setSn(int sn) {
		this.sn = sn;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	} 
}
