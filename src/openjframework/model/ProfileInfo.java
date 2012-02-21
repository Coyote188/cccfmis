package openjframework.model;

import java.util.*;
import openjframework.myenum.SexEnum;

public class ProfileInfo
{
	private String				id;
	private String				name;
	private String				userNo;
	private Integer				gender;
	private String				sexname;
	private String				nativePlace;
	private java.util.Date		birthday;
	private String				idcardNo;
	private PoliticalStatusInfo	politicalStatus;
	private PositionInfo		position;
	private OrganizationInfo	organization;
	private Set					roleList;
	private String				email;
	private String				telephone;
	private UserInfo			user;
	public void setId(String value)
	{
		this.id = value;
	}
	public void setName(String value)
	{
		this.name = value;
	}
	public void setUserNo(String value)
	{
		this.userNo = value;
	}
	public void setGender(Integer value)
	{
		this.gender = value;
	}
	public void setNativePlace(String value)
	{
		this.nativePlace = value;
	}
	public void setBirthday(java.util.Date value)
	{
		this.birthday = value;
	}
	public void setIdcardNo(String value)
	{
		this.idcardNo = value;
	}
	public void setPoliticalStatus(PoliticalStatusInfo value)
	{
		this.politicalStatus = value;
	}
	public void setPosition(PositionInfo value)
	{
		this.position = value;
	}
	public void setOrganization(OrganizationInfo value)
	{
		this.organization = value;
	}
	public void setRoleList(Set value)
	{
		this.roleList = value;
	}
	public void setEmail(String value)
	{
		this.email = value;
	}
	public void setUser(UserInfo value)
	{
		this.user = value;
	}
	public String getTelephone()
	{
		return telephone;
	}
	public void setTelephone(String telephone)
	{
		this.telephone = telephone;
	}
	public String getId()
	{
		return id;
	}
	public String getName()
	{
		return name;
	}
	public String getUserNo()
	{
		return userNo;
	}
	public Integer getGender()
	{
		return gender;
	}
	public String getNativePlace()
	{
		return nativePlace;
	}
	public java.util.Date getBirthday()
	{
		return birthday;
	}
	public String getIdcardNo()
	{
		return idcardNo;
	}
	public PoliticalStatusInfo getPoliticalStatus()
	{
		return politicalStatus;
	}
	public PositionInfo getPosition()
	{
		return position;
	}
	public OrganizationInfo getOrganization()
	{
		return organization;
	}
	public Set getRoleList()
	{
		return roleList;
	}
	public String getEmail()
	{
		return email;
	}
	public UserInfo getUser()
	{
		return user;
	}
	public String getSexname()
	{
		try
		{
			return SexEnum.values()[gender].toString();
		} catch (Exception e)
		{
			return null;
		}
	}
}