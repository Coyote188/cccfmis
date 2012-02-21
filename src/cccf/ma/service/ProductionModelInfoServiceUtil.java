package cccf.ma.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
import com.aidi.core.web.framework.ServiceAdapter;
import com.aidi.core.spring.CustomerContextHolder;
public class ProductionModelInfoServiceUtil
{
    public static Serializable  create(ProductionModelInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
       ProductionModelInfoService service=(ProductionModelInfoService)ServiceAdapter.getServiceByName("ProductionModelInfoService");
       return service.create(bean);
    }
    public static void delete(ProductionModelInfo bean){
    	CustomerContextHolder.setCustomerType("cccf");
        ProductionModelInfoService service=(ProductionModelInfoService)ServiceAdapter.getServiceByName("ProductionModelInfoService");
       service.delete(bean);
       }
    public static void saveOrUpdate(ProductionModelInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        ProductionModelInfoService service=(ProductionModelInfoService)ServiceAdapter.getServiceByName("ProductionModelInfoService");
       service.saveOrUpdate(bean);
       }
    public static void update(ProductionModelInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        ProductionModelInfoService service=(ProductionModelInfoService)ServiceAdapter.getServiceByName("ProductionModelInfoService");
       service.update(bean);
       }
    public static List getAll(){
       CustomerContextHolder.setCustomerType("cccf");
        ProductionModelInfoService service=(ProductionModelInfoService)ServiceAdapter.getServiceByName("ProductionModelInfoService");
       return service.getAll();
       }
    public static ProductionModelInfo getById(String id){
    	CustomerContextHolder.setCustomerType("cccf");
        ProductionModelInfoService service=(ProductionModelInfoService)ServiceAdapter.getServiceByName("ProductionModelInfoService");
       return service.getById(id);
       }
     public static ProductionModelInfo getByPrimaryKey(String key){
    	CustomerContextHolder.setCustomerType("cccf");
        ProductionModelInfoService service=(ProductionModelInfoService)ServiceAdapter.getServiceByName("ProductionModelInfoService");
       return service.getByPrimaryKey(key);
       }
    public static List findByQuery(String querystr){
        CustomerContextHolder.setCustomerType("cccf");
        ProductionModelInfoService service=(ProductionModelInfoService)ServiceAdapter.getServiceByName("ProductionModelInfoService");
       return service.findByQuery(querystr);
       }
}
   