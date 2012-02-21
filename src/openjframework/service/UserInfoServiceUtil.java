package openjframework.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;

import openjframework.model.*;

import com.aidi.core.web.framework.ServiceAdapter;
import com.aidi.core.spring.CustomerContextHolder;
public class UserInfoServiceUtil
{
    public static Serializable  create(UserInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
       UserInfoService service=(UserInfoService)ServiceAdapter.getServiceByName("UserInfoService");
       return service.create(bean);
    }
    public static void delete(UserInfo bean){
    	CustomerContextHolder.setCustomerType("cccf");
        UserInfoService service=(UserInfoService)ServiceAdapter.getServiceByName("UserInfoService");
       service.delete(bean);
       }
    public static void saveOrUpdate(UserInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        UserInfoService service=(UserInfoService)ServiceAdapter.getServiceByName("UserInfoService");
       service.saveOrUpdate(bean);
       }
    public static void update(UserInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        UserInfoService service=(UserInfoService)ServiceAdapter.getServiceByName("UserInfoService");
       service.update(bean);
       }
    public static List getAll(){
       CustomerContextHolder.setCustomerType("cccf");
        UserInfoService service=(UserInfoService)ServiceAdapter.getServiceByName("UserInfoService");
       return service.getAll();
       }
    public static UserInfo getById(String id){
    	CustomerContextHolder.setCustomerType("cccf");
        UserInfoService service=(UserInfoService)ServiceAdapter.getServiceByName("UserInfoService");
       return service.getById(id);
       }
     public static UserInfo getByPrimaryKey(String key){
    	CustomerContextHolder.setCustomerType("cccf");
        UserInfoService service=(UserInfoService)ServiceAdapter.getServiceByName("UserInfoService");
       return service.getByPrimaryKey(key);
       }
    public static List findByQuery(String querystr){
        CustomerContextHolder.setCustomerType("cccf");
        UserInfoService service=(UserInfoService)ServiceAdapter.getServiceByName("UserInfoService");
       return service.findByQuery(querystr);
       }
    // create a new account with his profile
    public static void createAccount(UserInfo account,ProfileInfo profile){
    	profile.setUser(account);
    	create(account);
    	ProfileInfoServiceUtil.create(profile);
    }
    public static void updateAccount(UserInfo account,ProfileInfo profile){
    	update(account);
    	ProfileInfoServiceUtil.update(profile);
    }
    //get by username
    public static UserInfo getByUserName(String username){
    	String querystr = "from UserInfo a where a.username ='"+username+"'";
    	List<UserInfo> list=findByQuery(querystr);
    	UserInfo account=null;
    	if(list!=null&&list.size()>0)
    		account=list.get(0);
    	return account;
    }
    public static List<UserInfo> getByRoleName(String roleName)
    {
    	String querystr = "select prof.user from  ProfileInfo prof,RoleInfo role where  role in elements(prof.roleList) and  role.name='"
						+ roleName+"'";
    	return findByQuery(querystr);
    }
    //get current user account
    public static UserInfo getCurrentLoginUser(){
    	final Execution exec = Executions.getCurrent();
		HttpServletRequest req = (HttpServletRequest) exec.getNativeRequest();
		HttpSession session = req.getSession();
		String userName = (String) session.getAttribute("username");
		UserInfo user = getByUserName(userName);
    	return user;
    }
    //get account by department
    public static List<UserInfo> getByRoleDept(String roleName, String deptName) {
		List list = findByQuery("select  prof.user from ProfileInfo prof,RoleInfo role where role in elements(user.roleList) and  role.name='"
						+ roleName
						+ "' and  prof.organization.orgName='"
						+ deptName + "'");

		return list;
	}
    
    
    /**
	 * 判断用户是否存在
	 * 
	 * @param usernameStr
	 * @param passwordStr
	 * @return
	 */
    public static boolean isExists(String usernameStr, String passwordStr) {
		if (getByUserName(usernameStr) == null)
			return false;
		else if (getByUserName(usernameStr).getPassword().equals(passwordStr))
			return true;
		else
			return false;
	}

	public static boolean isExistByUsername(String username) {
		UserInfo u = getByUserName(username);
		if (u == null) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 *检索用户,根据类型
	 * 
	 * @param type
	 *            0-评定中心,1-企业用户,2-系统管理员
	 * @return
	 */
	public static List<UserInfo> getByUserType(int type) {
		List<UserInfo> list = findByQuery("from UserInfo where type=" + type);
		return list;
	}
	
	public static UserInfo findByUsername(String username){
		List<UserInfo> list = findByQuery("from UserInfo where username = '" + username + "'");
		return list.isEmpty() ?null:list.get(0);
	}
	
	
}
   