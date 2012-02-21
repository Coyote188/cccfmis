package cccf.ma.model; 

import java.util.List;

import openjframework.model.OrganizationInfo;

/**
 *  检验报告 
 */
public class SurveyReport {
	private String id;  
	private EnterpriseInfo enterpriseInfo;//企业信息
	private ProductCatalogueInfo productCatalogueInfo;//产品分类信息
	private OrganizationInfo organizationInfo;//检验机构名称
	private String surverOrgName;  //检验机构名称
	private String surveyReportSN; //检验报告编号
	private String surveyFromDate; //检验开始日期
	private String surveyToDate;   //检验结束日期 
	private String surverAccording; //检验依据
	private int surverVerdict;//检验结论——合格、不合格
	private String surverVerdictContent ;//检验结论——内容
	private String issuingDate;//签发日期
	private String surverType;//检验类型
	private ProductionEnterpriseInfo productionEnterpriseInfo ;//制造商名称
	private ManufactureInfo manufactureInfo ;//生产厂名称
	private String agentOrg; //委托单位
    private String samplingOrg;//抽样单位
	private String organizer;//编制人
	private String organizeTime;//编制时间
	private String auditor;//审核人
	private String auditTime;//审核时间
	private int status;//状态  ： 新增未审核 0 、审核通过 1、审核不通过 2  /未审核(重提交)3
	
	private Attachment attachment; //附件
	
	private List<ProductModel> productModelList;//产品规格型号
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public EnterpriseInfo getEnterpriseInfo() {
		return enterpriseInfo;
	}
	public void setEnterpriseInfo(EnterpriseInfo enterpriseInfo) {
		this.enterpriseInfo = enterpriseInfo;
	}
	public ProductCatalogueInfo getProductCatalogueInfo() {
		return productCatalogueInfo;
	}
	public void setProductCatalogueInfo(ProductCatalogueInfo productCatalogueInfo) {
		this.productCatalogueInfo = productCatalogueInfo;
	}
	public String getSurveyReportSN() {
		return surveyReportSN;
	}
	public void setSurveyReportSN(String surveyReportSN) {
		this.surveyReportSN = surveyReportSN;
	}
	public String getSurveyFromDate() {
		return surveyFromDate;
	}
	public void setSurveyFromDate(String surveyFromDate) {
		this.surveyFromDate = surveyFromDate;
	}
	public String getSurveyToDate() {
		return surveyToDate;
	}
	public void setSurveyToDate(String surveyToDate) {
		this.surveyToDate = surveyToDate;
	}
	public String getSurverOrgName() {
		return surverOrgName;
	}
	public void setSurverOrgName(String surverOrgName) {
		this.surverOrgName = surverOrgName;
	}
	public String getSurverAccording() {
		return surverAccording;
	}
	public void setSurverAccording(String surverAccording) {
		this.surverAccording = surverAccording;
	}
	public int getSurverVerdict() {
		return surverVerdict;
	}
	public void setSurverVerdict(int surverVerdict) {
		this.surverVerdict = surverVerdict;
	}
	public String getSurverVerdictContent() {
		return surverVerdictContent;
	}
	public void setSurverVerdictContent(String surverVerdictContent) {
		this.surverVerdictContent = surverVerdictContent;
	}
	public String getIssuingDate() {
		return issuingDate;
	}
	public void setIssuingDate(String issuingDate) {
		this.issuingDate = issuingDate;
	}
	public String getSurverType() {
		return surverType;
	}
	public void setSurverType(String surverType) {
		this.surverType = surverType;
	}
	public ProductionEnterpriseInfo getProductionEnterpriseInfo() {
		return productionEnterpriseInfo;
	}
	public void setProductionEnterpriseInfo(
			ProductionEnterpriseInfo productionEnterpriseInfo) {
		this.productionEnterpriseInfo = productionEnterpriseInfo;
	}
	public ManufactureInfo getManufactureInfo() {
		return manufactureInfo;
	}
	public void setManufactureInfo(ManufactureInfo manufactureInfo) {
		this.manufactureInfo = manufactureInfo;
	}
	public String getAgentOrg() {
		return agentOrg;
	}
	public void setAgentOrg(String agentOrg) {
		this.agentOrg = agentOrg;
	}
	public String getSamplingOrg() {
		return samplingOrg;
	}
	public void setSamplingOrg(String samplingOrg) {
		this.samplingOrg = samplingOrg;
	}
	public String getOrganizer() {
		return organizer;
	}
	public void setOrganizer(String organizer) {
		this.organizer = organizer;
	}
	public String getOrganizeTime() {
		return organizeTime;
	}
	public void setOrganizeTime(String organizeTime) {
		this.organizeTime = organizeTime;
	}
	public String getAuditor() {
		return auditor;
	}
	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}
	public String getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public List<ProductModel> getProductModelList() {
		return productModelList;
	}
	public void setProductModelList(List<ProductModel> productModelList) {
		this.productModelList = productModelList;
	}
	public Attachment getAttachment() {
		return attachment;
	}
	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}
	public OrganizationInfo getOrganizationInfo() {
		return organizationInfo;
	}
	public void setOrganizationInfo(OrganizationInfo organizationInfo) {
		this.organizationInfo = organizationInfo;
	}  
}
