package cccf.ma.service;

import java.io.Serializable;
import java.util.List;

import cccf.ma.model.EnterpriseInfo;
import cccf.ma.model.EnterpriseProductModel;
import cccf.ma.model.ProductCatalogueInfo;

import com.aidi.core.spring.CustomerContextHolder;
import com.aidi.core.web.framework.ServiceAdapter;


public class EnterpriseProductModelServiceUtil {
    public static Serializable  create(EnterpriseProductModel bean){
       CustomerContextHolder.setCustomerType("cccf");
       EnterpriseProductModelService service=(EnterpriseProductModelService)ServiceAdapter.getServiceByName("EnterpriseProductModelService");
       return service.create(bean);
    }
    public static void delete(EnterpriseProductModel bean){
    	CustomerContextHolder.setCustomerType("cccf");
        EnterpriseProductModelService service=(EnterpriseProductModelService)ServiceAdapter.getServiceByName("EnterpriseProductModelService");
       service.delete(bean);
       }
    public static void saveOrUpdate(EnterpriseProductModel bean){
       CustomerContextHolder.setCustomerType("cccf");
        EnterpriseProductModelService service=(EnterpriseProductModelService)ServiceAdapter.getServiceByName("EnterpriseProductModelService");
       service.saveOrUpdate(bean);
       }
    public static void update(EnterpriseProductModel bean){
       CustomerContextHolder.setCustomerType("cccf");
        EnterpriseProductModelService service=(EnterpriseProductModelService)ServiceAdapter.getServiceByName("EnterpriseProductModelService");
       service.update(bean);
       }
    public static List getAll(){
       CustomerContextHolder.setCustomerType("cccf");
        EnterpriseProductModelService service=(EnterpriseProductModelService)ServiceAdapter.getServiceByName("EnterpriseProductModelService");
       return service.getAll();
       }
    public static EnterpriseProductModel getById(String id){
    	CustomerContextHolder.setCustomerType("cccf");
        EnterpriseProductModelService service=(EnterpriseProductModelService)ServiceAdapter.getServiceByName("EnterpriseProductModelService");
       return service.getById(id);
       }
     public static EnterpriseProductModel getByPrimaryKey(String key){
    	CustomerContextHolder.setCustomerType("cccf");
        EnterpriseProductModelService service=(EnterpriseProductModelService)ServiceAdapter.getServiceByName("EnterpriseProductModelService");
       return service.getByPrimaryKey(key);
       }
    public static List findByQuery(String querystr){
        CustomerContextHolder.setCustomerType("cccf");
        EnterpriseProductModelService service=(EnterpriseProductModelService)ServiceAdapter.getServiceByName("EnterpriseProductModelService");
       return service.findByQuery(querystr);
       }
    
    public static List<EnterpriseProductModel> findByEnterprise(EnterpriseInfo enter){
    	String str = "from EnterpriseProductModel e where e.enterprise = '" + enter.getId() + "'";
    	return findByQuery(str);
    }
    public static List<EnterpriseProductModel> findByProduct(ProductCatalogueInfo product){
    	String str = "from EnterpriseProductModel e where e.product = '" + product.getId() + "'";
    	return findByQuery(str);
    }
    public static List<EnterpriseProductModel> findByProductEnterprise(ProductCatalogueInfo product,EnterpriseInfo enter){
    	String str = "from EnterpriseProductModel e where e.product = '" + product.getId() + "' and e.enterprise = '"+enter.getId()+"'";
    	return findByQuery(str);
    }
    public static EnterpriseProductModel findByModel(String model){
    	String querystr = "from EnterpriseProductModel e where e.model ='" + model + "'";
    	return (EnterpriseProductModel) findByQuery(querystr).get(0);
    }
        
    public static boolean isModelExist(String model, EnterpriseInfo enter) {
		boolean isExist = false;
		for (EnterpriseProductModel epm : findByEnterprise(enter)) {
			if (null != epm.getModel()) {
				if (epm.getModel().equals(model)) {
					isExist = true;
					break;
				}
			} else
				break;
		}

		return isExist;
	}
}
