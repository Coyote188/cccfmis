package openjframework.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import openjframework.model.*;
import com.aidi.core.web.framework.ServiceAdapter;
import com.aidi.core.spring.CustomerContextHolder;
public class ResourceTypeInfoServiceUtil
{
    public static Serializable  create(ResourceTypeInfo bean){
       CustomerContextHolder.setCustomerType("dltaxoa");
       ResourceTypeInfoService service=(ResourceTypeInfoService)ServiceAdapter.getServiceByName("ResourceTypeInfoService");
       return service.create(bean);
    }
    public static void delete(ResourceTypeInfo bean){
    	CustomerContextHolder.setCustomerType("dltaxoa");
        ResourceTypeInfoService service=(ResourceTypeInfoService)ServiceAdapter.getServiceByName("ResourceTypeInfoService");
       service.delete(bean);
       }
    public static void saveOrUpdate(ResourceTypeInfo bean){
       CustomerContextHolder.setCustomerType("dltaxoa");
        ResourceTypeInfoService service=(ResourceTypeInfoService)ServiceAdapter.getServiceByName("ResourceTypeInfoService");
       service.saveOrUpdate(bean);
       }
    public static void update(ResourceTypeInfo bean){
       CustomerContextHolder.setCustomerType("dltaxoa");
        ResourceTypeInfoService service=(ResourceTypeInfoService)ServiceAdapter.getServiceByName("ResourceTypeInfoService");
       service.update(bean);
       }
    public static List getAll(){
       CustomerContextHolder.setCustomerType("dltaxoa");
        ResourceTypeInfoService service=(ResourceTypeInfoService)ServiceAdapter.getServiceByName("ResourceTypeInfoService");
       return service.getAll();
       }
    public static ResourceTypeInfo getById(String id){
    	CustomerContextHolder.setCustomerType("dltaxoa");
        ResourceTypeInfoService service=(ResourceTypeInfoService)ServiceAdapter.getServiceByName("ResourceTypeInfoService");
       return service.getById(id);
       }
     public static ResourceTypeInfo getByPrimaryKey(String key){
    	CustomerContextHolder.setCustomerType("dltaxoa");
        ResourceTypeInfoService service=(ResourceTypeInfoService)ServiceAdapter.getServiceByName("ResourceTypeInfoService");
       return service.getByPrimaryKey(key);
       }
    public static List findByQuery(String querystr){
        CustomerContextHolder.setCustomerType("dltaxoa");
        ResourceTypeInfoService service=(ResourceTypeInfoService)ServiceAdapter.getServiceByName("ResourceTypeInfoService");
       return service.findByQuery(querystr);
       }
}
   