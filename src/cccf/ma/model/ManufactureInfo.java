package cccf.ma.model;

import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;

import cccf.myenum.*;

public class ManufactureInfo {
	private String id;
	private String contactAddress; //通讯地址
	private String contactAddressEn; //_
	private String name; //名称
	private EnterpriseInfo enterprise;
	private Integer status; // 激活状态:0--未激活，1--已激活
	private String statusName;

	// 新添加属性
	private String manuNameEn; 
	private String manucode; //机构代码
	private String manuLegalPerson; //法人
	private String manuType; //类型
	private String manuWorkRange; //经营范围
	private String manuFlag; 
	private String manuCountry; //国家
	private String manuDistrict; //地区
	private String manuAddressEn; //制造商地址英文
	private String manuZip; //制造商邮编
	private String manuEmail; //email
	private String manuLinkMan; //联系人
	private String manuLinkManEn;
	private String manuTel;
	private String manuMob; //手机
	private String manuFax;
	
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
	private String manuEcocalling;
	private String manuEcocallingNew;
	private String manuEcotype;
	private String manuEcoTypeNew;
	
	public void setId(String value) {
		this.id = value;
	}

	public void setContactAddress(String value) {
		this.contactAddress = value;
	}

	public void setName(String value) {
		this.name = value;
	}

	public void setEnterprise(EnterpriseInfo value) {
		this.enterprise = value;
	}

	public void setStatus(Integer value) {
		this.status = value;
	}

	public String getId() {
		return id;
	}

	public String getContactAddress() {
		return contactAddress;
	}

	public String getName() {
		return name;
	}

	public EnterpriseInfo getEnterprise() {
		return enterprise;
	}

	public Integer getStatus() {
		return status;
	}

	public String getStatusName() {

		return ActivateStatus.values()[status].toString();
	}

	public void active() {
		setStatus(ActivateStatus.激活.ordinal());
	}

	public String getManuNameEn() {
		return manuNameEn;
	}

	public void setManuNameEn(String manuNameEn) {
		this.manuNameEn = manuNameEn;
	}

	public String getManucode() {
		return manucode;
	}

	public void setManucode(String manucode) {
		this.manucode = manucode;
	}

	public String getManuLegalPerson() {
		return manuLegalPerson;
	}

	public void setManuLegalPerson(String manuLegalPerson) {
		this.manuLegalPerson = manuLegalPerson;
	}

	public String getManuType() {
		return manuType;
	}

	public void setManuType(String manuType) {
		this.manuType = manuType;
	}

	public String getManuEcocalling() {
		return manuEcocalling;
	}

	public void setManuEcocalling(String manuEcocalling) {
		this.manuEcocalling = manuEcocalling;
	}

	public String getManuEcocallingNew() {
		return manuEcocallingNew;
	}

	public void setManuEcocallingNew(String manuEcocallingNew) {
		this.manuEcocallingNew = manuEcocallingNew;
	}

	public String getManuEcotype() {
		return manuEcotype;
	}

	public void setManuEcotype(String manuEcotype) {
		this.manuEcotype = manuEcotype;
	}

	public String getManuEcoTypeNew() {
		return manuEcoTypeNew;
	}

	public void setManuEcoTypeNew(String manuEcoTypeNew) {
		this.manuEcoTypeNew = manuEcoTypeNew;
	}

	public String getManuWorkRange() {
		return manuWorkRange;
	}

	public void setManuWorkRange(String manuWorkRange) {
		this.manuWorkRange = manuWorkRange;
	}

	public String getManuFlag() {
		return manuFlag;
	}

	public void setManuFlag(String manuFlag) {
		this.manuFlag = manuFlag;
	}

	public String getManuCountry() {
		return manuCountry;
	}

	public void setManuCountry(String manuCountry) {
		this.manuCountry = manuCountry;
	}

	public String getManuDistrict() {
		return manuDistrict;
	}

	public void setManuDistrict(String manuDistrict) {
		this.manuDistrict = manuDistrict;
	}

	public String getManuAddressEn() {
		return manuAddressEn;
	}

	public void setManuAddressEn(String manuAddressEn) {
		this.manuAddressEn = manuAddressEn;
	}

	public String getManuZip() {
		return manuZip;
	}

	public void setManuZip(String manuZip) {
		this.manuZip = manuZip;
	}

	public String getManuEmail() {
		return manuEmail;
	}

	public void setManuEmail(String manuEmail) {
		this.manuEmail = manuEmail;
	}

	public String getManuLinkMan() {
		return manuLinkMan;
	}

	public void setManuLinkMan(String manuLinkMan) {
		this.manuLinkMan = manuLinkMan;
	}

	public String getManuTel() {
		return manuTel;
	}

	public void setManuTel(String manuTel) {
		this.manuTel = manuTel;
	}

	public String getManuFax() {
		return manuFax;
	}

	public void setManuFax(String manuFax) {
		this.manuFax = manuFax;
	}

	public String getOrganizationNature() {
		return organizationNature;
	}

	public int getScale() {
		return scale;
	}

	public String getScaleName() {
		return EnterpriseScale.values()[this.scale].toString();
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

	public String getWebAddress() {
		return webAddress;
	}

	public String getNote() {
		return note;
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

	public void setScaleName(String scaleName) {
		this.scaleName = scaleName;
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

	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getContactAddressEn() {
		return contactAddressEn;
	}

	public String getManuLinkManEn() {
		return manuLinkManEn;
	}

	public String getManuMob() {
		return manuMob;
	}

	public void setContactAddressEn(String contactAddressEn) {
		this.contactAddressEn = contactAddressEn;
	}

	public void setManuLinkManEn(String manuLinkManEn) {
		this.manuLinkManEn = manuLinkManEn;
	}

	public void setManuMob(String manuMob) {
		this.manuMob = manuMob;
	}

}