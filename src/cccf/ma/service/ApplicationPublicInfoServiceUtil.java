package cccf.ma.service;
import java.math.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
import com.aidi.core.web.framework.ServiceAdapter;
import com.aidi.core.service.BaseDAOServcieUtil;
import com.aidi.core.spring.CustomerContextHolder;
public class ApplicationPublicInfoServiceUtil
{
    public static Serializable  create(ApplicationPublicInfo bean){
       CustomerContextHolder.setCustomerType("dltaxoa");
       ApplicationPublicInfoService service=(ApplicationPublicInfoService)ServiceAdapter.getServiceByName("ApplicationPublicInfoService");
       return service.create(bean);
    }
    public static void delete(ApplicationPublicInfo bean){
    	CustomerContextHolder.setCustomerType("dltaxoa");
        ApplicationPublicInfoService service=(ApplicationPublicInfoService)ServiceAdapter.getServiceByName("ApplicationPublicInfoService");
       service.delete(bean);
       }
    public static void saveOrUpdate(ApplicationPublicInfo bean){
       CustomerContextHolder.setCustomerType("dltaxoa");
        ApplicationPublicInfoService service=(ApplicationPublicInfoService)ServiceAdapter.getServiceByName("ApplicationPublicInfoService");
       service.saveOrUpdate(bean);
       }
    public static void update(ApplicationPublicInfo bean){
       CustomerContextHolder.setCustomerType("dltaxoa");
        ApplicationPublicInfoService service=(ApplicationPublicInfoService)ServiceAdapter.getServiceByName("ApplicationPublicInfoService");
       service.update(bean);
       }
    public static List getAll(){
       CustomerContextHolder.setCustomerType("dltaxoa");
        ApplicationPublicInfoService service=(ApplicationPublicInfoService)ServiceAdapter.getServiceByName("ApplicationPublicInfoService");
       return service.getAll();
       }
    public static ApplicationPublicInfo getById(String id){
    	CustomerContextHolder.setCustomerType("dltaxoa");
        ApplicationPublicInfoService service=(ApplicationPublicInfoService)ServiceAdapter.getServiceByName("ApplicationPublicInfoService");
       return service.getById(id);
       }
     public static ApplicationPublicInfo getByPrimaryKey(String key){
    	CustomerContextHolder.setCustomerType("dltaxoa");
        ApplicationPublicInfoService service=(ApplicationPublicInfoService)ServiceAdapter.getServiceByName("ApplicationPublicInfoService");
       return service.getByPrimaryKey(key);
       }
    public static List findByQuery(String querystr){
        CustomerContextHolder.setCustomerType("dltaxoa");
        ApplicationPublicInfoService service=(ApplicationPublicInfoService)ServiceAdapter.getServiceByName("ApplicationPublicInfoService");
       return service.findByQuery(querystr);
       } 
   
}
   