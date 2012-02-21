package cccf.mis.web.tsak;

import java.util.List;
import java.util.Map;

import net.modellite.spring.BeanAdapter;
import cccf.ma.service.ContractService;

public class BatchProductUtils {
	
	private static String	order_by	= " order by o.assignDate desc";
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
		StringBuffer hql = new StringBuffer("select DISTINCT new map( o.taskNo as taskNo")  
		        .append(",o.taskNo as no ")   
		        .append(",a.applicationInfo.applicationPublic.applyno as applyno ")    //任务号
				.append(",a.applicationInfo.applicationPublic.applyEnterprise.name as enterprise ")   //  申请企业名称 
				.append(",a.applicationInfo.applicationPublic.applyEnterprise.registeredAddress as enterprise_addres ")   // 申请企业地址
	            .append(",a.productModel.manufactureInfo.name as manufactureInfo_name")   //生产厂
	            .append(",a.productModel.manufactureInfo.registeredAddress as manufactureInfo_address")   //生产厂地址
	            .append(",a.productModel.productionEnterpriseInfo.name as productionEnterprise_name")//制造商
	            .append(",a.productModel.productionEnterpriseInfo.registeredAddress as productionEnterprise_address")//制造商地址
				.append(",a.applicationInfo.applicationPublic.productCatalog as product_catalog ")    // 产品大类
				.append(",o.assignDate as batch_date ")          // 派组日期
				.append(",o.assignNum as days ")             // 派组天数
				.append(",o.memo as nodes ")              // 备注  
				.append(")")
				.append(" from FactoryCheckTask o ,ProcessLog pl ,FactoryCheckTask ct ,ApplicationInfoProductModel a")
				.append(" where ct.applyNo = a.applicationInfo.sioid ")
				.append(" and ct.batchNo = o.batchNo ")
				.append(" and pl.boId = a.applicationInfo.id ") 
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
		StringBuffer hql = new StringBuffer("select count(DISTINCT o.taskNo )")   
			.append(" from FactoryCheckTask o ,ProcessLog pl ,FactoryCheckTask ct ,ApplicationInfoProductModel a")
			.append(" where ct.applyNo = a.applicationInfo.sioid ")
			.append(" and ct.batchNo = o.batchNo ")
			.append(" and pl.boId = a.applicationInfo.id ") 
			.append(" and pl.taskInstanceName='")
			.append(task_name).append("' ")
			.append(getWhere(args));
		ContractService contractService = BeanAdapter.getBean("ContractService", ContractService.class);
		return contractService.queryForResultSize(hql.toString());
	}
}
