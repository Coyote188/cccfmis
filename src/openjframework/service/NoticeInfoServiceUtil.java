package openjframework.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import openjframework.model.*;
import cccf.ma.model.EnterpriseInfo;

import com.aidi.core.web.framework.ServiceAdapter;
import com.aidi.core.spring.CustomerContextHolder;
public class NoticeInfoServiceUtil
{
    public static Serializable  create(NoticeInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
       NoticeInfoService service=(NoticeInfoService)ServiceAdapter.getServiceByName("NoticeInfoService");
       return service.create(bean);
    }
    public static void delete(NoticeInfo bean){
    	CustomerContextHolder.setCustomerType("cccf");
        NoticeInfoService service=(NoticeInfoService)ServiceAdapter.getServiceByName("NoticeInfoService");
       service.delete(bean);
       }
    public static void saveOrUpdate(NoticeInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        NoticeInfoService service=(NoticeInfoService)ServiceAdapter.getServiceByName("NoticeInfoService");
       service.saveOrUpdate(bean);
       }
    public static void update(NoticeInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        NoticeInfoService service=(NoticeInfoService)ServiceAdapter.getServiceByName("NoticeInfoService");
       service.update(bean);
       }
    public static List getAll(){
       CustomerContextHolder.setCustomerType("cccf");
        NoticeInfoService service=(NoticeInfoService)ServiceAdapter.getServiceByName("NoticeInfoService");
       return service.getAll();
       }
    public static NoticeInfo getById(String id){
    	CustomerContextHolder.setCustomerType("cccf");
        NoticeInfoService service=(NoticeInfoService)ServiceAdapter.getServiceByName("NoticeInfoService");
       return service.getById(id);
       }
     public static NoticeInfo getByPrimaryKey(String key){
    	CustomerContextHolder.setCustomerType("cccf");
        NoticeInfoService service=(NoticeInfoService)ServiceAdapter.getServiceByName("NoticeInfoService");
       return service.getByPrimaryKey(key);
       }
    public static List findByQuery(String querystr){
        CustomerContextHolder.setCustomerType("cccf");
        NoticeInfoService service=(NoticeInfoService)ServiceAdapter.getServiceByName("NoticeInfoService");
       return service.findByQuery(querystr);
       }
    
    
}
   