package cccf.ma.service;

import java.io.Serializable;
import java.util.List;

import cccf.ma.model.ProductionModelInspection;

import com.aidi.core.spring.CustomerContextHolder;
import com.aidi.core.web.framework.ServiceAdapter;

public class PMIServiceUtil {
	public static Serializable  create(ProductionModelInspection bean){
	       CustomerContextHolder.setCustomerType("cccf");
	       PMIService service=(PMIService)ServiceAdapter.getServiceByName("ProductionModelInspectionService");
	       return service.create(bean);
	    }
	    public static void delete(ProductionModelInspection bean){
	    	CustomerContextHolder.setCustomerType("cccf");
	    	PMIService service=(PMIService)ServiceAdapter.getServiceByName("ProductionModelInspectionService");
	       service.delete(bean);
	       }
	    public static void saveOrUpdate(ProductionModelInspection bean){
	       CustomerContextHolder.setCustomerType("cccf");
	       PMIService service=(PMIService)ServiceAdapter.getServiceByName("ProductionModelInspectionService");
	       service.saveOrUpdate(bean);
	       }
	    public static void update(ProductionModelInspection bean){
	       CustomerContextHolder.setCustomerType("cccf");
	       PMIService service=(PMIService)ServiceAdapter.getServiceByName("ProductionModelInspectionService");
	       service.update(bean);
	       }
	    public static List getAll(){
	       CustomerContextHolder.setCustomerType("cccf");
	       PMIService service=(PMIService)ServiceAdapter.getServiceByName("ProductionModelInspectionService");
	       return service.getAll();
	       }
	    public static ProductionModelInspection getById(String id){
	    	CustomerContextHolder.setCustomerType("cccf");
	    	PMIService service=(PMIService)ServiceAdapter.getServiceByName("ProductionModelInspectionService");
	       return service.getById(id);
	       }
	     public static ProductionModelInspection getByPrimaryKey(String key){
	    	CustomerContextHolder.setCustomerType("cccf");
	    	PMIService service=(PMIService)ServiceAdapter.getServiceByName("ProductionModelInspectionService");
	       return service.getByPrimaryKey(key);
	       }
	    public static List findByQuery(String querystr){
	        CustomerContextHolder.setCustomerType("cccf");
	        PMIService service=(PMIService)ServiceAdapter.getServiceByName("ProductionModelInspectionService");
	       return service.findByQuery(querystr);
	       }
}
