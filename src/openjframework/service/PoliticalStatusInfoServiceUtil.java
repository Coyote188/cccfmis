package openjframework.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import openjframework.model.*;
import com.aidi.core.web.framework.ServiceAdapter;
import com.aidi.core.spring.CustomerContextHolder;
public class PoliticalStatusInfoServiceUtil
{
    public static Serializable  create(PoliticalStatusInfo bean){
       CustomerContextHolder.setCustomerType("dltaxoa");
       PoliticalStatusInfoService service=(PoliticalStatusInfoService)ServiceAdapter.getServiceByName("PoliticalStatusInfoService");
       return service.create(bean);
    }
    public static void delete(PoliticalStatusInfo bean){
    	CustomerContextHolder.setCustomerType("dltaxoa");
        PoliticalStatusInfoService service=(PoliticalStatusInfoService)ServiceAdapter.getServiceByName("PoliticalStatusInfoService");
       service.delete(bean);
       }
    public static void saveOrUpdate(PoliticalStatusInfo bean){
       CustomerContextHolder.setCustomerType("dltaxoa");
        PoliticalStatusInfoService service=(PoliticalStatusInfoService)ServiceAdapter.getServiceByName("PoliticalStatusInfoService");
       service.saveOrUpdate(bean);
       }
    public static void update(PoliticalStatusInfo bean){
       CustomerContextHolder.setCustomerType("dltaxoa");
        PoliticalStatusInfoService service=(PoliticalStatusInfoService)ServiceAdapter.getServiceByName("PoliticalStatusInfoService");
       service.update(bean);
       }
    public static List getAll(){
       CustomerContextHolder.setCustomerType("dltaxoa");
        PoliticalStatusInfoService service=(PoliticalStatusInfoService)ServiceAdapter.getServiceByName("PoliticalStatusInfoService");
       return service.getAll();
       }
    public static PoliticalStatusInfo getById(String id){
    	CustomerContextHolder.setCustomerType("dltaxoa");
        PoliticalStatusInfoService service=(PoliticalStatusInfoService)ServiceAdapter.getServiceByName("PoliticalStatusInfoService");
       return service.getById(id);
       }
     public static PoliticalStatusInfo getByPrimaryKey(String key){
    	CustomerContextHolder.setCustomerType("dltaxoa");
        PoliticalStatusInfoService service=(PoliticalStatusInfoService)ServiceAdapter.getServiceByName("PoliticalStatusInfoService");
       return service.getByPrimaryKey(key);
       }
    public static List findByQuery(String querystr){
        CustomerContextHolder.setCustomerType("dltaxoa");
        PoliticalStatusInfoService service=(PoliticalStatusInfoService)ServiceAdapter.getServiceByName("PoliticalStatusInfoService");
       return service.findByQuery(querystr);
       }
}
   