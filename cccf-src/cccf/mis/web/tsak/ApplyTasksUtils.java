package cccf.mis.web.tsak;

import java.util.List;
import java.util.Map;
import net.modellite.spring.BeanAdapter;
import cccf.ma.service.ApplicationPublicService;

public class ApplyTasksUtils
{
	private static String	order_by	= " order by o.applicationPublic.applydate desc";
	/**
	 * 获取任务(不安用户)
	 * 
	 * @param task_name
	 *            任务名称
	 * @param page
	 *            业码
	 * @param pageSize
	 *            页大小
	 * @param args
	 *            查询参数
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> getTasks(String task_name, int page, int pageSize, Map<String, String> args)
	{
		StringBuffer hql = new StringBuffer("select DISTINCT new map(o.sioid as no")
				// 申请号
				.append(",o.sioid as applyno ").append(",o.applicationPublic.applyEnterprise.name as enterprise ")
				// 企业名称
				.append(",o.applicationPublic.applyEnterprise.state as state ")
				// 国家
				.append(",o.applicationPublic.applyEnterprise.location as region ")
				// 地区
				.append(",o.applicationPublic.businessType as business_type ")
				// 业务类型
				.append(",o.applicationPublic.productCatalog as product_catalog ")
				// 产品大类
				.append(",ap.productModel.surveyReport.surverOrgName as surver_orgname ")
				// 质检中心
				.append(",o.applicationPublic.applyType as apply_type ")
				// 申请类型
				.append(",o.applicationPublic.applydate as apply_date ")
				// 申请时间
				.append(",case when pl.end is null then '处理' else '查看' end as butlabel ").append(")").append(" from ApplicationInfo o , ApplicationInfoProductModel ap").append(" ,ProcessLog pl").append(" where o = ap.applicationInfo ")
				.append(" and pl.boId=o.id ").append(" and taskInstanceName='").append(task_name).append("' ").append(getWhere(args));
		if ("1".equals(args.get("status")))
		{
			hql.append(order_by);
		}
		ApplicationPublicService applicationPublicService = BeanAdapter.getBean("ApplicationPublicService", ApplicationPublicService.class);
		List<Map<String, Object>> list = applicationPublicService.queryListForPage(page * pageSize, pageSize, hql.toString());
		return list;
	}
	private static StringBuffer getWhere(Map<String, String> args)
	{
		StringBuffer hql = new StringBuffer();
		if (args.get("no") != null && !args.get("no").isEmpty())
		{
			hql.append(" and sioid='").append(args.get("no")).append("' ");
		}
		if (args.get("enterprise") != null && !args.get("enterprise").isEmpty())
		{
			hql.append(" and o.applicationPublic.applyEnterprise.name like '%").append(args.get("enterprise")).append("%' ");
		}
		if (args.get("state") != null && !args.get("state").isEmpty())
		{
			hql.append(" and o.applicationPublic.applyEnterprise.state='").append(args.get("state")).append("' ");
		}
		if (args.get("region") != null && !args.get("region").isEmpty())
		{
			hql.append(" and o.applicationPublic.applyEnterprise.location='").append(args.get("region")).append("' ");
		}
		if (args.get("business_type") != null && !args.get("business_type").isEmpty())
		{
			hql.append(" and o.applicationPublic.businessType='").append(args.get("business_type")).append("' ");
		}
		if (args.get("product_catalog") != null && !args.get("product_catalog").isEmpty())
		{
			hql.append(" and o.applicationPublic.productCatalog='").append(args.get("product_catalog")).append("' ");
		}
		if (args.get("surver_orgname") != null && !args.get("surver_orgname").isEmpty())
		{
			hql.append(" and ap.productModel.surveyReport.surverOrgName like '%").append(args.get("surver_orgname")).append("%' ");
		}
		if (args.get("apply_type") != null && !args.get("apply_type").isEmpty())
		{
			hql.append(" and o.applicationPublic.applyType='").append(args.get("apply_type")).append("' ");
		}
		if (args.get("start_applydate") != null && !args.get("start_applydate").isEmpty())
		{
			hql.append(" and o.applicationPublic.applydate >='").append(args.get("start_applydate")).append("' ");
		}
		if (args.get("end_applydate") != null && !args.get("end_applydate").isEmpty())
		{
			hql.append(" and o.applicationPublic.applydate <='").append(args.get("end_applydate")).append("' ");
		}
		if (args.get("end_applydate") != null && !args.get("end_applydate").isEmpty())
		{
			hql.append(" and o.applicationPublic.applydate <='").append(args.get("end_applydate")).append("' ");
		}
		if (args.get("status") != null && !args.get("status").isEmpty())
		{
			if ("0".equals(args.get("status")))
			{
				hql.append(" and pl.end is null ");
			} else
			{
				hql.append(" and pl.end is not null ");
				if (args.get("usid") != null && !args.get("usid").isEmpty())
				{
					hql.append(" and pl.actorId='").append(args.get("usid")).append("'");
				}
			}
		}
		return hql;
	}
	/**
	 * 获取任务大小(不安用户)
	 * 
	 * @param task_name
	 *            任务名称
	 * @param args
	 *            查询参数
	 * @return
	 */
	public static int getTasksSize(String task_name, Map<String, String> args)
	{
		StringBuffer hql = new StringBuffer("select count(DISTINCT o.sioid )").append(" from ApplicationInfo o , ApplicationInfoProductModel ap").append(" ,ProcessLog pl").append(" where o = ap.applicationInfo ").append(" and pl.boId=o.id ")
				.append(" and taskInstanceName='").append(task_name).append("' ").append(getWhere(args));
		ApplicationPublicService applicationPublicService = BeanAdapter.getBean("ApplicationPublicService", ApplicationPublicService.class);
		return applicationPublicService.queryForResultSize(hql.toString());
	}
}
