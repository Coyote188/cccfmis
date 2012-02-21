package cccf.ma.service;

import java.io.Serializable;
import java.util.List;

import cccf.ma.model.EnterpriseNoticeInfo;
import cccf.ma.model.FactoryInspection;

import com.aidi.core.spring.CustomerContextHolder;
import com.aidi.core.web.framework.ServiceAdapter;

public class FactoryInspectionServiceUtil {
	 public static Serializable  create(FactoryInspection bean){
	       CustomerContextHolder.setCustomerType("cccf");
	       FactoryInspectionService service=(FactoryInspectionService)ServiceAdapter.getServiceByName("FactoryInspectionService");
	       return service.create(bean);
	    }
	    public static void delete(FactoryInspection bean){
	    	CustomerContextHolder.setCustomerType("cccf");
	        FactoryInspectionService service=(FactoryInspectionService)ServiceAdapter.getServiceByName("FactoryInspectionService");
	       service.delete(bean);
	       }
	    public static void saveOrUpdate(FactoryInspection bean){
	       CustomerContextHolder.setCustomerType("cccf");
	        FactoryInspectionService service=(FactoryInspectionService)ServiceAdapter.getServiceByName("FactoryInspectionService");
	       service.saveOrUpdate(bean);
	       }
	    public static void update(FactoryInspection bean){
	       CustomerContextHolder.setCustomerType("cccf");
	        FactoryInspectionService service=(FactoryInspectionService)ServiceAdapter.getServiceByName("FactoryInspectionService");
	       service.update(bean);
	       }
	    public static List getAll(){
	       CustomerContextHolder.setCustomerType("cccf");
	        FactoryInspectionService service=(FactoryInspectionService)ServiceAdapter.getServiceByName("FactoryInspectionService");
	       return service.getAll();
	       }
	    public static FactoryInspection getById(String id){
	    	CustomerContextHolder.setCustomerType("cccf");
	        FactoryInspectionService service=(FactoryInspectionService)ServiceAdapter.getServiceByName("FactoryInspectionService");
	       return service.getById(id);
	       }
	     public static FactoryInspection getByPrimaryKey(String key){
	    	CustomerContextHolder.setCustomerType("cccf");
	        FactoryInspectionService service=(FactoryInspectionService)ServiceAdapter.getServiceByName("FactoryInspectionService");
	       return service.getByPrimaryKey(key);
	       }
	    public static List findByQuery(String querystr){
	        CustomerContextHolder.setCustomerType("cccf");
	        FactoryInspectionService service=(FactoryInspectionService)ServiceAdapter.getServiceByName("FactoryInspectionService");
	       return service.findByQuery(querystr);
	       }
}
