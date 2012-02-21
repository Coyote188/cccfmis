package openjframework.service;

import java.math.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.sql.*;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.zkoss.zk.ui.Executions;

import openjframework.model.*;

import com.aidi.core.web.framework.ServiceAdapter;
import com.aidi.core.spring.CustomerContextHolder;

public class UserLogInfoServiceUtil {
	public static Serializable create(UserLogInfo bean) {
		CustomerContextHolder.setCustomerType("dltaxoa");
		UserLogInfoService service = (UserLogInfoService) ServiceAdapter
				.getServiceByName("UserLogInfoService");
		return service.create(bean);
	}

	public static void delete(UserLogInfo bean) {
		CustomerContextHolder.setCustomerType("dltaxoa");
		UserLogInfoService service = (UserLogInfoService) ServiceAdapter
				.getServiceByName("UserLogInfoService");
		service.delete(bean);
	}

	public static void saveOrUpdate(UserLogInfo bean) {
		CustomerContextHolder.setCustomerType("dltaxoa");
		UserLogInfoService service = (UserLogInfoService) ServiceAdapter
				.getServiceByName("UserLogInfoService");
		service.saveOrUpdate(bean);
	}

	public static void update(UserLogInfo bean) {
		CustomerContextHolder.setCustomerType("dltaxoa");
		UserLogInfoService service = (UserLogInfoService) ServiceAdapter
				.getServiceByName("UserLogInfoService");
		service.update(bean);
	}

	public static List getAll() {
		CustomerContextHolder.setCustomerType("dltaxoa");
		UserLogInfoService service = (UserLogInfoService) ServiceAdapter
				.getServiceByName("UserLogInfoService");
		return service.getAll();
	}

	public static UserLogInfo getById(String id) {
		CustomerContextHolder.setCustomerType("dltaxoa");
		UserLogInfoService service = (UserLogInfoService) ServiceAdapter
				.getServiceByName("UserLogInfoService");
		return service.getById(id);
	}

	public static UserLogInfo getByPrimaryKey(String key) {
		CustomerContextHolder.setCustomerType("dltaxoa");
		UserLogInfoService service = (UserLogInfoService) ServiceAdapter
				.getServiceByName("UserLogInfoService");
		return service.getByPrimaryKey(key);
	}

	public static List findByQuery(String querystr) {
		CustomerContextHolder.setCustomerType("dltaxoa");
		UserLogInfoService service = (UserLogInfoService) ServiceAdapter
				.getServiceByName("UserLogInfoService");
		return service.findByQuery(querystr);
	}

	public static void insertLoginLog(UserInfo user) {
		UserLogInfo log = new UserLogInfo();
		log.setLogType(1);
		log.setUser(user);
		log.setLogDate(new Date());
		create(log);
	}

	public static void insertLoginLog() {
		UserLogInfo log = new UserLogInfo();
		log.setLogType(1);
		UserInfo user = UserInfoServiceUtil.getCurrentLoginUser();
		log.setUser(user);
		log.setLogDate(new Date());
		HttpServletRequest request = (HttpServletRequest) Executions
				.getCurrent().getNativeRequest();
		String ip = request.getRemoteAddr();
		log.setIpAddr(ip);
		create(log);
	}

	public static UserLogInfo getUserLoginLogByDate(Date logDate, String uid) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String str1 = sdf.format(logDate) + " 00:00";
		String str2 = sdf.format(logDate) + " 24:00";
		List list = new ArrayList();
		list = findByQuery("from UserLogInfo where user.id='" + uid
				+ "' and logType=1 and (logDate>='" + str1 + "' and logDate<='"
				+ str2 + "')");
		UserLogInfo log = new UserLogInfo();
		if (list.size() > 0) {
			log = (UserLogInfo) list.get(0);
		}
		return log;
	}

	public static List getUserLoginLog(String uid) {

		List list = new ArrayList();
		list = findByQuery("from UserLogInfo where user.id='" + uid + "' and logType=1)");
		return list;
	}

	public static UserLogInfo getUserLastLoginLog(String uid) {

		List list = new ArrayList();
		list = findByQuery("from UserLogInfo where user.id='" + uid + "' and logType=1 order by logDate desc)");
		UserLogInfo log = null;
		if (list.size() > 1) {
			log = (UserLogInfo) list.get(1);
		}
		return log;
	}

	public static void UpdateLoginIP(String uid, String ip) {
		List list = new ArrayList();
		list = findByQuery("from UserLogInfo where user.id='" + uid
				+ "' and logType=1 order by logDate desc");
		UserLogInfo log = new UserLogInfo();
		if (list.size() > 0) {
			log = (UserLogInfo) list.get(0);
		}
		log.setIpAddr(ip);
		update(log);
	}
}
