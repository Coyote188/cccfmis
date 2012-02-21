package openjframework.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import openjframework.model.*;
import com.aidi.core.web.framework.ServiceAdapter;
import com.aidi.core.spring.CustomerContextHolder;
public class ResourceInfoServiceUtil
{
    public static Serializable  create(ResourceInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
       ResourceInfoService service=(ResourceInfoService)ServiceAdapter.getServiceByName("ResourceInfoService");
       return service.create(bean);
    }
    public static void delete(ResourceInfo bean){
    	CustomerContextHolder.setCustomerType("cccf");
        ResourceInfoService service=(ResourceInfoService)ServiceAdapter.getServiceByName("ResourceInfoService");
       service.delete(bean);
       }
    public static void saveOrUpdate(ResourceInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        ResourceInfoService service=(ResourceInfoService)ServiceAdapter.getServiceByName("ResourceInfoService");
       service.saveOrUpdate(bean);
       }
    public static void update(ResourceInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        ResourceInfoService service=(ResourceInfoService)ServiceAdapter.getServiceByName("ResourceInfoService");
       service.update(bean);
       }
    public static List getAll(){
       CustomerContextHolder.setCustomerType("cccf");
        ResourceInfoService service=(ResourceInfoService)ServiceAdapter.getServiceByName("ResourceInfoService");
       return service.getAll();
       }
    public static ResourceInfo getById(String id){
    	CustomerContextHolder.setCustomerType("cccf");
        ResourceInfoService service=(ResourceInfoService)ServiceAdapter.getServiceByName("ResourceInfoService");
       return service.getById(id);
       }
     public static ResourceInfo getByPrimaryKey(String key){
    	CustomerContextHolder.setCustomerType("cccf");
        ResourceInfoService service=(ResourceInfoService)ServiceAdapter.getServiceByName("ResourceInfoService");
       return service.getByPrimaryKey(key);
       }
    public static List findByQuery(String querystr){
        CustomerContextHolder.setCustomerType("cccf");
        ResourceInfoService service=(ResourceInfoService)ServiceAdapter.getServiceByName("ResourceInfoService");
       return service.findByQuery(querystr);
       }
}
   