package cccf.ma.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
import com.aidi.core.web.framework.ServiceAdapter;
import com.aidi.core.spring.CustomerContextHolder;
public class StateInfoServiceUtil
{
    public static Serializable  create(StateInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
       StateInfoService service=(StateInfoService)ServiceAdapter.getServiceByName("StateInfoService");
       return service.create(bean);
    }
    public static void delete(StateInfo bean){
    	CustomerContextHolder.setCustomerType("cccf");
        StateInfoService service=(StateInfoService)ServiceAdapter.getServiceByName("StateInfoService");
       service.delete(bean);
       }
    public static void saveOrUpdate(StateInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        StateInfoService service=(StateInfoService)ServiceAdapter.getServiceByName("StateInfoService");
       service.saveOrUpdate(bean);
       }
    public static void update(StateInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        StateInfoService service=(StateInfoService)ServiceAdapter.getServiceByName("StateInfoService");
       service.update(bean);
       }
    public static List getAll(){
       CustomerContextHolder.setCustomerType("cccf");
        StateInfoService service=(StateInfoService)ServiceAdapter.getServiceByName("StateInfoService");
       return service.getAll();
       }
    public static StateInfo getById(String id){
    	CustomerContextHolder.setCustomerType("cccf");
        StateInfoService service=(StateInfoService)ServiceAdapter.getServiceByName("StateInfoService");
       return service.getById(id);
       }
     public static StateInfo getByPrimaryKey(String key){
    	CustomerContextHolder.setCustomerType("cccf");
        StateInfoService service=(StateInfoService)ServiceAdapter.getServiceByName("StateInfoService");
       return service.getByPrimaryKey(key);
       }
    public static List findByQuery(String querystr){
        CustomerContextHolder.setCustomerType("cccf");
        StateInfoService service=(StateInfoService)ServiceAdapter.getServiceByName("StateInfoService");
       return service.findByQuery(querystr);
       }
}
   