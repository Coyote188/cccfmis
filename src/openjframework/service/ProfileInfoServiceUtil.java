package openjframework.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import openjframework.model.*;
import com.aidi.core.web.framework.ServiceAdapter;
import com.aidi.core.spring.CustomerContextHolder;
public class ProfileInfoServiceUtil
{
    public static Serializable  create(ProfileInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
       ProfileInfoService service=(ProfileInfoService)ServiceAdapter.getServiceByName("ProfileInfoService");
       return service.create(bean);
    }
    public static void delete(ProfileInfo bean){
    	CustomerContextHolder.setCustomerType("cccf");
        ProfileInfoService service=(ProfileInfoService)ServiceAdapter.getServiceByName("ProfileInfoService");
       service.delete(bean);
       }
    public static void saveOrUpdate(ProfileInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        ProfileInfoService service=(ProfileInfoService)ServiceAdapter.getServiceByName("ProfileInfoService");
       service.saveOrUpdate(bean);
       }
    public static void update(ProfileInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        ProfileInfoService service=(ProfileInfoService)ServiceAdapter.getServiceByName("ProfileInfoService");
       service.update(bean);
       }
    public static List getAll(){
       CustomerContextHolder.setCustomerType("cccf");
        ProfileInfoService service=(ProfileInfoService)ServiceAdapter.getServiceByName("ProfileInfoService");
       return service.getAll();
       }
    public static ProfileInfo getById(String id){
    	CustomerContextHolder.setCustomerType("cccf");
        ProfileInfoService service=(ProfileInfoService)ServiceAdapter.getServiceByName("ProfileInfoService");
       return service.getById(id);
       }
     public static ProfileInfo getByPrimaryKey(String key){
    	CustomerContextHolder.setCustomerType("cccf");
        ProfileInfoService service=(ProfileInfoService)ServiceAdapter.getServiceByName("ProfileInfoService");
       return service.getByPrimaryKey(key);
       }
    public static List findByQuery(String querystr){
        CustomerContextHolder.setCustomerType("cccf");
        ProfileInfoService service=(ProfileInfoService)ServiceAdapter.getServiceByName("ProfileInfoService");
       return service.findByQuery(querystr);
       }
    
    public static List<ProfileInfo> getByOrganization(OrganizationInfo org)
    {
    	String qstr="from ProfileInfo  where organization.id='"+org.getId()+"'";
    	return findByQuery(qstr);
    	
    }
    public static List<ProfileInfo> getByNotAssign()
    {
    	String qstr="from ProfileInfo  where organization.id  is null";
    	List<ProfileInfo> list=findByQuery(qstr);
    	return list ;
    }
    
    public static List<ProfileInfo> findByRole(RoleInfo role){
    	String querystr = "from  ProfileInfo prof,RoleInfo role where  role in elements(prof.roleList) and  role.name='"+ role.getName()+"'";
    	List<ProfileInfo> profileList = findByQuery(querystr);
    	return null;
    }
}
   