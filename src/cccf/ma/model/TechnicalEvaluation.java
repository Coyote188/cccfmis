package cccf.ma.model;
 

/**
 * 技术评定
 */
public class TechnicalEvaluation {
	private String id;
	private ApplicationInfo applicationInfo; 
    private String certificateValid ;//证书有效期  ： 无、三年、五年、原证书数截止日期
	private String entTollCode;//企业人数代码  ： M、S、L
	private String operateType;//操作类型 ：发证 、发通知  
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ApplicationInfo getApplicationInfo() {
		return applicationInfo;
	}
	public void setApplicationInfo(ApplicationInfo applicationInfo) {
		this.applicationInfo = applicationInfo;
	}
	public String getCertificateValid() {
		return certificateValid;
	}
	public void setCertificateValid(String certificateValid) {
		this.certificateValid = certificateValid;
	}
	public String getEntTollCode() {
		return entTollCode;
	}
	public void setEntTollCode(String entTollCode) {
		this.entTollCode = entTollCode;
	}
	public String getOperateType() {
		return operateType;
	}
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	} 
	 
}
