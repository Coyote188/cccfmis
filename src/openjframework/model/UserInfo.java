package openjframework.model;

import java.util.List;
import java.util.Set;

import openjframework.myenum.UserStatusEnum;
import openjframework.myenum.UserTypeEnum;
import openjframework.service.ProfileInfoServiceUtil;

public class UserInfo {
	public int getTemp() {
		return temp;
	}

	public void setTemp(int temp) {
		this.temp = temp;
	}

	public String getNameAndorganization() {
		String orgname=organization==null?"单位不明":organization.getOrganizationName();
		return name+"<"+orgname+">";
	}
	private String id;
	private String username;
	private String password;
	private Integer type;
	private String typename;
	private Integer status;
	private String statusname;
	private String name;
	private int temp;
	private String nameAndorganization;
	private OrganizationInfo organization;
	private Set roleList;

	public void setId(String value) {
		this.id = value;
	}

	public void setUsername(String value) {
		this.username = value;
	}

	public void setPassword(String value) {
		this.password = value;
	}

	public void setType(Integer value) {
		this.type = value;
	}

	public void setStatus(Integer value) {
		this.status = value;
	}

	public void setName(String value) {
		this.name = value;
	}

	public String getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public Integer getType() {
		return type;
	}

	public Integer getStatus() {
		return status;
	}

	public String getName() {
		return name;
	}

	public String getTypename() {
		return UserTypeEnum.values()[type].toString();
	}

	public OrganizationInfo getOrganization() {
		return getProfile().getOrganization();
	}

	public Set getRoleList() {
		return getProfile().getRoleList();
	}

	public String getStatusname() {
		return UserStatusEnum.values()[status].toString();
	}

	public ProfileInfo getProfile() {
		String querystr = "from ProfileInfo p where user.id= '" + getId() + "'";
		List list = ProfileInfoServiceUtil.findByQuery(querystr);
		ProfileInfo p = new ProfileInfo();
		if (list != null)
			if (list.size() > 0)
				p = (ProfileInfo) list.get(0);
		
		return p;
	}

}