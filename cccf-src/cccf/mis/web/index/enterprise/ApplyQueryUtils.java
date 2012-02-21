package cccf.mis.web.index.enterprise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.modellite.spring.BeanAdapter;
import cccf.ma.service.ApplicationPublicService;

public class ApplyQueryUtils {
	
	//查询检验报告
	public static List<Map> getSurveyReports(String usid){
		ApplicationPublicService svc = BeanAdapter.getBean("ApplicationPublicService", ApplicationPublicService.class); 
		StringBuffer hql = new StringBuffer("select DISTINCT new map( ") 
	        .append("sr.id as id")
	        .append(",surveyReportSN as surveyReportSN")
	        .append(",surverOrgName as surverOrgName") 
	        .append(",sr.productCatalogueInfo.productName as productName")
	        .append(",sr.productCatalogueInfo.productParent.productName as nextProductName")
	        .append(",sr.productCatalogueInfo.productParent.productParent.productName as rootProductName") 
	        .append(") from SurveyReport sr ")
	        .append(" where sr.status = 1 ") 
	        .append(" and sr.enterpriseInfo.account.id='").append(usid).append("'")  ;   
		List<Map> srList = svc.getResultList(hql.toString()); 
		hql = new StringBuffer("select new map( ") 
	        .append("sr.id as surveyReport_id")
	        .append(",pm.id as id") //型号ID
	        .append(",pm.productCatalogueInfo.id as productId") //型号ID
	        .append(",pm.specification as specification")   //规格型号
	        .append(",pm.characterization as characterization")   //特性描述
	        .append(",pm.isMainModel as isMainModel ")  //主型  1  ,分型 0
	        .append(") from SurveyReport sr , ProductModel pm")
	        .append(" where sr.status = 1 ") 
	        .append(" and sr.id = pm.surveyReport.id")
	        .append(" and sr.enterpriseInfo.account.id='").append(usid).append("'")  ;   
		List<Map> pmList = svc.getResultList(hql.toString()); 
		
		HashMap<String,Map> tmp = new HashMap<String,Map> ();
		for(Map map :srList){
			map.put("list_ProductModel", new ArrayList());
			tmp.put((String) map.get("id"), map);
		}
		for(Map map :pmList){
			Map srMap = tmp.get(map.get("surveyReport_id"));
			List list = (List) srMap.get("list_ProductModel");
			list.add(map);
		} 
		
		return srList; 
	}

	 //流程未结束
	public static List<Map> getRunningApplys(String usid){
		StringBuffer hql = new StringBuffer("select DISTINCT new map( apm.applicationInfo.applicationPublic.applyno as applyno")
		                  .append(", apm.applicationInfo.applicationPublic.applyno as no")
		                  .append(", apm.applicationInfo.applicationPublic.businessType as business_type")
		                  .append(", apm.applicationInfo.applicationPublic.applyType as apply_type")
		                  .append(", apm.applicationInfo.applicationPublic.applydate as apply_date")
		                  .append(", apm.applicationInfo.applicationPublic.productCatalog as product_catalog")
		                  .append(", apm.productModel.surveyReport.surverOrgName as surver_orgname") 
		                  .append(", pl.taskInstanceName as status") 
		                  .append(")")
		                  .append(" from ProcessLog pl,ApplicationInfoProductModel apm  ")
		                  .append(" where pl.boId=apm.applicationInfo.id ")
		                  .append(" and pl.end is null")
                          .append(" and apm.applicationInfo.applicationPublic.applyEnterprise.account.id='").append(usid).append("'")
                          .append(" order by apm.applicationInfo.applicationPublic.applydate"); 
		     
		ApplicationPublicService svc = BeanAdapter.getBean("ApplicationPublicService", ApplicationPublicService.class); 
		return svc.getResultList(hql.toString()); 
	}
	
	 //待办事务
	public static List<Map> getTodoTask(String usid){
		StringBuffer hql = new StringBuffer("select DISTINCT new map( apm.applicationInfo.applicationPublic.applyno as applyno")
        .append(", apm.applicationInfo.applicationPublic.applyno as no")
        .append(", apm.applicationInfo.applicationPublic.businessType as business_type")
        .append(", apm.applicationInfo.applicationPublic.applyType as apply_type")
        .append(", apm.applicationInfo.applicationPublic.applydate as apply_date")
        .append(", apm.applicationInfo.applicationPublic.productCatalog as product_catalog")
        .append(", apm.productModel.surveyReport.surverOrgName as surver_orgname") 
        .append(", pl.taskInstanceName as status") 
        .append(")")
        .append(" from ProcessLog pl,ApplicationInfoProductModel apm  ")
        .append(" where pl.boId=apm.applicationInfo.id ")
        .append(" and pl.taskInstanceName='申请者修改'")
        .append(" and pl.end is null")
        .append(" and apm.applicationInfo.applicationPublic.applyEnterprise.account.id='").append(usid).append("'") 
		.append(" order by apm.applicationInfo.applicationPublic.applydate"); 

		ApplicationPublicService svc = BeanAdapter.getBean("ApplicationPublicService", ApplicationPublicService.class); 
		return svc.getResultList(hql.toString()); 
	}
	
}
