package cccf.ma.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.aidi.core.service.BaseDAOServcieUtil;

public class FactoryInspection {
	private String id;
	private String facInspectionNo;
	private Date startTime;
	private Date endTime;
	private int businessType;
	private int typeOfInspection;
	private String checkGroup;
	
	private int factoryConditions; //工厂检查条件
	private int equipmentConditions; //生产设备、检验设备
	private int certificateUsage; //证书使用情况
	private int flagUsage; //标志使用情况
	
	private boolean facConditionChange; //工厂条件变化情况
	private int fullyMeet,baseMeet,unMeet;
	private String inspectionCenter;
	
	private String unMeetDescription;
	private String inspectionResult;
	private String inspectionResultDescription;
	
	private Set<ProductionModelInspection> productionModelIspection;
	private List<FactoryChecklist> enterpriseCheckList;
	private SurveyReport surveyReport;
	
	private ApplicationInfo application;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFacInspectionNo() {
		return facInspectionNo;
	}
	public void setFacInspectionNo(String facInspectionNo) {
		this.facInspectionNo = facInspectionNo;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public int getBusinessType() {
		return businessType;
	}
	public void setBusinessType(int businessType) {
		this.businessType = businessType;
	}
	public int getTypeOfInspection() {
		return typeOfInspection;
	}
	public void setTypeOfInspection(int typeOfInspection) {
		this.typeOfInspection = typeOfInspection;
	}
	public String getCheckGroup() {
		return checkGroup;
	}
	public void setCheckGroup(String checkGroup) {
		this.checkGroup = checkGroup;
	}
	public int getFactoryConditions() {
		return factoryConditions;
	}
	public void setFactoryConditions(int factoryConditions) {
		this.factoryConditions = factoryConditions;
	}
	public int getEquipmentConditions() {
		return equipmentConditions;
	}
	public void setEquipmentConditions(int equipmentConditions) {
		this.equipmentConditions = equipmentConditions;
	}
	public int getCertificateUsage() {
		return certificateUsage;
	}
	public void setCertificateUsage(int certificateUsage) {
		this.certificateUsage = certificateUsage;
	}
	public int getFlagUsage() {
		return flagUsage;
	}
	public void setFlagUsage(int flagUsage) {
		this.flagUsage = flagUsage;
	}
	public boolean isFacConditionChange() {
		return facConditionChange;
	}
	public void setFacConditionChange(boolean facConditionChange) {
		this.facConditionChange = facConditionChange;
	}
	public int getFullyMeet() {
		return fullyMeet;
	}
	public void setFullyMeet(int fullyMeet) {
		this.fullyMeet = fullyMeet;
	}
	public int getBaseMeet() {
		return baseMeet;
	}
	public void setBaseMeet(int baseMeet) {
		this.baseMeet = baseMeet;
	}
	public int getUnMeet() {
		return unMeet;
	}
	public void setUnMeet(int unMeet) {
		this.unMeet = unMeet;
	}
	public void setApplication(ApplicationInfo application) {
		this.application = application;
	}
	public ApplicationInfo getApplication() {
		return application;
	}
	public void setProductionModelIspection(Set<ProductionModelInspection> productionModelIspection) {
		this.productionModelIspection = productionModelIspection;
	}
	public Set<ProductionModelInspection> getProductionModelIspection() {
		return productionModelIspection;
	}
	public String getUnMeetDescription() {
		return unMeetDescription;
	}
	public String getInspectionResult() {
		return inspectionResult;
	}
	public String getInspectionResultDescription() {
		return inspectionResultDescription;
	}
	public void setUnMeetDescription(String unMeetDescription) {
		this.unMeetDescription = unMeetDescription;
	}
	public void setInspectionResult(String inspectionResult) {
		this.inspectionResult = inspectionResult;
	}
	public void setInspectionResultDescription(String inspectionResultDescription) {
		this.inspectionResultDescription = inspectionResultDescription;
	}
	public void setInspectionCenter(String inspectionCenter) {
		this.inspectionCenter = inspectionCenter;
	}
	public String getInspectionCenter() {
		return inspectionCenter;
	}
	public void setEnterpriseCheckList(List<FactoryChecklist> enterpriseCheckList) {
		this.enterpriseCheckList = enterpriseCheckList;
	}
	public List<FactoryChecklist> getEnterpriseCheckList() {
		String queryStr = "FROM FactoryChecklist";
		return BaseDAOServcieUtil.findByQueryString(queryStr);
	}
	public void setSurveyReport(SurveyReport surveyReport) {
		this.surveyReport = surveyReport;
	}
	public SurveyReport getSurveyReport() {
		String queryStr = "FROM ApplicationInfoProductModel a WHERE a.";
//		BaseDAOServcieUtil.findByQueryString(queryString)
		return surveyReport;
	}
}
