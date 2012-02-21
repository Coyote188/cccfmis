package cccf.ma.model;

import java.util.List;

/**
 *  产品 样品（型号） 
 */
public class ProductModel {
	private String id;  
	private ProductCatalogueInfo productCatalogueInfo;//产品分类信息  
	private EnterpriseInfo enterpriseInfo;//企业信息
	private ProductionEnterpriseInfo productionEnterpriseInfo ;//制造商名称
	private ManufactureInfo manufactureInfo ;//生产厂名称
	private SurveyReport surveyReport;//检验报告   
	private String specification;//规格型号
	private String characterization;//特性描述
	private Boolean isMainModel;//主型  1  ,分型 0
	private Boolean whetherSign; //是否被标记
	private List<ProductModelAttachment> productModelAttachment;//附件
	
	private ProductModel mainProductModel;  //主型
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ProductCatalogueInfo getProductCatalogueInfo() {
		return productCatalogueInfo;
	}
	public void setProductCatalogueInfo(ProductCatalogueInfo productCatalogueInfo) {
		this.productCatalogueInfo = productCatalogueInfo;
	}
	public EnterpriseInfo getEnterpriseInfo() {
		return enterpriseInfo;
	}
	public void setEnterpriseInfo(EnterpriseInfo enterpriseInfo) {
		this.enterpriseInfo = enterpriseInfo;
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
	public SurveyReport getSurveyReport() {
		return surveyReport;
	}
	public void setSurveyReport(SurveyReport surveyReport) {
		this.surveyReport = surveyReport;
	}
	public String getSpecification() {
		return specification;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public String getCharacterization() {
		return characterization;
	}
	public void setCharacterization(String characterization) {
		this.characterization = characterization;
	}
	public Boolean getIsMainModel() {
		return isMainModel;
	}
	public void setIsMainModel(Boolean isMainModel) {
		this.isMainModel = isMainModel;
	}
	public List<ProductModelAttachment> getProductModelAttachment() {
		return productModelAttachment;
	}
	public void setProductModelAttachment(
			List<ProductModelAttachment> productModelAttachment) {
		this.productModelAttachment = productModelAttachment;
	}
	 
	public void setWhetherSign(Boolean whetherSign) {
		this.whetherSign = whetherSign;
	}
	public Boolean getWhetherSign() {
		return whetherSign;
	}
	public ProductModel getMainProductModel() {
		return mainProductModel;
	}
	public void setMainProductModel(ProductModel mainProductModel) {
		this.mainProductModel = mainProductModel;
	}
}
