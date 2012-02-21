package cccf.mis.web.tsak;

import java.util.List;
import java.util.Map;

import net.modellite.spring.BeanAdapter;
import cccf.ma.service.ContractService;

public class BatchTaskUtils {
	
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
		        .append("o.batchNo as batch_no ")            //批次号
				.append(",o.batchName as batch_name ")       // 批次名称 
				.append(",o.createDate as create_date ")    // 创建时间  
				.append(",o.createrId as created ")          // 创建人ID 
				.append(",o.productCount as productCount ")  // 产品数量 
				.append(",o.status as status ")              // 合同状态
				.append(",o.memo as memo ")                  // 备注 
				.append(")")
				.append(" from AssignBatch o ,ProcessLog pl ,FactoryCheckTask ct ,ApplicationInfo a")
				.append(" where ct.applyNo = a.sioid ")
				.append(" and ct.batchNo = o.batchNo ")
				.append(" and pl.boId = a.id ")
				.append(" and pl.taskInstanceName='")
				.append(task_name).append("' ")
				.append(getWhere(args));
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
		StringBuffer hql = new StringBuffer("select count(DISTINCT o.batchNo )")   
			.append(" from AssignBatch o ,ProcessLog pl ,FactoryCheckTask ct ,ApplicationInfo a")
			.append(" where ct.applyNo = a.sioid ")
			.append(" and ct.batchNo = o.batchNo ")
			.append(" and pl.boId = a.id ")
			.append(" and pl.taskInstanceName='")
			.append(task_name).append("' ")
			.append(getWhere(args)); 
		 
		ContractService contractService = BeanAdapter.getBean("ContractService", ContractService.class);
		return contractService.queryForResultSize(hql.toString());
	}
}
