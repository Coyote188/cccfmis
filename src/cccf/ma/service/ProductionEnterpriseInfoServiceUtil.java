package cccf.ma.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;

import com.aidi.core.web.framework.ServiceAdapter;
import com.aidi.core.spring.CustomerContextHolder;
@SuppressWarnings("unchecked")
public class ProductionEnterpriseInfoServiceUtil
{
    public static Serializable  create(ProductionEnterpriseInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
       ProductionEnterpriseInfoService service=(ProductionEnterpriseInfoService)ServiceAdapter.getServiceByName("ProductionEnterpriseInfoService");
       return service.create(bean);
    }
    public static void delete(ProductionEnterpriseInfo bean){
    	CustomerContextHolder.setCustomerType("cccf");
        ProductionEnterpriseInfoService service=(ProductionEnterpriseInfoService)ServiceAdapter.getServiceByName("ProductionEnterpriseInfoService");
       service.delete(bean);
       }
    public static void saveOrUpdate(ProductionEnterpriseInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        ProductionEnterpriseInfoService service=(ProductionEnterpriseInfoService)ServiceAdapter.getServiceByName("ProductionEnterpriseInfoService");
       service.saveOrUpdate(bean);
       }
    public static void update(ProductionEnterpriseInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        ProductionEnterpriseInfoService service=(ProductionEnterpriseInfoService)ServiceAdapter.getServiceByName("ProductionEnterpriseInfoService");
       service.update(bean);
       }
    public static List getAll(){
       CustomerContextHolder.setCustomerType("cccf");
        ProductionEnterpriseInfoService service=(ProductionEnterpriseInfoService)ServiceAdapter.getServiceByName("ProductionEnterpriseInfoService");
       return service.getAll();
       }
    public static ProductionEnterpriseInfo getById(String id){
    	CustomerContextHolder.setCustomerType("cccf");
        ProductionEnterpriseInfoService service=(ProductionEnterpriseInfoService)ServiceAdapter.getServiceByName("ProductionEnterpriseInfoService");
       return service.getById(id);
       }
     public static ProductionEnterpriseInfo getByPrimaryKey(String key){
    	CustomerContextHolder.setCustomerType("cccf");
        ProductionEnterpriseInfoService service=(ProductionEnterpriseInfoService)ServiceAdapter.getServiceByName("ProductionEnterpriseInfoService");
       return service.getByPrimaryKey(key);
       }
    public static List findByQuery(String querystr){
        CustomerContextHolder.setCustomerType("cccf");
        ProductionEnterpriseInfoService service=(ProductionEnterpriseInfoService)ServiceAdapter.getServiceByName("ProductionEnterpriseInfoService");
       return service.findByQuery(querystr);
       }
    
    
    public static List<ProductionEnterpriseInfo> getProductionEnterprise(EnterpriseInfo enterprise){
    	String querystr = "from ProductionEnterpriseInfo p where p.enterprise = '"+ enterprise.getId() +"'";
    	List<ProductionEnterpriseInfo> result = findByQuery(querystr);
    	return result;
    }
    
    
	public static List getActivedProductionEnterpriseByUserId(String uid) {

		List list = findByQuery("from ProductionEnterpriseInfo where status=1 and enterprise.account.id='"
				+ uid + "'");
		return list;

	}
	
	public static void active(ProductionEnterpriseInfo pro){
		pro.active();
		saveOrUpdate(pro);
	}
	
	public static List<ProductionEnterpriseInfo> getInactive(){
		String querystr = "from ProductionEnterpriseInfo p where p.status = 0";
		List<ProductionEnterpriseInfo> list = findByQuery(querystr);
		return list;
	}
	
	public static List<ProductionEnterpriseInfo> getEnterpriseInactive(EnterpriseInfo e){
		String querystr = "from ProductionEnterpriseInfo p where p.status = 0 and p.enterprise='"+e.getId()+"'";
		List<ProductionEnterpriseInfo> list = findByQuery(querystr);
		return list;
	}
	
	public static boolean isProExist(String str){
		EnterpriseInfo e = EnterpriseInfoServiceUtil.getCurrentEnterprise();
		String querystr = "from ProductionEnterpriseInfo p where p.enterprise = '"+e.getId()+"' and p.name = '"+str+"'";
		List<ProductionEnterpriseInfo> list = findByQuery(querystr);
		if (list.isEmpty()) {
			return false;
		}else {
			return true;
		}
	}
}
   