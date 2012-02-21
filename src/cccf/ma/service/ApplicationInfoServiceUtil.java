package cccf.ma.service;

import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
import com.aidi.core.web.framework.ServiceAdapter;
import com.aidi.core.spring.CustomerContextHolder;

@SuppressWarnings("unchecked")
public class ApplicationInfoServiceUtil
{
	public static Serializable create(ApplicationInfo bean)
	{
		CustomerContextHolder.setCustomerType("cccf");
		ApplicationInfoService service = (ApplicationInfoService) ServiceAdapter.getServiceByName("ApplicationInfoService");
		return service.create(bean);
	}
	public static void delete(ApplicationInfo bean)
	{
		CustomerContextHolder.setCustomerType("cccf");
		ApplicationInfoService service = (ApplicationInfoService) ServiceAdapter.getServiceByName("ApplicationInfoService");
		service.delete(bean);
	}
	public static void saveOrUpdate(ApplicationInfo bean)
	{
		CustomerContextHolder.setCustomerType("cccf");
		ApplicationInfoService service = (ApplicationInfoService) ServiceAdapter.getServiceByName("ApplicationInfoService");
		service.saveOrUpdate(bean);
	}
	public static void update(ApplicationInfo bean)
	{
		CustomerContextHolder.setCustomerType("cccf");
		ApplicationInfoService service = (ApplicationInfoService) ServiceAdapter.getServiceByName("ApplicationInfoService");
		service.update(bean);
	}
	public static List getAll()
	{
		CustomerContextHolder.setCustomerType("cccf");
		ApplicationInfoService service = (ApplicationInfoService) ServiceAdapter.getServiceByName("ApplicationInfoService");
		return service.getAll();
	}
	/**
	 * 取工厂检查表
	 * 
	 * @param factory
	 *            nust|none|批次ID
	 * @return
	 */
	public static List getfactoryInspection(String factory)
	{
		return findByQuery("from ApplicationInfo where factoryInspection='" + factory + "'");
	}
	
	public static ApplicationInfo getById(String id)
	{
		CustomerContextHolder.setCustomerType("cccf");
		ApplicationInfoService service = (ApplicationInfoService) ServiceAdapter.getServiceByName("ApplicationInfoService");
		return service.getById(id);
	}
	public static ApplicationInfo getByPrimaryKey(String key)
	{
		CustomerContextHolder.setCustomerType("cccf");
		ApplicationInfoService service = (ApplicationInfoService) ServiceAdapter.getServiceByName("ApplicationInfoService");
		return service.getByPrimaryKey(key);
	}
	public static List findByQuery(String querystr)
	{
		CustomerContextHolder.setCustomerType("cccf");
		ApplicationInfoService service = (ApplicationInfoService) ServiceAdapter.getServiceByName("ApplicationInfoService");
		return service.findByQuery(querystr);
	}
	public static List getApplicationByEnterprise(String eid)
	{
		List list = findByQuery("from ApplicationInfo a where a.enterprise='" + eid + "' order by applyDate desc");
		return list;
	}
	public static List getApplicationListSubmit()
	{
		List list = findByQuery("from ApplicationInfo where status=1 order by applyDate desc");
		return list;
	}
	public static List getApplicationListSubmitByProductionId(String pid)
	{
		List list = findByQuery("from ApplicationInfo where status=1 and production.id='" + pid + "' order by applyDate desc");
		return list;
	}
	/**
	 * 取得pid本级及所有孩子节点的所有申请
	 * 
	 * @param pid
	 * @return
	 */
	public static List getApplicationListSubmitByCatalogue(String pid)
	{
		// 取得pid下级的所有叶子节点
		List plist = new ArrayList();
		List<ProductCatalogueInfo> childrenLeafList = ProductCatalogueInfoServiceUtil.getLeafByPid(pid, plist);
		String sb = "'" + pid + "',";
		for (int i = 0; i < childrenLeafList.size(); i++)
		{
			String s = childrenLeafList.get(i).getId();
			sb = sb + "'" + s + "',";
		}
		String qstr = sb.toString();
		if (qstr.substring(qstr.length() - 1).equals(","))
			qstr = qstr.substring(0, qstr.length() - 1);
		// 取得pid下的所有叶子节点
		List list = findByQuery("from ApplicationInfo where status=1 and production.id in (" + qstr + ") order by applyDate desc");
		return list;
	}
}
