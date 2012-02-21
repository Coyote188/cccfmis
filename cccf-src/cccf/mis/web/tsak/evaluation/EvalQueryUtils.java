package cccf.mis.web.tsak.evaluation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cccf.ma.service.BaseService;

public class EvalQueryUtils {

	public static Map getApplyInfo(BaseService svc,String applyno){
		StringBuffer hql = new StringBuffer("select new map(applyno as applyno")
			.append(",applydate as applydate")     //提交日期/提交日期
				.append(",productCatalog as productCatalog")    
			.append(",applyEnterprise.name as enterprise_name")   // 企业名称 
			.append(",applyEnterprise.state as enterprise_state")// 国家
			.append(",applyEnterprise.location as enterprise_location")// 地区
			.append(",applyType as applyType")//申请类型 
			.append(",businessType as businessType")//业务类型
			.append(",applyEnterprise.registeredAddress as registeredAddress")//企业注册地址
			.append(",applyEnterprise.postalcode as postalcode")  //邮编 
			.append(",applyEnterprise.legalPerson as enterprise_legalPerson") //法人姓名
			.append(",applyEnterprise.telForLegal as enterprise_telForLegal")    //法人电话 
			.append(")")
			.append(" from ApplicationPublicInfo o")
			.append(" where applyno='").append(applyno).append("'");
			
		return (Map)svc.querySingleResult(hql.toString());
	}
	 
	
	//申请附件
	public static List<Map> getAppAttachments(BaseService svc,String applyno){
		StringBuffer hql = new StringBuffer("select DISTINCT new map(") 
		  .append("o.id as id") 
		  .append(",")
		  .append("o.name as name") 
		  .append(",")
	      .append("o.attachment.path as file_path")           //附件路径
	      .append(",")
	      .append("o.attachment.name as file_name")            //主型  true  ,分型false
          .append(",")
	      .append("o.attachment.id as file_id")                  //附件ID 
	      .append(")")
	      .append(" from ApplicationPublicInfoAttachment o")
	      .append(" where")
	      .append(" o.applicationPublicInfo.applyno='").append(applyno).append("'") 
		  .append(" order by sn asc"); 
		return  svc.getResultList(hql.toString()); 
	}
	
	//申请附件审核信息
	public static List<Map> getAttachmentApprove(BaseService svc,String applyno){
		StringBuffer hql = new StringBuffer("select DISTINCT new map(")
		        .append("apa.id as id")
		        .append(",o.approverID as approverID")     // 审批 、审核人ID
		        .append(",o.approverName as approverName")  // 审批 、审核人姓名
		        .append(",o.approveResult as approveResult") // 审批 、审核结论
		        .append(",o.approveMemo as approveMemo")     // 备注
		        .append(",o.approveDate as approveDate")   // 审批 、审核时间
		        .append(")")
		        .append(" from ObjectApproveInfo o ,ApplicationPublicInfoAttachment apa")
		        .append(" where o.objectId = apa.id and o.objectType='ApplicationPublicInfoAttachment'") 
		        .append(" and apa.applicationPublicInfo.applyno='").append(applyno).append("'") ;
		return svc.getResultList(hql.toString()); 
	}
	
	public static List<Map> getProductlList(BaseService svc,String applyno){
		StringBuffer hql = new StringBuffer("select DISTINCT new map(") 
		      .append("o.productModel.surveyReport.id as id")
		      .append(",")
		      .append("o.productModel.productCatalogueInfo.productName as productName")//产品名称
		      .append(",") 
		      .append("o.productModel.surveyReport.surverOrgName as surverOrgName")//质检中心 
		      .append(",")
		      .append("o.productModel.productCatalogueInfo.observedStandard as observedStandard")//执行标准
		      .append(",")
		      .append("o.productModel.productionEnterpriseInfo.name as productionEnterpriseName")//生产厂
		      .append(",")
		      .append("o.productModel.manufactureInfo.name as manufactureName")//制造商
		       .append(",")
		      .append("o.productModel.manufactureInfo.registeredAddress as registeredAddress")//实际生产地址
		      .append(")")
		      .append(" from ApplicationInfoProductModel o")
		      .append(" where")
		      .append(" o.applicationInfo.sioid='").append(applyno).append("'"); 
		return svc.getResultList(hql.toString()); 
	}
	// --产品型号 
	public static List<Map> getProductModelList(BaseService svc,String applyno){ 
		StringBuffer hql = new StringBuffer("select DISTINCT new map(")
		      .append("o.productModel.id as id")                              //产品ID
		      .append(",")
		      .append("o.productModel.specification as specification")        //规格型号
		      .append(",")
		      .append("o.productModel.characterization as characterization")   //特性描述
		      .append(",")
		      .append("o.productModel.isMainModel as isMainModel")            //主型  true  ,分型false
		      .append(",")  
		      .append("o.productModel.surveyReport.id as surveyReport_id")    
		      .append(")")
		      .append(" from ApplicationInfoProductModel o")
		      .append(" where")
		      .append(" o.applicationInfo.sioid='").append(applyno).append("'");   
		
		return svc.getResultList(hql.toString()); 
	}
	
	//-- 产品型号的附件
	public static List<Map> getProductModelAttachmentList(BaseService svc,String applyno){ 
		StringBuffer hql = new StringBuffer("select DISTINCT new map(")
	      .append("o.productModel.id as productModel_id")  //产品型号ID
	      .append(",")
	      .append(" p.name as name")                        //附件名称
	      .append(",")
	      .append(" p.attachment.name as file_name")         //附件文件名
	      .append(",")
	      .append(" p.attachment.path as file_path")         //附件文件路径
	      .append(",")
	      .append(" p.attachment.id as file_id")             //附件ID
	      .append(",")
	      .append(" p.id as id")                       //申请产品型号ID
	      .append(")")
	      .append(" from ApplicationInfoProductModel o , ProductModelAttachment p")
	      .append(" where")
	      .append(" o.productModel=p.productModel") 
	      .append(" and o.applicationInfo.sioid='").append(applyno).append("'");  
		
		return svc.getResultList(hql.toString()); 
	}
	//-- 产品型号的附件 审核信息
	public static List<Map> getProductModelAttachmentApproveList(BaseService svc,String applyno){
		StringBuffer hql = new StringBuffer("select DISTINCT new map(")
	        .append("pma.id as id")
	        .append(",o.approverID as approverID")     // 审批 、审核人ID
	        .append(",o.approverName as approverName")  // 审批 、审核人姓名
	        .append(",o.approveResult as approveResult") // 审批 、审核结论
	        .append(",o.approveMemo as approveMemo")     // 备注
	        .append(",o.approveDate as approveDate")   // 审批 、审核时间
	        .append(")")
	        .append(" from ObjectApproveInfo o ,ProductModelAttachment pma , ApplicationInfoProductModel apm")
	        .append(" where o.objectId = pma.id and o.objectType='ProductModelAttachment'") 
	        .append(" and apm.productModel = pma.productModel")
	        .append(" and apm.applicationInfo.sioid='").append(applyno).append("'") ;
		return svc.getResultList(hql.toString()); 
	}
	
	public static void buildObjectLinkApprove(List<Map> objList, List<Map> approveList){
		HashMap<String,Map> tmpMap = new HashMap<String,Map>();
		for (Map map : objList ){ 
			 map.put("approveList", new ArrayList());
			 tmpMap.put((String) map.get("id"), map); 
		} 
		 
		if(approveList!=null){
			for (Map map : approveList ){  
				Map attachment = tmpMap.get((String) map.get("id"));  
				List list = (List)attachment.get("approveList");
				list.add(map);
				 
			}
		}
	}
	//关联产品型号附件 
	public static void buildProductModelLinkAttachment(List<Map> productModels,List<Map> attachments){
		HashMap<String,Map> tmpMap = new HashMap<String,Map>();
	 	Set<String> idSet =new HashSet<String>();
	 	for(Map i : productModels){ 
	 		tmpMap.put((String) i.get("id"), i);
	 		i.put("productModelAttachmentList", new ArrayList ());
	 		idSet.add((String) i.get("surveyReport_id"));
	 	} 
	 	for(Map map : attachments){
	 		Map productModel= tmpMap.get((String) map.get("productModel_id"));
	 		List list = (List) productModel.get("productModelAttachmentList");
	 		list.add(map);  
	 	}  
	}
	
	//关联产品附件 
	public static void buildProductLinkProductModel(List<Map> productList,List<Map> productModelList){
		HashMap<String,Map> tmpMap = new HashMap<String,Map>();
		 
		for(Map i : productList){ 
	 		tmpMap.put((String) i.get("id"), i);
	 		i.put("productModelList", new ArrayList ());
	 	}
	 	for(Map i : productModelList){
	 		Map surveyReport=tmpMap.get((String) i.get("surveyReport_id"));
	 		List list = (List) surveyReport.get("productModelList");
	 		list.add(i); 
	 	}
	}
	
	
	public static Map getSurveyReport(BaseService svc,String surveyReportSN){
		 StringBuffer hql = new StringBuffer("select new map(")
		                  .append("id as id")
		                  .append(",").append("surveyReportSN as surveyReportSN")
		                  .append(",").append("enterpriseInfo.name as enterpriseInfo_name")
		                  .append(",").append("productCatalogueInfo.productName as productCatalogueInfo_productName")
		                  .append(",").append("manufactureInfo.name as manufactureInfo_name")
		                  .append(",").append("productionEnterpriseInfo.name as productionEnterpriseInfo_name")
		                  .append(",").append("surverType as surverType")
		                  .append(",").append("surverVerdict as surverVerdict")
		                  .append(",").append("organizer as organizer")
		                  .append(",").append("organizeTime as organizeTime")
		                  .append(",").append("auditor as auditor")
		                  .append(",").append("auditTime as auditTime")
		                  .append(",").append("surveyFromDate as surveyFromDate")
		                  .append(",").append("surveyToDate as surveyToDate")
		                  .append(",").append("surverOrgName as surverOrgName")
		                  .append(",").append("surverAccording as surverAccording")
		                  .append(",").append("surverVerdictContent as surverVerdictContent")
		                  .append(",").append("issuingDate as issuingDate")
		                  .append(",").append("agentOrg as agentOrg")
		                  .append(",").append("samplingOrg as samplingOrg")
		                  .append(",").append("status as status")
		                  .append(",").append("attachment.id as file_id")
		                  .append(")").append("from SurveyReport o ")
		                  .append(" where surveyReportSN='").append(surveyReportSN).append("'");  
		 Map params = (Map)svc.querySingleResult(hql.toString()); 
		 String file_id = (String)params.get("file_id");
		 if(file_id!=null){
			 String file_path = (String) svc.querySingleResult("select path from Attachment where id='"+file_id+"'");
			 params.put("file_path", file_path);
		 }else{
			 params.put("file_path", null);
		 }
		String srid = (String)params.get("id");
		// 获得检验报告中的产品型号
		hql = new StringBuffer("select new map(o.id as id,o.specification as specification,o.characterization as characterization ,isMainModel as isMainModel)")
		                    .append(" from ProductModel o ")
		                    .append(" where o.surveyReport.id='").append(srid).append("'"); 
		List<Map> productModelList = svc.getResultList(hql.toString());
		 
		//获得检验报告中产品型号的附件
		hql = new StringBuffer("select new map(o.productModel.id as productModel_id,o.name as name,o.attachment.name as fileName,o.attachment.path as file_path)")
		         .append(" from ProductModelAttachment o where o.productModel.surveyReport.id='").append(srid).append("'");
	 	  
		List<Map> productModelAttachmentList = svc.getResultList(hql.toString());
		//关联产品型号附件 
	 	HashMap<String,Map> tmpMap = new HashMap<String,Map>();
	 	for(Map i : productModelList){
	 		tmpMap.put((String) i.get("id"), i);
	 		i.put("productModelAttachmentList", new ArrayList ());
	 	}
	 	for(Map i : productModelAttachmentList){
	 		Map productModel=tmpMap.get((String) i.get("productModel_id"));
	 		List list = (List) productModel.get("productModelAttachmentList");
	 		list.add(i);
	 		productModel.put("productName",params.get("productCatalogueInfo_productName"));
	 	}
	 	
	 	params.put("productModelList",productModelList);  
	 	return params;
	}
	
}
