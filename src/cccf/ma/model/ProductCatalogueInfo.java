package cccf.ma.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import openjframework.model.UserInfo;
import cccf.ma.service.ProductCatalogueInfoServiceUtil;
import cccf.myenum.ActivateStatus;
import cccf.myenum.ProductEnabledStatus;

public class ProductCatalogueInfo {
	public static final String businessType_strong = "1"; //强制性认证
	public static final String businessType_form = "0";   //形式认可
 

	/**
	 * productNo 产品编号 productName 产品名称 productParent 产品父 productChildren 产品子
	 * productLevel 产品级 activateStatus 产品状态 observedStandard 执行标准
	 * technicalRequirement 技术要求 certificatesNo 证书编码 productDescribe 产品描述
	 * modelStatus 是否允许分型号 fireEngineStatus 是否消防车 productPerson 产品人
	 * certificationTypeNo 对应证书分类编号 businessType 业务类型 systemStandards 体系标准
	 * caRule认证规则
	 */
	private String id;
	private String productNo;
	private String productName;
	private ProductCatalogueInfo productParent;
	private Set productChildren;
	private Integer productLevel;
	private Integer activateStatus;
	private String enabledStatus;
	private String activateStatusName;
	private String observedStandard;
	private String technicalRequirement;
	private String certificatesNo;
	private String productDescribe;
	private boolean modelStatus;
	private boolean fireEngineStatus;
	private UserInfo productPerson;
	private Set userArray;
	private List<ProductCatalogueInfo> allChildren;
	private String simpleName;// 简称
	private int simpleNameLen = 6;// 简称默认6个字
	
	private String productNameEn;//产品名称(英文)
	private String certificationTypeNo;//对应证书分类编号
	private String businessType;//业务类型 强制性认证，型式认可
	private String systemStandards;//体系标准
	private String caRule; //认证规则

	public Set getUserArray() {
		return userArray;
	}

	public void setUserArray(Set userArray) {
		this.userArray = userArray;
	}

	public void setId(String value) {
		this.id = value;
	}

	public void setProductNo(String value) {
		this.productNo = value;
	}

	public void setProductName(String value) {
		this.productName = value;
	}

	public void setProductParent(ProductCatalogueInfo value) {
		this.productParent = value;
	}

	public void setProductChildren(Set value) {
		this.productChildren = value;
	}

	public void setProductLevel(Integer value) {
		this.productLevel = value;
	}

	public void setActivateStatus(Integer value) {
		this.activateStatus = value;
	}

	public void setObservedStandard(String value) {
		this.observedStandard = value;
	}

	public void setTechnicalRequirement(String value) {
		this.technicalRequirement = value;
	}

	public void setCertificatesNo(String value) {
		this.certificatesNo = value;
	}

	public void setProductDescribe(String value) {
		this.productDescribe = value;
	}

	public String getId() {
		return id;
	}

	public String getProductNo() {
		return productNo;
	}

	public String getProductName() {
		return productName;
	}

	public ProductCatalogueInfo getProductParent() {
		return productParent;
	}

	public Set getProductChildren() {
		return productChildren;
	}

	public Integer getProductLevel() {
		return productLevel;
	}

	public Integer getActivateStatus() {
		return activateStatus;
	}

	public String getObservedStandard() {
		return observedStandard;
	}

	public String getTechnicalRequirement() {
		return technicalRequirement;
	}

	public String getCertificatesNo() {
		return certificatesNo;
	}

	public String getProductDescribe() {
		return productDescribe;
	}

	public boolean isFireEngineStatus() {
		return fireEngineStatus;
	}

	public boolean isModelStatus() {
		return modelStatus;
	}

	public UserInfo getProductPerson() {
		return productPerson;
	}

	public void setProductPerson(UserInfo productPerson) {
		this.productPerson = productPerson;
	}

	public void setModelStatus(boolean modelStatus) {
		this.modelStatus = modelStatus;
	}

	public void setFireEngineStatus(boolean fireEngineStatus) {
		this.fireEngineStatus = fireEngineStatus;
	}

	public String getEnabledStatus() {
		return ProductEnabledStatus.values()[activateStatus].toString();
	}

	// 产品树显示
	public String getActivateStatusName() {
		return ActivateStatus.values()[activateStatus].toString();
	}

	/**
	 * 增加产品
	 * 
	 * @param productCatalogue
	 */
	@SuppressWarnings("unchecked")
	public void addChildProduct(ProductCatalogueInfo productCatalogue) {

		if (productCatalogue == null)

			throw new IllegalArgumentException();

		if (productCatalogue.getProductParent() != null)

			productCatalogue.getProductParent().getProductChildren()
					.remove(productCatalogue);

		productCatalogue.setProductParent(this);

		this.getProductChildren().add(productCatalogue);

	}

	public List<ProductCatalogueInfo> getAllChildren() {
		allChildren = new ArrayList<ProductCatalogueInfo>();
		traversalselfChildren(this);
		return allChildren;
	}

	public void traversalselfChildren(ProductCatalogueInfo product) {
		List<ProductCatalogueInfo> chlist = new ArrayList<ProductCatalogueInfo>(
				product.getProductChildren());
		if (chlist != null && chlist.size() > 0) {
			for (ProductCatalogueInfo _product : chlist) {
				allChildren.add(_product);
				traversalselfChildren(_product);
			}

		}
		return;
	}

	public String getSimpleName() {
		if (productName.length() > simpleNameLen)
			simpleName = (new StringBuilder(String.valueOf(productName
					.substring(0, simpleNameLen)))).toString();
		else
			simpleName = productName;
		return simpleName;
	}

	public int getSimpleNameLen() {
		return simpleNameLen;
	}

	public void setSimpleNameLen(int simpleNameLen) {
		this.simpleNameLen = simpleNameLen;
	}

	public String getCertificationTypeNo() {
		return certificationTypeNo;
	}

	public void setCertificationTypeNo(String certificationTypeNo) {
		this.certificationTypeNo = certificationTypeNo;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getSystemStandards() {
		return systemStandards;
	}

	public void setSystemStandards(String systemStandards) {
		this.systemStandards = systemStandards;
	}

	public String getCaRule() {
		return caRule;
	}

	public void setCaRule(String caRule) {
		this.caRule = caRule;
	}

	public String getProductNameEn() {
		return productNameEn;
	}

	public void setProductNameEn(String productNameEn) {
		this.productNameEn = productNameEn;
	}

}