package cccf.ma.model;

import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;

import cccf.myenum.ActivateStatus;
import cccf.myenum.EnterpriseScale;

public class ProductionEnterpriseInfo {
	private String id;
	private Integer status; // 激活状态:0--未激活，1--已激活
	private EnterpriseInfo enterprise;
	private String statusName; 

	// new property
	private String name;//名称
	private String facNameEn; //英文名
	private String facCode; //组织机构代码
	private String facLegalPerson; //法人
	private String facType; //机构类型

	private String facWorkRange; //经营范围
	private String facFlag; //标志
	private String facCountry; //所在国家地区
	private String facDistrict; //所在地区
	private String contactAddress; //通讯地址
	private String contactAddressEn; //通讯地址英文_
	private String facAddressEn; //地址(英文)
	private String facZip; //邮编
	private String facEmail; //电子邮件
	private String facLinkMan; //联系人
	private String facLinkManEn; //_
	private String facTel; //联系电话
	private String facMob; //_
	private String facFax; //传真
	private String facCoding; //编码
	
	//生产厂独有
	private String realAdd; //实际生产厂地址
	private String realAddEn; 
	private String realZip; //实际生产厂邮编
	
	/*
	 * 新属性
	 */
	private String organizationNature; //组织性质代码_
	private int scale; //规模_
	private String scaleName;
	private String legalPersonEn; //_
	private String contactAddForLegal; //法人地址_
	private String contactAddForLegalEn;// _
	private String telForLegal; //电话_
	private String mobileForLegal; //手机_
	private String registeredAddress; //注册地址_
	private String registeredAddressEn;//_
	private String registeredCapital; //注册资金_
	private String businessScope; //经营范围_
//	private String contactPersonEn;//_
	private String webAddress; //网址_
	private String note; //简介

	//停用
	private String facEcocalling;//经济行业
	private String facEcocallingNew; //经济行业(新国标代码)
	private String facEcotype; //经济类型
	private String facEcoTypeNew; //经济类型(新国标代码)
	
	public void setId(String value) {
		this.id = value;
	}

	public void setName(String value) {
		this.name = value;
	}

	public void setContactAddress(String value) {
		this.contactAddress = value;
	}

	public void setStatus(Integer value) {
		this.status = value;
	}

	public void setEnterprise(EnterpriseInfo value) {
		this.enterprise = value;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getContactAddress() {
		return contactAddress;
	}

	public Integer getStatus() {
		return status;
	}

	public EnterpriseInfo getEnterprise() {
		return enterprise;
	}

	public String getStatusName() {
		return ActivateStatus.values()[status].toString();
	}

	public void active() {
		setStatus(ActivateStatus.激活.ordinal());
	}

	public String getFacNameEn() {
		return facNameEn;
	}

	public void setFacNameEn(String facNameEn) {
		this.facNameEn = facNameEn;
	}

	public String getFacCode() {
		return facCode;
	}

	public void setFacCode(String faccode) {
		this.facCode = faccode;
	}

	public String getFacLegalPerson() {
		return facLegalPerson;
	}

	public void setFacLegalPerson(String facLegalPerson) {
		this.facLegalPerson = facLegalPerson;
	}

	public String getFacType() {
		return facType;
	}

	public void setFacType(String facType) {
		this.facType = facType;
	}

	public String getFacEcocalling() {
		return facEcocalling;
	}

	public void setFacEcocalling(String facEcocalling) {
		this.facEcocalling = facEcocalling;
	}

	public String getFacEcocallingNew() {
		return facEcocallingNew;
	}

	public void setFacEcocallingNew(String facEcocallingNew) {
		this.facEcocallingNew = facEcocallingNew;
	}

	public String getFacEcotype() {
		return facEcotype;
	}

	public void setFacEcotype(String facEcotype) {
		this.facEcotype = facEcotype;
	}

	public String getFacEcoTypeNew() {
		return facEcoTypeNew;
	}

	public void setFacEcoTypeNew(String facEcoTypeNew) {
		this.facEcoTypeNew = facEcoTypeNew;
	}

	public String getFacWorkRange() {
		return facWorkRange;
	}

	public void setFacWorkRange(String facWorkRange) {
		this.facWorkRange = facWorkRange;
	}

	public String getFacFlag() {
		return facFlag;
	}

	public void setFacFlag(String facFlag) {
		this.facFlag = facFlag;
	}

	public String getFacCountry() {
		return facCountry;
	}

	public void setFacCountry(String facCountry) {
		this.facCountry = facCountry;
	}

	public String getFacDistrict() {
		return facDistrict;
	}

	public void setFacDistrict(String facDistrict) {
		this.facDistrict = facDistrict;
	}

	public String getFacAddressEn() {
		return facAddressEn;
	}

	public void setFacAddressEn(String facAddressEn) {
		this.facAddressEn = facAddressEn;
	}

	public String getFacZip() {
		return facZip;
	}

	public void setFacZip(String facZip) {
		this.facZip = facZip;
	}

	public String getFacEmail() {
		return facEmail;
	}

	public void setFacEmail(String facEmail) {
		this.facEmail = facEmail;
	}

	public String getFacLinkMan() {
		return facLinkMan;
	}

	public void setFacLinkMan(String facLinkMan) {
		this.facLinkMan = facLinkMan;
	}

	public String getFacTel() {
		return facTel;
	}

	public void setFacTel(String facTel) {
		this.facTel = facTel;
	}

	public String getFacFax() {
		return facFax;
	}

	public void setFacFax(String facFax) {
		this.facFax = facFax;
	}

	public void setFacCoding(String facCoding) {
		this.facCoding = facCoding;
	}

	public String getFacCoding() {
		return facCoding;
	}

	public String getOrganizationNature() {
		return organizationNature;
	}

	public int getScale() {
		return scale;
	}

	public String getLegalPersonEn() {
		return legalPersonEn;
	}

	public String getContactAddForLegal() {
		return contactAddForLegal;
	}

	public String getContactAddForLegalEn() {
		return contactAddForLegalEn;
	}

	public String getTelForLegal() {
		return telForLegal;
	}

	public String getMobileForLegal() {
		return mobileForLegal;
	}

	public String getRegisteredAddress() {
		return registeredAddress;
	}

	public String getRegisteredAddressEn() {
		return registeredAddressEn;
	}

	public String getRegisteredCapital() {
		return registeredCapital;
	}

	public String getBusinessScope() {
		return businessScope;
	}

//	public String getContactPersonEn() {
//		return contactPersonEn;
//	}

	public String getWebAddress() {
		return webAddress;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public void setOrganizationNature(String organizationNature) {
		this.organizationNature = organizationNature;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public void setLegalPersonEn(String legalPersonEn) {
		this.legalPersonEn = legalPersonEn;
	}

	public void setContactAddForLegal(String contactAddForLegal) {
		this.contactAddForLegal = contactAddForLegal;
	}

	public void setContactAddForLegalEn(String contactAddForLegalEn) {
		this.contactAddForLegalEn = contactAddForLegalEn;
	}

	public void setTelForLegal(String telForLegal) {
		this.telForLegal = telForLegal;
	}

	public void setMobileForLegal(String mobileForLegal) {
		this.mobileForLegal = mobileForLegal;
	}

	public void setRegisteredAddress(String registeredAddress) {
		this.registeredAddress = registeredAddress;
	}

	public void setRegisteredAddressEn(String registeredAddressEn) {
		this.registeredAddressEn = registeredAddressEn;
	}

	public void setRegisteredCapital(String registeredCapital) {
		this.registeredCapital = registeredCapital;
	}

	public void setBusinessScope(String businessScope) {
		this.businessScope = businessScope;
	}

//	public void setContactPersonEn(String contactPersonEn) {
//		this.contactPersonEn = contactPersonEn;
//	}

	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
	}

	public String getContactAddressEn() {
		return contactAddressEn;
	}

	public String getFacLinkManEn() {
		return facLinkManEn;
	}

	public String getFacMob() {
		return facMob;
	}

	public String getRealAdd() {
		return realAdd;
	}

	public String getRealAddEn() {
		return realAddEn;
	}

	public String getRealZip() {
		return realZip;
	}

	public String getNote() {
		return note;
	}

	public void setContactAddressEn(String contactAddressEn) {
		this.contactAddressEn = contactAddressEn;
	}

	public void setFacLinkManEn(String facLinkManEn) {
		this.facLinkManEn = facLinkManEn;
	}

	public void setFacMob(String facMob) {
		this.facMob = facMob;
	}

	public void setRealAdd(String realAdd) {
		this.realAdd = realAdd;
	}

	public void setRealAddEn(String realAddEn) {
		this.realAddEn = realAddEn;
	}

	public void setRealZip(String realZip) {
		this.realZip = realZip;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setScaleName(String scaleName) {
		this.scaleName = scaleName;
	}

	public String getScaleName() {
		return EnterpriseScale.values()[this.scale].toString();
	}

}