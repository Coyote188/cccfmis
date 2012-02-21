package cccf.ma.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import openjframework.model.UserInfo;
import cccf.myenum.ApplicationStatusEnum;

public class ApplicationInfo
{
	private String						id;
	private ProductCatalogueInfo		production;
	private EnterpriseInfo				enterprise;
	private String						address;
	 
	public void setModels(List<ProductionModelInfo> models)
	{
		this.models = models;
	}
	private String						businessLisence;
	private String						organizationCode;
	private String						inspectionDeviceList;
	private String						layoutGraph;
	private String						brand;
	private String						qualityManual;
	private String						originalCertificate;
	private Integer						stauts;
	private Integer						stauts0	= -1;
	private String						sioid;
	// 工厂检查
	private String						factoryInspection;		// none 没有 must 需要
	// 批次计划者
	private String						planner;
	// 批次参与者
	private String						participants;
	// 组长
	private String						leader;
	// 批次ID
	private long						batchid;
	private Set<ProductionModelInfo>	productionModel;
	private List<ProductionModelInfo>	models;
	private PumperDocumentsInfo			pumperDocuments;
	// 申请日期
	private java.util.Date				applyDate;
	private ManufactureInfo				manufacture;
	private UserInfo					reveiwUser;			// 符合性审查人员
	private UserInfo					acceptUser;			// 受理分工人
	private Long						processInstanceId;
	private ProductionEnterpriseInfo	productionEnterprise;
	private String						contractFileUrl;
	private float						amountFee;
	// 合同条款5中的选择项
	private String						contractChoice1;
	private String						contractChoice2;
	private String						contractYear;
	private String						contractMonth;
	private String						contractNo;
	private String						ysYear;				// 预审年
	private String						ysMonth;
	private String						reportUser;
	// 审请类型
	private String						apptype;
	// 业务类型
	private String						businesstype ,showuser;
	//公共类
	private ApplicationPublicInfo applicationPublic;
	
	public ApplicationPublicInfo getApplicationPublic() {
		return applicationPublic;
	}
	public void setApplicationPublic(ApplicationPublicInfo applicationPublic) {
		this.applicationPublic = applicationPublic;
	}
	public String getShowuser()
	{
		return showuser;
	}
	public void setShowuser(String showuser)
	{
		this.showuser = showuser;
	}
	public UserInfo getAcceptUser()
	{
		return acceptUser;
	}
	public String getAddress()
	{
		return address;
	}
	public float getAmountFee()
	{
		return amountFee;
	}
	public java.util.Date getApplyDate()
	{
		return applyDate;
	}
	public String getApptype()
	{
		return apptype;
	}
	public long getBatchid()
	{
		return batchid;
	}
	public String getBrand()
	{
		return brand;
	}
	public String getBusinessLisence()
	{
		return businessLisence;
	}
	public String getBusinesstype()
	{
		return businesstype;
	}
	public String getContractChoice1()
	{
		return contractChoice1;
	}
	public String getContractChoice2()
	{
		return contractChoice2;
	}
	public String getContractFileUrl()
	{
		return contractFileUrl;
	}
	public String getContractMonth()
	{
		return contractMonth;
	}
	public String getContractNo()
	{
		return contractNo;
	}
	public String getContractYear()
	{
		return contractYear;
	}
	public EnterpriseInfo getEnterprise()
	{
		return enterprise;
	}
	public String getFactoryInspection()
	{
		return factoryInspection;
	}
	public String[] getFieldNameArr()
	{
		String[] fileFieldArr = { "businessLisence", "organizationCode", "inspectionDeviceList", "layoutGraph", "brand", "qualityManual", "originalCertificate" };
		return fileFieldArr;
	}
	public String[] getFileUrlArr()
	{
		String[] fileUrlArr = new String[7];
		fileUrlArr[0] = this.getBusinessLisence();
		fileUrlArr[1] = this.getOrganizationCode();
		fileUrlArr[2] = this.getInspectionDeviceList();
		fileUrlArr[3] = this.getLayoutGraph();
		fileUrlArr[4] = this.getBrand();
		fileUrlArr[5] = this.getQualityManual();
		fileUrlArr[6] = this.getOriginalCertificate();
		return fileUrlArr;
	}
	public String getId()
	{
		return id;
	}
	public String getInspectionDeviceList()
	{
		return inspectionDeviceList;
	}
	public String getLayoutGraph()
	{
		return layoutGraph;
	}
	public String getLeader()
	{
		return leader;
	}
	public ManufactureInfo getManufacture()
	{
		return manufacture;
	}
	public List<ProductionModelInfo> getModels()
	{
		models = new ArrayList<ProductionModelInfo>();
		for (ProductionModelInfo info : productionModel)
		{
			// info.getModel()
			models.add(info);
		}
		return models;
	}
	public String getOrganizationCode()
	{
		return organizationCode;
	}
	public String getOriginalCertificate()
	{
		return originalCertificate;
	}
	public String getParticipants()
	{
		return participants;
	}
	public String getPlanner()
	{
		return planner;
	}
	public Long getProcessInstanceId()
	{
		return processInstanceId;
	}
	public ProductCatalogueInfo getProduction()
	{
		return production;
	}
	public ProductionEnterpriseInfo getProductionEnterprise()
	{
		return productionEnterprise;
	}
	public Set getProductionModel()
	{
		return productionModel;
	}
	public PumperDocumentsInfo getPumperDocuments()
	{
		return pumperDocuments;
	}
	public String getQualityManual()
	{
		return qualityManual;
	}
	public String getReportUser()
	{
		return this.reportUser;
	}
	public UserInfo getReveiwUser()
	{
		return reveiwUser;
	}
	public String getSioid()
	{
		return sioid;
	}
	// 如果已经变更则返回变更后的名称
	public String getStatusName()
	{
		if (stauts0 == -1)
			return ApplicationStatusEnum.values()[stauts].toString();
		else
			return ApplicationStatusEnum.values()[stauts0].toString();
	}
	public Integer getStauts()
	{
		return stauts;
	}
	public Integer getStauts0()
	{
		return stauts0;
	}
	public String getYsMonth()
	{
		return ysMonth;
	}
	public String getYsYear()
	{
		return ysYear;
	}
	public void setAcceptUser(UserInfo acceptUser)
	{
		this.acceptUser = acceptUser;
	}
	public void setAddress(String value)
	{
		this.address = value;
	}
	public void setAmountFee(float amountFee)
	{
		this.amountFee = amountFee;
	}
	public void setApplyDate(java.util.Date value)
	{
		this.applyDate = value;
	}
	public void setApptype(String apptype)
	{
		this.apptype = apptype;
	}
	public void setBatchid(long batchid)
	{
		this.batchid = batchid;
	}
	public void setBrand(String value)
	{
		this.brand = value;
	}
	public void setBusinessLisence(String value)
	{
		this.businessLisence = value;
	}
	public void setBusinesstype(String businesstype)
	{
		this.businesstype = businesstype;
	}
	public void setContractChoice1(String contractChoice1)
	{
		this.contractChoice1 = contractChoice1;
	}
	public void setContractChoice2(String contractChoice2)
	{
		this.contractChoice2 = contractChoice2;
	}
	public void setContractFileUrl(String contractFileUrl)
	{
		this.contractFileUrl = contractFileUrl;
	}
	public void setContractMonth(String contractMonth)
	{
		this.contractMonth = contractMonth;
	}
	public void setContractNo(String contractNo)
	{
		this.contractNo = contractNo;
	}
	public void setContractYear(String contractYear)
	{
		this.contractYear = contractYear;
	}
	public void setEnterprise(EnterpriseInfo value)
	{
		this.enterprise = value;
	}
	public void setFactoryInspection(String factoryInspection)
	{
		this.factoryInspection = factoryInspection;
	}
	public void setId(String value)
	{
		this.id = value;
	}
	public void setInspectionDeviceList(String value)
	{
		this.inspectionDeviceList = value;
	}
	public void setLayoutGraph(String value)
	{
		this.layoutGraph = value;
	}
	public void setLeader(String leader)
	{
		this.leader = leader;
	}
	public void setManufacture(ManufactureInfo manufacture)
	{
		this.manufacture = manufacture;
	}
	public void setOrganizationCode(String value)
	{
		this.organizationCode = value;
	}
	public void setOriginalCertificate(String value)
	{
		this.originalCertificate = value;
	}
	public void setParticipants(String participants)
	{
		this.participants = participants;
	}
	public void setPlanner(String planner)
	{
		this.planner = planner;
	}
	public void setProcessInstanceId(Long processInstanceId)
	{
		this.processInstanceId = processInstanceId;
	}
	public void setProduction(ProductCatalogueInfo value)
	{
		this.production = value;
	}
	public void setProductionEnterprise(ProductionEnterpriseInfo productionEnterprise)
	{
		this.productionEnterprise = productionEnterprise;
	}
	public void setProductionModel(Set value)
	{
		this.productionModel = value;
	}
	public void setPumperDocuments(PumperDocumentsInfo pumperDocuments)
	{
		this.pumperDocuments = pumperDocuments;
	}
	public void setQualityManual(String value)
	{
		this.qualityManual = value;
	}
	public void setReportUser(String reportUser)
	{
		this.reportUser = reportUser;
	}
	public void setReveiwUser(UserInfo reveiwUser)
	{
		this.reveiwUser = reveiwUser;
	}
	public void setSioid(String sioid)
	{
		this.sioid = sioid;
	}
	public void setStauts(Integer value)
	{
		this.stauts = value;
	}
	public void setStauts0(Integer stauts0)
	{
		this.stauts0 = stauts0;
	}
	public void setYsMonth(String ysMonth)
	{
		this.ysMonth = ysMonth;
	}
	public void setYsYear(String ysYear)
	{
		this.ysYear = ysYear;
	}
}