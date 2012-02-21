package cccf.ma.service;

import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
import com.aidi.core.web.framework.ServiceAdapter;
import com.aidi.core.spring.CustomerContextHolder;

public class FeeDetailInfoServiceUtil {
	public static Serializable create(FeeDetailInfo bean) {
		CustomerContextHolder.setCustomerType("cccf");
		FeeDetailInfoService service = (FeeDetailInfoService) ServiceAdapter
				.getServiceByName("FeeDetailInfoService");
		return service.create(bean);
	}

	public static void delete(FeeDetailInfo bean) {
		CustomerContextHolder.setCustomerType("cccf");
		FeeDetailInfoService service = (FeeDetailInfoService) ServiceAdapter
				.getServiceByName("FeeDetailInfoService");
		service.delete(bean);
	}

	public static void saveOrUpdate(FeeDetailInfo bean) {
		CustomerContextHolder.setCustomerType("cccf");
		FeeDetailInfoService service = (FeeDetailInfoService) ServiceAdapter
				.getServiceByName("FeeDetailInfoService");
		service.saveOrUpdate(bean);
	}

	public static void update(FeeDetailInfo bean) {
		CustomerContextHolder.setCustomerType("cccf");
		FeeDetailInfoService service = (FeeDetailInfoService) ServiceAdapter
				.getServiceByName("FeeDetailInfoService");
		service.update(bean);
	}

	public static List getAll() {
		CustomerContextHolder.setCustomerType("cccf");
		FeeDetailInfoService service = (FeeDetailInfoService) ServiceAdapter
				.getServiceByName("FeeDetailInfoService");
		return service.getAll();
	}

	public static FeeDetailInfo getById(String id) {
		CustomerContextHolder.setCustomerType("cccf");
		FeeDetailInfoService service = (FeeDetailInfoService) ServiceAdapter
				.getServiceByName("FeeDetailInfoService");
		return service.getById(id);
	}

	public static FeeDetailInfo getByPrimaryKey(String key) {
		CustomerContextHolder.setCustomerType("cccf");
		FeeDetailInfoService service = (FeeDetailInfoService) ServiceAdapter
				.getServiceByName("FeeDetailInfoService");
		return service.getByPrimaryKey(key);
	}

	public static List findByQuery(String querystr) {
		CustomerContextHolder.setCustomerType("cccf");
		FeeDetailInfoService service = (FeeDetailInfoService) ServiceAdapter
				.getServiceByName("FeeDetailInfoService");
		return service.findByQuery(querystr);
	}

	public static void deleteFeeByApplication(String applicationId) {

		String sql = "from FeeDetailInfo where application.id='"
				+ applicationId + "'";
		List<FeeDetailInfo> list = findByQuery(sql);
		if (list != null) {
			for (FeeDetailInfo fee : list) {
				delete(fee);
			}
		}
	}

	public static List<FeeDetailInfo> getFeeListByApplication(
			String applicationId) {

		String sql = "from FeeDetailInfo where application.id='"
				+ applicationId + "'";
		List<FeeDetailInfo> list = findByQuery(sql);
		return list;
	}

	public static FeeDetailInfo getApplicationFeeByItemId(String itemId,
			String applicationId) {
		FeeDetailInfo fee = new FeeDetailInfo();
		String sql = "from FeeDetailInfo where application.id='"
				+ applicationId + "' and accountItem.accountItemId='" + itemId
				+ "'";
		List<FeeDetailInfo> list = findByQuery(sql);
		if (list != null) {
			if (list.size() > 0)
				fee = list.get(0);
		}
		
		return fee;
	}
}
