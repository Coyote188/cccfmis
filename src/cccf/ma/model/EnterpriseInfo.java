package cccf.ma.model;

import openjframework.model.*;
import java.util.*;

import com.aidi.core.service.BaseDAOServcieUtil;

import cccf.myenum.ActivateStatus;
import cccf.myenum.EnterpriseScale;

public class EnterpriseInfo {
	private String id;
	
	private String name; //企业名称
	private String nameEn;//_
	private String state; //国家
	private String location; //地区
	private String organizationCode; //组织机构代码证_
	private String organizationNature; //组织性质代码_
	private int scale; //规模_
	private String scaleName;
	private String legalPerson ; //法人_
	private String legalPersonEn; //_
	private String contactAddForLegal; //法人地址_
	private String contactAddForLegalEn;// _
	private String telForLegal; //电话_
	private String mobileForLegal; //手机_
	private String registeredAddress; //注册地址_
	private String registeredAddressEn;//_
	private String registeredCapital; //注册资金_
	private String businessScope; //经营范围_
	private String contactAddress; //通讯地址
	private String contactAddressEn;//_
	private String contactPerson; //联系人
	private String contactPersonEn;//_
	private String telephoneNum; //联系电话
	private String mobileNum; //手机_
	private String faxNum; //传真
	private String emailAddress; //email
	private String postalcode; //邮编
	private String webAddress; //网址_
	private String copyOfBusinessLicense; //营业执照复印件
	
	private String note; //介绍_
	private List<Attachment> attachments;//附件_
	//	private 
	private String atts;
	
	
	
	
	//
	private List<ManufactureInfo> manufacture; //制造商
	private List<ProductionEnterpriseInfo> pEnterprise; //生硬厂
	private UserInfo account; //账户
	private Integer status; //状态
	private int simpleNameLen=6; 
	private String simpleName;
	
	private String addForView;

	public void setId(String value) {
		this.id = value;
	}

	public void setName(String value) {
		this.name = value;
	}

	public void setState(String value) {
		this.state = value;
	}

	public void setLocation(String value) {
		this.location = value;
	}

	public void setContactAddress(String value) {
		this.contactAddress = value;
	}

	public void setContactPerson(String value) {
		this.contactPerson = value;
	}

	public void setTelephoneNum(String value) {
		this.telephoneNum = value;
	}

	public void setFaxNum(String value) {
		this.faxNum = value;
	}

	public void setPostalcode(String value) {
		this.postalcode = value;
	}

	public void setEmailAddress(String value) {
		this.emailAddress = value;
	}

	public void setCopyOfBusinessLicense(String value) {
		this.copyOfBusinessLicense = value;
	}

	public void setAccount(UserInfo value) {
		this.account = value;
	}

	public void setStatus(Integer value) {
		this.status = value;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getState() {
		return state;
	}

	public String getLocation() {
		return location;
	}

	public String getContactAddress() {
		return contactAddress;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public String getTelephoneNum() {
		return telephoneNum;
	}

	public String getFaxNum() {
		return faxNum;
	}

	public String getPostalcode() {
		return postalcode;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getCopyOfBusinessLicense() {
		return copyOfBusinessLicense;
	}

	public UserInfo getAccount() {
		return account;
	}

	public Integer getStatus() {
		return status;
	}

	public void active() {
		setStatus(ActivateStatus.激活.ordinal());
	}

	public void refused() {
		setStatus(ActivateStatus.退回.ordinal());
	}

	public List<ProductionEnterpriseInfo> getPEnterprise() {
		return pEnterprise;
	}

	public void setPEnterprise(List<ProductionEnterpriseInfo> enterprise) {
		pEnterprise = enterprise;
	}

	public void setManufacture(List<ManufactureInfo> manufacture) {
		this.manufacture = manufacture;
	}

	public List<ManufactureInfo> getManufacture() {
		return manufacture;
	}

	public String getSimpleName() {
		if (name.length() > simpleNameLen)
			simpleName = (new StringBuilder(String.valueOf(name.substring(0,
					simpleNameLen)))).toString();
		else
			simpleName = name;
		return simpleName;
	}

	public int getSimpleNameLen() {
		return simpleNameLen;
	}

	public void setSimpleNameLen(int simpleNameLen) {
		this.simpleNameLen = simpleNameLen;
	}

	public String getNameEn() {
		return nameEn;
	}

	public String getOrganizationCode() {
		return organizationCode;
	}

	public String getOrganizationNature() {
		return organizationNature;
	}

	public int getScale() {
		return scale;
	}

	public String getLegalPerson() {
		return legalPerson;
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

	public String getContactAddressEn() {
		return contactAddressEn;
	}

	public String getContactPersonEn() {
		return contactPersonEn;
	}

	public String getMobileNum() {
		return mobileNum;
	}

	public String getWebAddress() {
		return webAddress;
	}

	public String getNote() {
		return note;
	}

	public List<Attachment> getAttachments() {
		String[] atts = getAtts().split(",");
		List<Attachment> temp = new ArrayList<Attachment>();
		for(String att : atts){
			if(!att.equals("") || null != null){
				String queryStr = "FROM Attachment WHERE id="+ att +"'";
				temp.add((Attachment)BaseDAOServcieUtil.findById(Attachment.class, att));
			}
		}
		return temp;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public void setOrganizationNature(String organizationNature) {
		this.organizationNature = organizationNature;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
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

	public void setContactAddressEn(String contactAddressEn) {
		this.contactAddressEn = contactAddressEn;
	}

	public void setContactPersonEn(String contactPersonEn) {
		this.contactPersonEn = contactPersonEn;
	}

	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}

	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setAttachments(List<Attachment> attachments) {
		String str = "";
		for(Attachment a : attachments){
			str += a.getId() + ",";
		}
		this.atts = str;
	}

	public void setScaleName(String scaleName) {
		this.scaleName = scaleName;
	}

	public String getScaleName() {
		
		return EnterpriseScale.values()[this.scale].toString();
	}

	public String getAddForView() {
		String location = this.getState() + this.getLocation();
		return location;
	}

	public void setAddForView(String addForView) {
		this.addForView = addForView;
	}

	public void setAtts(String atts) {
		this.atts = atts;
	}

	public String getAtts() {
		return atts;
	}

}