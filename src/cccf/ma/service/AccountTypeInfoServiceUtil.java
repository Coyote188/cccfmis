package cccf.ma.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
import com.aidi.core.web.framework.ServiceAdapter;
import com.aidi.core.spring.CustomerContextHolder;
public class AccountTypeInfoServiceUtil
{
    public static Serializable  create(AccountTypeInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
       AccountTypeInfoService service=(AccountTypeInfoService)ServiceAdapter.getServiceByName("AccountTypeInfoService");
       return service.create(bean);
    }
    public static void delete(AccountTypeInfo bean){
    	CustomerContextHolder.setCustomerType("cccf");
        AccountTypeInfoService service=(AccountTypeInfoService)ServiceAdapter.getServiceByName("AccountTypeInfoService");
       service.delete(bean);
       }
    public static void saveOrUpdate(AccountTypeInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        AccountTypeInfoService service=(AccountTypeInfoService)ServiceAdapter.getServiceByName("AccountTypeInfoService");
       service.saveOrUpdate(bean);
       }
    public static void update(AccountTypeInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        AccountTypeInfoService service=(AccountTypeInfoService)ServiceAdapter.getServiceByName("AccountTypeInfoService");
       service.update(bean);
       }
    public static List getAll(){
       CustomerContextHolder.setCustomerType("cccf");
        AccountTypeInfoService service=(AccountTypeInfoService)ServiceAdapter.getServiceByName("AccountTypeInfoService");
       return service.getAll();
       }
    public static AccountTypeInfo getById(String id){
    	CustomerContextHolder.setCustomerType("cccf");
        AccountTypeInfoService service=(AccountTypeInfoService)ServiceAdapter.getServiceByName("AccountTypeInfoService");
       return service.getById(id);
       }
     public static AccountTypeInfo getByPrimaryKey(String key){
    	CustomerContextHolder.setCustomerType("cccf");
        AccountTypeInfoService service=(AccountTypeInfoService)ServiceAdapter.getServiceByName("AccountTypeInfoService");
       return service.getByPrimaryKey(key);
       }
    public static List findByQuery(String querystr){
        CustomerContextHolder.setCustomerType("cccf");
        AccountTypeInfoService service=(AccountTypeInfoService)ServiceAdapter.getServiceByName("AccountTypeInfoService");
       return service.findByQuery(querystr);
       }
}
   