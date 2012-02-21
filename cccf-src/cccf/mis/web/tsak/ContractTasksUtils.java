package cccf.mis.web.tsak;

import java.util.List;
import java.util.Map;
import net.modellite.spring.BeanAdapter;
import cccf.ma.service.ContractService;

public class ContractTasksUtils
{
	private static String	order_by	= " order by o.createDate desc";
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
		StringBuffer hql = new StringBuffer("select DISTINCT new map(")
		        .append("o.feeTotal as feeTotal")
		        .append(",o.contractNo as contractno")
				// 合同号
				.append(",o.applyno as applyno ").append(",o.applicationPublicInfo.applyEnterprise.name as enterprise ")
				// 企业名称
				.append(",o.applicationPublicInfo.applyEnterprise.state as state ")
				// 国家
				.append(",o.applicationPublicInfo.applyEnterprise.location as region ")
				// 地区
				.append(",o.applicationPublicInfo.businessType as business_type ")
				// 业务类型
				.append(",o.createDate as create_date ")
				// 创建日期
				.append(",o.status as status ")
				// 合同状态
				.append(",o.creater as creater ")
				// 合同状态
				.append(",case when pl.end is null then '处理' else '查看' end as butlabel ").append(")")
				.append(" from Contract o ,ApplicationInfo a ,ProcessLog pl")
				.append(" where o.applyno = a.sioid ")
				.append(" and pl.boId = a.id ")
				.append(" and pl.taskInstanceName='").append(task_name).append("' ").append(getWhere(args));
		if ("1".equals(args.get("status")))
		{
			hql.append(order_by);
		}
		ContractService contractService = BeanAdapter.getBean("ContractService", ContractService.class);
		List<Map<String, Object>> list = contractService.queryListForPage(page * pageSize, pageSize, hql.toString());
		return list;
	}
	private static StringBuffer getWhere(Map<String, String> args)
	{
		StringBuffer hql = new StringBuffer();
		if (args.get("contractno") != null && !args.get("contractno").isEmpty())
		{
			hql.append(" and o.contractNo='").append(args.get("contractno")).append("' ");
		}
		if (args.get("enterprise") != null && !args.get("enterprise").isEmpty())
		{
			hql.append(" and o.applicationPublicInfo.applyEnterprise.name like '%").append(args.get("enterprise")).append("%' ");
		}
		if (args.get("start_create_date") != null && !args.get("start_create_date").isEmpty())
		{
			hql.append(" and o.createDate >='").append(args.get("start_create_date")).append("' ");
		}
		if (args.get("end_create_date") != null && !args.get("end_create_date").isEmpty())
		{
			hql.append(" and o.createDate <='").append(args.get("end_create_date")).append("' ");
		}
		if (args.get("region") != null && !args.get("region").isEmpty())
		{
			hql.append(" o.applicationPublicInfo.applyEnterprise.location='").append(args.get("region")).append("' ");
		}
		if (args.get("business_type") != null && !args.get("business_type").isEmpty())
		{
			hql.append(" o.applicationPublicInfo.businessType='").append(args.get("business_type")).append("' ");
		}
		if (args.get("contract_status") != null && !args.get("contract_status").isEmpty())
		{}
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
		StringBuffer hql = new StringBuffer("select count(DISTINCT o.contractNo )").append(" from Contract o ,ApplicationInfo a ,ProcessLog pl").append(" where o.applyno = a.sioid ").append(" and pl.boId = a.id ").append(" and pl.taskInstanceName='")
				.append(task_name).append("' ").append(getWhere(args));
		ContractService contractService = BeanAdapter.getBean("ContractService", ContractService.class);
		return contractService.queryForResultSize(hql.toString());
	}
}
