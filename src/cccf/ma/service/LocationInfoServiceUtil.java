package cccf.ma.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
import com.aidi.core.web.framework.ServiceAdapter;
import com.aidi.core.spring.CustomerContextHolder;
public class LocationInfoServiceUtil
{
    public static Serializable  create(LocationInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
       LocationInfoService service=(LocationInfoService)ServiceAdapter.getServiceByName("LocationInfoService");
       return service.create(bean);
    }
    public static void delete(LocationInfo bean){
    	CustomerContextHolder.setCustomerType("cccf");
        LocationInfoService service=(LocationInfoService)ServiceAdapter.getServiceByName("LocationInfoService");
       service.delete(bean);
       }
    public static void saveOrUpdate(LocationInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        LocationInfoService service=(LocationInfoService)ServiceAdapter.getServiceByName("LocationInfoService");
       service.saveOrUpdate(bean);
       }
    public static void update(LocationInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        LocationInfoService service=(LocationInfoService)ServiceAdapter.getServiceByName("LocationInfoService");
       service.update(bean);
       }
    public static List getAll(){
       CustomerContextHolder.setCustomerType("cccf");
        LocationInfoService service=(LocationInfoService)ServiceAdapter.getServiceByName("LocationInfoService");
       return service.getAll();
       }
    public static LocationInfo getById(String id){
    	CustomerContextHolder.setCustomerType("cccf");
        LocationInfoService service=(LocationInfoService)ServiceAdapter.getServiceByName("LocationInfoService");
       return service.getById(id);
       }
     public static LocationInfo getByPrimaryKey(String key){
    	CustomerContextHolder.setCustomerType("cccf");
        LocationInfoService service=(LocationInfoService)ServiceAdapter.getServiceByName("LocationInfoService");
       return service.getByPrimaryKey(key);
       }
    public static List findByQuery(String querystr){
        CustomerContextHolder.setCustomerType("cccf");
        LocationInfoService service=(LocationInfoService)ServiceAdapter.getServiceByName("LocationInfoService");
       return service.findByQuery(querystr);
       }
    
    //get locations
    public static List getProvinceList(){
    	String querystr = "from LocationInfo l where l.parent is null";
    	List l = findByQuery(querystr);
    	return l == null ? null:l;
    }
    public static List getCityList(LocationInfo province){
    	String querystr = "from LocationInfo l where l.parent = '"+province.getId()+"'";
    	List l = findByQuery(querystr);
    	return l == null?null:l;
    }
}
   