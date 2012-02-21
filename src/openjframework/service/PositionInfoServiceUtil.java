package openjframework.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import openjframework.model.*;
import com.aidi.core.web.framework.ServiceAdapter;
import com.aidi.core.spring.CustomerContextHolder;
public class PositionInfoServiceUtil
{
    public static Serializable  create(PositionInfo bean){
       CustomerContextHolder.setCustomerType("dltaxoa");
       PositionInfoService service=(PositionInfoService)ServiceAdapter.getServiceByName("PositionInfoService");
       return service.create(bean);
    }
    public static void delete(PositionInfo bean){
    	CustomerContextHolder.setCustomerType("dltaxoa");
        PositionInfoService service=(PositionInfoService)ServiceAdapter.getServiceByName("PositionInfoService");
       service.delete(bean);
       }
    public static void saveOrUpdate(PositionInfo bean){
       CustomerContextHolder.setCustomerType("dltaxoa");
        PositionInfoService service=(PositionInfoService)ServiceAdapter.getServiceByName("PositionInfoService");
       service.saveOrUpdate(bean);
       }
    public static void update(PositionInfo bean){
       CustomerContextHolder.setCustomerType("dltaxoa");
        PositionInfoService service=(PositionInfoService)ServiceAdapter.getServiceByName("PositionInfoService");
       service.update(bean);
       }
    public static List getAll(){
       CustomerContextHolder.setCustomerType("dltaxoa");
        PositionInfoService service=(PositionInfoService)ServiceAdapter.getServiceByName("PositionInfoService");
       return service.getAll();
       }
    public static PositionInfo getById(String id){
    	CustomerContextHolder.setCustomerType("dltaxoa");
        PositionInfoService service=(PositionInfoService)ServiceAdapter.getServiceByName("PositionInfoService");
       return service.getById(id);
       }
     public static PositionInfo getByPrimaryKey(String key){
    	CustomerContextHolder.setCustomerType("dltaxoa");
        PositionInfoService service=(PositionInfoService)ServiceAdapter.getServiceByName("PositionInfoService");
       return service.getByPrimaryKey(key);
       }
    public static List findByQuery(String querystr){
        CustomerContextHolder.setCustomerType("dltaxoa");
        PositionInfoService service=(PositionInfoService)ServiceAdapter.getServiceByName("PositionInfoService");
       return service.findByQuery(querystr);
       }
}
   