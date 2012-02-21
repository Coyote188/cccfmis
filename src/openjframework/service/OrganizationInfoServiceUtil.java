package openjframework.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import openjframework.model.*;
import com.aidi.core.web.framework.ServiceAdapter;
import com.aidi.core.spring.CustomerContextHolder;
public class OrganizationInfoServiceUtil
{
    public static Serializable  create(OrganizationInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
       OrganizationInfoService service=(OrganizationInfoService)ServiceAdapter.getServiceByName("OrganizationInfoService");
       return service.create(bean);
    }
    public static void delete(OrganizationInfo bean){
    	CustomerContextHolder.setCustomerType("cccf");
        OrganizationInfoService service=(OrganizationInfoService)ServiceAdapter.getServiceByName("OrganizationInfoService");
       service.delete(bean);
       }
    public static void saveOrUpdate(OrganizationInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        OrganizationInfoService service=(OrganizationInfoService)ServiceAdapter.getServiceByName("OrganizationInfoService");
       service.saveOrUpdate(bean);
       }
    public static void update(OrganizationInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        OrganizationInfoService service=(OrganizationInfoService)ServiceAdapter.getServiceByName("OrganizationInfoService");
       service.update(bean);
       }
    public static List getAll(){
       CustomerContextHolder.setCustomerType("cccf");
        OrganizationInfoService service=(OrganizationInfoService)ServiceAdapter.getServiceByName("OrganizationInfoService");
       return service.getAll();
       }
    public static OrganizationInfo getById(String id){
    	CustomerContextHolder.setCustomerType("cccf");
        OrganizationInfoService service=(OrganizationInfoService)ServiceAdapter.getServiceByName("OrganizationInfoService");
       return service.getById(id);
       }
     public static OrganizationInfo getByPrimaryKey(String key){
    	CustomerContextHolder.setCustomerType("cccf");
        OrganizationInfoService service=(OrganizationInfoService)ServiceAdapter.getServiceByName("OrganizationInfoService");
       return service.getByPrimaryKey(key);
       }
    public static List findByQuery(String querystr){
        CustomerContextHolder.setCustomerType("cccf");
        OrganizationInfoService service=(OrganizationInfoService)ServiceAdapter.getServiceByName("OrganizationInfoService");
       return service.findByQuery(querystr);
       }
    public static List getUserList(String orgId) {
		List<UserInfo> userList=new ArrayList();
		userList=findByQuery("select prof.user from  ProfileInfo prof where prof.organization.id='"+orgId+"'");
		return userList;
	}
    /**
     * 按级别检索部门
     * @param level
     * @return
     */
    public static List getByOrganizationLevel(int level)
    {
    	String qstr="from OrganizationInfo where organizationLevel="+level+"order by organizationNo asc";
    	List<OrganizationInfo>result=findByQuery(qstr);
    	return result;
    }
}
   