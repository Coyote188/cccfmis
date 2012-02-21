package cccf.ma.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
import com.aidi.core.web.framework.ServiceAdapter;
import com.aidi.core.spring.CustomerContextHolder;
public class PumperDocumentsInfoServiceUtil
{
    public static Serializable  create(PumperDocumentsInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
       PumperDocumentsInfoService service=(PumperDocumentsInfoService)ServiceAdapter.getServiceByName("PumperDocumentsInfoService");
       return service.create(bean);
    }
    public static void delete(PumperDocumentsInfo bean){
    	CustomerContextHolder.setCustomerType("cccf");
        PumperDocumentsInfoService service=(PumperDocumentsInfoService)ServiceAdapter.getServiceByName("PumperDocumentsInfoService");
       service.delete(bean);
       }
    public static void saveOrUpdate(PumperDocumentsInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        PumperDocumentsInfoService service=(PumperDocumentsInfoService)ServiceAdapter.getServiceByName("PumperDocumentsInfoService");
       service.saveOrUpdate(bean);
       }
    public static void update(PumperDocumentsInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        PumperDocumentsInfoService service=(PumperDocumentsInfoService)ServiceAdapter.getServiceByName("PumperDocumentsInfoService");
       service.update(bean);
       }
    public static List getAll(){
       CustomerContextHolder.setCustomerType("cccf");
        PumperDocumentsInfoService service=(PumperDocumentsInfoService)ServiceAdapter.getServiceByName("PumperDocumentsInfoService");
       return service.getAll();
       }
    public static PumperDocumentsInfo getById(String id){
    	CustomerContextHolder.setCustomerType("cccf");
        PumperDocumentsInfoService service=(PumperDocumentsInfoService)ServiceAdapter.getServiceByName("PumperDocumentsInfoService");
       return service.getById(id);
       }
     public static PumperDocumentsInfo getByPrimaryKey(String key){
    	CustomerContextHolder.setCustomerType("cccf");
        PumperDocumentsInfoService service=(PumperDocumentsInfoService)ServiceAdapter.getServiceByName("PumperDocumentsInfoService");
       return service.getByPrimaryKey(key);
       }
    public static List findByQuery(String querystr){
        CustomerContextHolder.setCustomerType("cccf");
        PumperDocumentsInfoService service=(PumperDocumentsInfoService)ServiceAdapter.getServiceByName("PumperDocumentsInfoService");
       return service.findByQuery(querystr);
       }
}
   