package cccf.ma.service;

import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
import com.aidi.core.web.framework.ServiceAdapter;
import com.aidi.core.spring.CustomerContextHolder;

public class AccountItemInfoServiceUtil {
	public static Serializable create(AccountItemInfo bean) {
		CustomerContextHolder.setCustomerType("cccf");
		AccountItemInfoService service = (AccountItemInfoService) ServiceAdapter
				.getServiceByName("AccountItemInfoService");
		return service.create(bean);
	}

	public static void delete(AccountItemInfo bean) {
		CustomerContextHolder.setCustomerType("cccf");
		AccountItemInfoService service = (AccountItemInfoService) ServiceAdapter
				.getServiceByName("AccountItemInfoService");
		service.delete(bean);
	}

	public static void saveOrUpdate(AccountItemInfo bean) {
		CustomerContextHolder.setCustomerType("cccf");
		AccountItemInfoService service = (AccountItemInfoService) ServiceAdapter
				.getServiceByName("AccountItemInfoService");
		service.saveOrUpdate(bean);
	}

	public static void update(AccountItemInfo bean) {
		CustomerContextHolder.setCustomerType("cccf");
		AccountItemInfoService service = (AccountItemInfoService) ServiceAdapter
				.getServiceByName("AccountItemInfoService");
		service.update(bean);
	}

	public static List getAll() {
		CustomerContextHolder.setCustomerType("cccf");
		AccountItemInfoService service = (AccountItemInfoService) ServiceAdapter
				.getServiceByName("AccountItemInfoService");
		return service.getAll();
	}

	public static AccountItemInfo getById(String id) {
		CustomerContextHolder.setCustomerType("cccf");
		AccountItemInfoService service = (AccountItemInfoService) ServiceAdapter
				.getServiceByName("AccountItemInfoService");
		return service.getById(id);
	}

	public static AccountItemInfo getByPrimaryKey(String key) {
		CustomerContextHolder.setCustomerType("cccf");
		AccountItemInfoService service = (AccountItemInfoService) ServiceAdapter
				.getServiceByName("AccountItemInfoService");
		return service.getByPrimaryKey(key);
	}

	public static List findByQuery(String querystr) {
		CustomerContextHolder.setCustomerType("cccf");
		AccountItemInfoService service = (AccountItemInfoService) ServiceAdapter
				.getServiceByName("AccountItemInfoService");
		return service.findByQuery(querystr);
	}
	
	public static List getItemListByType(String typeId){
		List itemList=findByQuery("from AccountItemInfo where accountType.atypeId='"+typeId+"'");
		return itemList;
	}
}
