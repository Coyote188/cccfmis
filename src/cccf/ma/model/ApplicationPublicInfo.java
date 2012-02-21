package cccf.ma.model;

import java.util.Date;
import java.util.List;

import com.aidi.core.service.BaseDAOServcieUtil;

/**
 * ID(id), 申请号(applyno), 申请类型(applyType), 业务类型(businessType),
 * 
 * 
 * 企业营业执照(businessLicense), 代码证(codeCertificate), 检验清单(inspectionChecklist),
 * 配置平面图(dispositionPlan), 质量手册和程序文件(programFiles),
 * 原产品认证证书复印件(productCertificate),
 * 
 * @author lenovo
 * 
 */
public class ApplicationPublicInfo {
	private String id;
	private String applyno;//申请号
	private String applyType;//申请类型
	private String businessType;//业务类型
	private Date applydate;//申请时间
	private String productCatalog;//产品大类
	private String contractAgree;//合同信息,是否同意派发审核组
	private String contractYear;//合同信息,审核时间年
	private String contractMonth;//合同信息,审核时间月
	private String contractApprove;//合同信息,是否预审核
	private EnterpriseInfo applyEnterprise;//申请企业
	private List<Attachment> attachments;//附件_列表
	private List<ApplicationInfo> applications;//
	
	private EnterpriseInfo enterprise;
	
	public EnterpriseInfo getApplyEnterprise() {
		return this.applyEnterprise;
	}
	
	public EnterpriseInfo getEnterprise(){
		String queryStr = "SELECT applyEnterprise FROM ApplicationPublicInfo WHERE id = '" +this.getId()+ "'" ;
		return (EnterpriseInfo) BaseDAOServcieUtil.findByQueryString(queryStr).get(0);
	}

	public void setApplyEnterprise(EnterpriseInfo applyEnterprise) {
		this.applyEnterprise = applyEnterprise;
	}

	public List<ApplicationInfo> getApplications() {
		return applications;
	}

	public void setApplications(List<ApplicationInfo> applications) {
		this.applications = applications;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public String getContractAgree() {
		return contractAgree;
	}

	public void setContractAgree(String contractAgree) {
		this.contractAgree = contractAgree;
	}

	public String getContractYear() {
		return contractYear;
	}

	public void setContractYear(String contractYear) {
		this.contractYear = contractYear;
	}

	public String getContractMonth() {
		return contractMonth;
	}

	public void setContractMonth(String contractMonth) {
		this.contractMonth = contractMonth;
	}

	public String getContractApprove() {
		return contractApprove;
	}

	public void setContractApprove(String contractApprove) {
		this.contractApprove = contractApprove;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	public Date getApplydate() {
		return applydate;
	}

	public void setApplydate(Date applydate) {
		this.applydate = applydate;
	}

	public String getProductCatalog() {
		return productCatalog;
	}

	public void setProductCatalog(String productCatalog) {
		this.productCatalog = productCatalog;
	}

	public String getId() {
		return id;
	}

	public String getApplyno() {
		return applyno;
	}

	public String getApplyType() {
		return applyType;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setId(String value) {
		this.id = value;
	}

	public void setApplyno(String value) {
		this.applyno = value;
	}

	public void setApplyType(String value) {
		this.applyType = value;
	}

	public void setBusinessType(String value) {
		this.businessType = value;
	}

	public void setEnterprise(EnterpriseInfo enterprise) {
		this.enterprise = enterprise;
	}

}