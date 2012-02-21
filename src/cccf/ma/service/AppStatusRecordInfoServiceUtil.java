package cccf.ma.service;

import java.math.*;
import openjframework.model.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
import com.aidi.core.web.framework.ServiceAdapter;
import com.aidi.core.spring.CustomerContextHolder;

public class AppStatusRecordInfoServiceUtil {
	public static Serializable create(AppStatusRecordInfo bean) {
		CustomerContextHolder.setCustomerType("cccf");
		AppStatusRecordInfoService service = (AppStatusRecordInfoService) ServiceAdapter
				.getServiceByName("AppStatusRecordInfoService");
		return service.create(bean);
	}

	public static void delete(AppStatusRecordInfo bean) {
		CustomerContextHolder.setCustomerType("cccf");
		AppStatusRecordInfoService service = (AppStatusRecordInfoService) ServiceAdapter
				.getServiceByName("AppStatusRecordInfoService");
		service.delete(bean);
	}

	public static void saveOrUpdate(AppStatusRecordInfo bean) {
		CustomerContextHolder.setCustomerType("cccf");
		AppStatusRecordInfoService service = (AppStatusRecordInfoService) ServiceAdapter
				.getServiceByName("AppStatusRecordInfoService");
		service.saveOrUpdate(bean);
	}

	public static void update(AppStatusRecordInfo bean) {
		CustomerContextHolder.setCustomerType("cccf");
		AppStatusRecordInfoService service = (AppStatusRecordInfoService) ServiceAdapter
				.getServiceByName("AppStatusRecordInfoService");
		service.update(bean);
	}

	public static List getAll() {
		CustomerContextHolder.setCustomerType("cccf");
		AppStatusRecordInfoService service = (AppStatusRecordInfoService) ServiceAdapter
				.getServiceByName("AppStatusRecordInfoService");
		return service.getAll();
	}

	public static AppStatusRecordInfo getById(String id) {
		CustomerContextHolder.setCustomerType("cccf");
		AppStatusRecordInfoService service = (AppStatusRecordInfoService) ServiceAdapter
				.getServiceByName("AppStatusRecordInfoService");
		return service.getById(id);
	}

	public static AppStatusRecordInfo getByPrimaryKey(String key) {
		CustomerContextHolder.setCustomerType("cccf");
		AppStatusRecordInfoService service = (AppStatusRecordInfoService) ServiceAdapter
				.getServiceByName("AppStatusRecordInfoService");
		return service.getByPrimaryKey(key);
	}

	public static List findByQuery(String querystr) {
		CustomerContextHolder.setCustomerType("cccf");
		AppStatusRecordInfoService service = (AppStatusRecordInfoService) ServiceAdapter
				.getServiceByName("AppStatusRecordInfoService");
		return service.findByQuery(querystr);
	}
	
	public static List getListByApplication(String appId){
		String sql="from AppStatusRecordInfo where application.id='"+appId+"'";
		List list=findByQuery(sql);
		return list;
	}
}
