package cccf.ma.service.impl;
 
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
 
import cccf.ma.model.ObjectApproveInfo;
import cccf.ma.model.ProcessLog;
import cccf.ma.service.ConformityReviewService;
import cccf.ma.service.ObjectApproveService;
import cccf.ma.service.WorkFlowService;

public class ConformityReviewServiceImpl extends AbstractBaseService implements
		ConformityReviewService {
	public static final String ApproveName = "符合性审查";
	
	private ObjectApproveService objectApproveService;
	
	private WorkFlowService workFlowService; 
	
	@Override
	public void doSaveProModAttApprove(String applyno,
			String productModelAttachmentId , String approverID,
			String approverName, String approveResult, String approveMemo) {
		ObjectApproveInfo po = new ObjectApproveInfo();
		po.setApproveCode(ApproveName+"_"+applyno);
		po.setApproveDate(new Date());
		po.setApproveMemo(approveMemo);
		po.setApproveResult(approveResult);
		po.setApproverID(approverID);
		po.setApproverName(approverName); 
		po.setObjectId(productModelAttachmentId);
		po.setObjectType("ProductModelAttachment");
		 
		getHibernateTemplate().save(po);
	}
	
	@Override
	public void doSaveAppAttApprove(String applyno,String applicationPublicInfoAttachmentId,
			  String approverID, String approverName,
			String approveResult, String approveMemo) { 
		ObjectApproveInfo po = new ObjectApproveInfo();
		po.setApproveCode(ApproveName+"_"+applyno);
		po.setApproveDate(new Date());
		po.setApproveMemo(approveMemo);
		po.setApproveResult(approveResult);
		po.setApproverID(approverID);
		po.setApproverName(approverName); 
		po.setObjectId(applicationPublicInfoAttachmentId);
		po.setObjectType("ApplicationPublicInfoAttachment");
		 
		getHibernateTemplate().save(po);
	}
	//申请信息
	private Map getConformityInfo(String applyno){
		StringBuffer hql=new StringBuffer("select DISTINCT new map(") 
	      .append("o.applyno as applyno")            //申请号
	      .append(",")                                 
	      .append("o.applydate as applydate")       //提交日期   
          .append(",")
	      .append("o.applyEnterprise.name as enterprise_name")   //申请企业
	      .append(",")
	      .append("o.applyEnterprise.state||'-'||o.applyEnterprise.location as enterprise_state_location")   //申请企业
	      .append(",")
	      .append("o.applyEnterprise.name as enterprise_name")   //申请企业
	      .append(",")
	      .append("o.applyType as applyType")                     //申请类型
	      .append(",")
	      .append("o.businessType as businessType")         //业务类型
	      .append(",")
	      .append("o.applyEnterprise.registeredAddress as registeredAddress")    //企业注册地址
	      .append(",")
	      .append("o.applyEnterprise.postalcode as enterprise_legalPerson")         //法人_
	      .append(",")
	      .append("o.applyEnterprise.telForLegal as enterprise_telForLegal")         //法人电话
	      .append(")")
	      .append(" from ApplicationPublicInfo o")
	      .append(" where")
	      .append(" o.applyno='").append(applyno).append("'") ; 
		return (Map) querySingleResult(hql.toString());
	}
	//申请附件
	private List<Map> getAppAttachments(String applyno){
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
		return getHibernateTemplate().find(hql.toString()); 
	}
	//申请附件审核信息
	private List<Map> getAttachmentApprove(String applyno){
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
		        .append(" and o.approveCode='").append(ApproveName).append("_").append(applyno).append("'")
		        .append(" and apa.applicationPublicInfo.applyno='").append(applyno).append("'") ;
		return getHibernateTemplate().find(hql.toString()); 
	}
	
	private List<Map> getProductlList(String applyno){
		StringBuffer hql = new StringBuffer("select DISTINCT new map(") 
		      .append("o.productModel.surveyReport.id as id")
		      .append(",o.productModel.surveyReport.surveyReportSN as surveyReportSN")
		      .append(",o.applicationInfo.applicationPublic.productCatalog as productCatalog")
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
		return getHibernateTemplate().find(hql.toString()); 
	}
	// --产品型号 
	private List<Map> productModelList(String applyno){ 
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
		List<Map> productModelList =  getHibernateTemplate().find(hql.toString());
		
		return productModelList;
	}
	
	//-- 产品型号的附件
	private List<Map> getProductModelAttachmentList(String applyno){ 
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
		List<Map> productModelAttachmentList =getHibernateTemplate().find(hql.toString());
		
		return productModelAttachmentList;
	}
	//-- 产品型号的附件 审核信息
	private List<Map> getProductModelAttachmentApproveList(String applyno){
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
	        .append(" and o.approveCode='").append(ApproveName).append("_").append(applyno).append("'")
	        .append(" and apm.productModel = pma.productModel")
	        .append(" and apm.applicationInfo.sioid='").append(applyno).append("'") ;
        return getHibernateTemplate().find(hql.toString()); 
	}
	
	private void buildApproveLink(List<Map> objList, List<Map> approveList){
		HashMap<String,Map> tmpMap = new HashMap<String,Map>();
		if(objList!=null){
			int i=1;
			for (Map map : objList ){ 
				 map.put("sn", i++);  //附件序号  
				 map.put("approverID", null); 
				 map.put("approverName", null); 
				 map.put("approveResult", "未审核"); 
				 map.put("approveMemo", null); 
				 map.put("approveDate", null); 
				 tmpMap.put((String) map.get("id"), map);
			}
		}   
		if(approveList!=null){
			for (Map map : approveList ){ 
				Map attachment = tmpMap.get((String) map.get("id"));  
				attachment.put("approverID", map.get("approverID")); 
				attachment.put("approverName", map.get("approverName")); 
				attachment.put("approveResult", map.get("approveResult")); 
				attachment.put("approveMemo", map.get("approveMemo")); 
				attachment.put("approveDate", map.get("approveDate")); 
			}
		}
	}
	
	
	@Override
	public Map getApplyDatasByNo(String applyno) { 
		Map conformityInfo = (Map) getConformityInfo(applyno); 
		
		//申请材料列表  
		List<Map> attachments = getAppAttachments(applyno);
		conformityInfo.put("attachments", attachments);
		 
		buildApproveLink(attachments,getAttachmentApprove(applyno));
		
		
		/**申请产品信息列表**/ 
		List<Map> productList = getProductlList(applyno);  
		// --产品型号  
		List<Map> productModelList =  productModelList(applyno); 
		//-- 产品型号的附件 
		List<Map> productModelAttachmentList =getProductModelAttachmentList(applyno); 
		List<Map> productModelAttachmentApproveList = getProductModelAttachmentApproveList(applyno); 
		buildApproveLink(productModelAttachmentList,productModelAttachmentApproveList);
		
		
		//关联产品型号附件 
		HashMap<String,Map> tmpMap = new HashMap<String,Map>();
	 	Set<String> idSet =new HashSet<String>();
	 	for(Map i : productModelList){ 
	 		tmpMap.put((String) i.get("id"), i);
	 		i.put("productModelAttachmentList", new ArrayList ());
	 		idSet.add((String) i.get("surveyReport_id"));
	 	} 
	 	for(Map map : productModelAttachmentList){
	 		Map productModel= tmpMap.get((String) map.get("productModel_id"));
	 		List list = (List) productModel.get("productModelAttachmentList");
	 		list.add(map);  
	 	}  
		
		//关联
		tmpMap.clear();
		for(Map i : productList){ 
	 		tmpMap.put((String) i.get("id"), i);
	 		i.put("productModelList", new ArrayList ());
	 	}
	 	for(Map i : productModelList){
	 		Map surveyReport=tmpMap.get((String) i.get("surveyReport_id"));
	 		List list = (List) surveyReport.get("productModelList");
	 		list.add(i); 
	 	}
	 	
	 	conformityInfo.put("productList",productList); //申请产品信息列表  
		 
	 	System.out.println(conformityInfo);
		return conformityInfo;
	}
	 
	@Override
	public void doSubmit(String applyno, String approveResult,
			String approveMemo, String usid,
			List<ObjectApproveInfo> fileApprovelist) { 
		  
		 StringBuffer hql = new StringBuffer("select pl from ApplicationInfo o , ProcessLog pl")
		                .append(" where pl.boId=o.id and pl.end is null ") 
		                .append(" and o.sioid ='").append(applyno).append("'");
		 List<ProcessLog> pList = getResultList(hql.toString());
		 if(pList.size()>0){
			 for(ProcessLog pItem : pList){ 
				 
				 ProcessLog processLog = new ProcessLog("ApplicationInfo", pItem.getBoId());
				 workFlowService.doTaskSubmit(pItem.getTaskInstanceId(), usid, processLog, approveResult, approveMemo ,null); 
			 } 
		 
			for(ObjectApproveInfo obj:fileApprovelist)
			{
				objectApproveService.save(obj);
			}
		 }
	} 
	
	
 
 

	public void setObjectApproveService(ObjectApproveService objectApproveService) {
		this.objectApproveService = objectApproveService;
	}

	public void setWorkFlowService(WorkFlowService workFlowService) {
		this.workFlowService = workFlowService;
	}
	
 
}
