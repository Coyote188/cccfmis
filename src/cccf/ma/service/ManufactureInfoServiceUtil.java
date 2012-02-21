package cccf.ma.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;

import org.zkoss.zhtml.Li;

import cccf.ma.model.*;
import com.aidi.core.web.framework.ServiceAdapter;
import com.aidi.core.spring.CustomerContextHolder;
public class ManufactureInfoServiceUtil
{
    public static Serializable  create(ManufactureInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
       ManufactureInfoService service=(ManufactureInfoService)ServiceAdapter.getServiceByName("ManufactureInfoService");
       return service.create(bean);
    }
    public static void delete(ManufactureInfo bean){
    	CustomerContextHolder.setCustomerType("cccf");
        ManufactureInfoService service=(ManufactureInfoService)ServiceAdapter.getServiceByName("ManufactureInfoService");
       service.delete(bean);
       }
    public static void saveOrUpdate(ManufactureInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        ManufactureInfoService service=(ManufactureInfoService)ServiceAdapter.getServiceByName("ManufactureInfoService");
       service.saveOrUpdate(bean);
       }
    public static void update(ManufactureInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        ManufactureInfoService service=(ManufactureInfoService)ServiceAdapter.getServiceByName("ManufactureInfoService");
       service.update(bean);
       }
    public static List getAll(){
       CustomerContextHolder.setCustomerType("cccf");
        ManufactureInfoService service=(ManufactureInfoService)ServiceAdapter.getServiceByName("ManufactureInfoService");
       return service.getAll();
       }
    public static ManufactureInfo getById(String id){
    	CustomerContextHolder.setCustomerType("cccf");
        ManufactureInfoService service=(ManufactureInfoService)ServiceAdapter.getServiceByName("ManufactureInfoService");
       return service.getById(id);
       }
     public static ManufactureInfo getByPrimaryKey(String key){
    	CustomerContextHolder.setCustomerType("cccf");
        ManufactureInfoService service=(ManufactureInfoService)ServiceAdapter.getServiceByName("ManufactureInfoService");
       return service.getByPrimaryKey(key);
       }
    public static List findByQuery(String querystr){
        CustomerContextHolder.setCustomerType("cccf");
        ManufactureInfoService service=(ManufactureInfoService)ServiceAdapter.getServiceByName("ManufactureInfoService");
       return service.findByQuery(querystr);
       }
    
    
    public static List<ManufactureInfo> getManufactures(EnterpriseInfo enterprise){
    	String querystr = "from ManufactureInfo m where m.enterprise = '"+enterprise.getId()+"'";
    	List<ManufactureInfo> result = findByQuery(querystr);
    	return result;
    }
    
    public static List getActivedManufactureByUserId(String uid) {

		List list = findByQuery("from ManufactureInfo where status=1 and enterprise.account.id='"
				+ uid + "'");

		return list;

	}
    
    public static void active(ManufactureInfo manu){
    	manu.active();
    	saveOrUpdate(manu);
    }
    
    public static List<ManufactureInfo> getInactive(){
    	String querystr = "from ManufactureInfo m where m.status=0";
    	List<ManufactureInfo> list = findByQuery(querystr);
    	return list;
    }
    
    public static boolean isManufactureExist(String str){
    	EnterpriseInfo e = EnterpriseInfoServiceUtil.getCurrentEnterprise();
    	String querystr = "from ManufactureInfo m where m.enterprise = '"+e.getId()+"' and m.name = '"+str + "'";
    	List<ManufactureInfo> list = findByQuery(querystr);
    	if (list.isEmpty()) {
			return false;
		}else {
			return true;
		}
    }
}
   