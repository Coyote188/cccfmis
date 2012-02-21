package openjframework.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import openjframework.model.ProfileInfo;
import openjframework.model.RoleInfo;

import com.aidi.core.spring.CustomerContextHolder;
import com.aidi.core.web.framework.ServiceAdapter;

public class RoleInfoServiceUtil {
	public static Serializable create(RoleInfo bean) {
		CustomerContextHolder.setCustomerType("dltaxoa");
		RoleInfoService service = (RoleInfoService) ServiceAdapter
				.getServiceByName("RoleInfoService");
		return service.create(bean);
	}

	public static void delete(RoleInfo bean) {
		CustomerContextHolder.setCustomerType("dltaxoa");
		RoleInfoService service = (RoleInfoService) ServiceAdapter
				.getServiceByName("RoleInfoService");
		service.delete(bean);
	}

	public static void saveOrUpdate(RoleInfo bean) {
		CustomerContextHolder.setCustomerType("dltaxoa");
		RoleInfoService service = (RoleInfoService) ServiceAdapter
				.getServiceByName("RoleInfoService");
		service.saveOrUpdate(bean);
	}

	public static void update(RoleInfo bean) {
		CustomerContextHolder.setCustomerType("dltaxoa");
		RoleInfoService service = (RoleInfoService) ServiceAdapter
				.getServiceByName("RoleInfoService");
		service.update(bean);
	}

	public static List getAll() {
		CustomerContextHolder.setCustomerType("dltaxoa");
		RoleInfoService service = (RoleInfoService) ServiceAdapter
				.getServiceByName("RoleInfoService");
		return service.getAll();
	}

	public static RoleInfo getById(String id) {
		CustomerContextHolder.setCustomerType("dltaxoa");
		RoleInfoService service = (RoleInfoService) ServiceAdapter
				.getServiceByName("RoleInfoService");
		return service.getById(id);
	}

	public static RoleInfo getByPrimaryKey(String key) {
		CustomerContextHolder.setCustomerType("dltaxoa");
		RoleInfoService service = (RoleInfoService) ServiceAdapter
				.getServiceByName("RoleInfoService");
		return service.getByPrimaryKey(key);
	}

	public static List findByQuery(String querystr) {
		CustomerContextHolder.setCustomerType("dltaxoa");
		RoleInfoService service = (RoleInfoService) ServiceAdapter
				.getServiceByName("RoleInfoService");
		return service.findByQuery(querystr);
	}
	
	public static RoleInfo findByName(String name) {
		String querystr="from RoleInfo where name='"+name+"'";
		List list=findByQuery(querystr);
		RoleInfo role=null;
		if(list!=null){
			if(list.size()>0){
				role=(RoleInfo)list.get(0);
			}
		}
		return role;
	}
	public static boolean isExistByProfileInfo(ProfileInfo profile,RoleInfo role)
	{
		Set roleset=profile.getRoleList();
		List<RoleInfo>rolelist=new ArrayList(roleset);
		if(rolelist!=null&&rolelist.size()>0)
		for(RoleInfo ro:rolelist)
		{
			if(ro.getName().equals(role.getName()))
				return true;
		}
		return false;
		
	}
	public static boolean isRoleExist(String name){
		String str = "from RoleInfo r where r.name = '"+ name +"'";
		return findByQuery(str).isEmpty()?false:true;
	}
	public static List<ProfileInfo> findProfileByRole(RoleInfo role){
		String querystr = "select p.userList from Role as r where r.id = '"+ role.getId() +"'";
		return findByQuery(querystr);
	}
}
