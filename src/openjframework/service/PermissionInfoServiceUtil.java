package openjframework.service;

import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import openjframework.model.*;
import com.aidi.core.web.framework.ServiceAdapter;
import com.aidi.core.spring.CustomerContextHolder;

public class PermissionInfoServiceUtil {
	public static Serializable create(PermissionInfo bean) {
		CustomerContextHolder.setCustomerType("cccf");
		PermissionInfoService service = (PermissionInfoService) ServiceAdapter
				.getServiceByName("PermissionInfoService");
		return service.create(bean);
	}

	public static void delete(PermissionInfo bean) {
		CustomerContextHolder.setCustomerType("cccf");
		PermissionInfoService service = (PermissionInfoService) ServiceAdapter
				.getServiceByName("PermissionInfoService");
		service.delete(bean);
	}

	public static void saveOrUpdate(PermissionInfo bean) {
		CustomerContextHolder.setCustomerType("cccf");
		PermissionInfoService service = (PermissionInfoService) ServiceAdapter
				.getServiceByName("PermissionInfoService");
		service.saveOrUpdate(bean);
	}

	public static void update(PermissionInfo bean) {
		CustomerContextHolder.setCustomerType("cccf");
		PermissionInfoService service = (PermissionInfoService) ServiceAdapter
				.getServiceByName("PermissionInfoService");
		service.update(bean);
	}

	public static List getAll() {
		CustomerContextHolder.setCustomerType("cccf");
		PermissionInfoService service = (PermissionInfoService) ServiceAdapter
				.getServiceByName("PermissionInfoService");
		return service.getAll();
	}

	public static PermissionInfo getById(String id) {
		CustomerContextHolder.setCustomerType("cccf");
		PermissionInfoService service = (PermissionInfoService) ServiceAdapter
				.getServiceByName("PermissionInfoService");
		return service.getById(id);
	}

	public static PermissionInfo getByPrimaryKey(String key) {
		CustomerContextHolder.setCustomerType("cccf");
		PermissionInfoService service = (PermissionInfoService) ServiceAdapter
				.getServiceByName("PermissionInfoService");
		return service.getByPrimaryKey(key);
	}

	public static List findByQuery(String querystr) {
		CustomerContextHolder.setCustomerType("cccf");
		PermissionInfoService service = (PermissionInfoService) ServiceAdapter
				.getServiceByName("PermissionInfoService");
		return service.findByQuery(querystr);
	}

	public static PermissionInfo getByResourceId(String id) {
		PermissionInfo permissionInfo = null;
		List list = findByQuery("from PermissionInfo where resource.id='" + id
				+ "'");
		if (list != null) {
			if (list.size() > 0) {
				permissionInfo = new PermissionInfo();
				permissionInfo = (PermissionInfo) list.get(0);
			}

		}
		return permissionInfo;
	}

	public static PermissionInfo getByResourceName(String name) {
		PermissionInfo permissionInfo = null;
		List list = findByQuery("from PermissionInfo where resource.name='"
				+ name + "'");
		if (list != null) {
			if (list.size() > 0) {
				permissionInfo = new PermissionInfo();
				permissionInfo = (PermissionInfo) list.get(0);
			}

		}
		return permissionInfo;
	}

	/**得到当前用户对资源的可用性
	 * @param resoueName
	 * @return
	 */
	public static boolean getEmabledByResoueNameOfLoginUser(String resoueName) {
		boolean isEmabled = false;
		PermissionInfo permissionInfo = getByResourceName(resoueName);
		if (permissionInfo != null) {
			UserInfo loginUser = UserInfoServiceUtil.getCurrentLoginUser();
			Set<RoleInfo> roleSet = permissionInfo.getRoleList();
			Set<RoleInfo> userRoleSet = loginUser.getRoleList();
			
			if (roleSet != null && userRoleSet != null) {				
				List userRoleIds=new ArrayList();
				for(RoleInfo r:userRoleSet){
					userRoleIds.add(r.getId());
				}
								
				for (RoleInfo role : roleSet) {
					if(userRoleIds.contains(role.getId())){
						String pstr = String.valueOf(permissionInfo.getPermissionType());
						if (pstr.substring(0, 1).equals("1")) {
							isEmabled = true;
						}
					}
				}
			}
			
		} else {
			isEmabled = true;// 如果该资源没进行设定则为可用
		}
		return isEmabled;
	}
}
