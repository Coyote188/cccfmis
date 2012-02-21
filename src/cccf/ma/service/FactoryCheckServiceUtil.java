package cccf.ma.service;

import java.util.List;

import cccf.ma.model.FactoryChecklist;

import com.aidi.core.spring.CustomerContextHolder;
import com.aidi.core.web.framework.ServiceAdapter;

@SuppressWarnings("unchecked")
public class FactoryCheckServiceUtil {

	public static List findByQuery(String querystr) {
		CustomerContextHolder.setCustomerType("cccf");
		FactoryCheckService service = (FactoryCheckService) ServiceAdapter
				.getServiceByName("FactoryCheckService");
		return service.findByQuery(querystr);
	}
	

	public static List<FactoryChecklist> getFirstLevel(){
		String queryStr = "FROM FactoryChecklist f WHERE f.parent is null";
		return findByQuery(queryStr);
	}
}
