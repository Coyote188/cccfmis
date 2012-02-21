package cccf.ma.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import openjframework.bpm.model.TaskInstanceInfo;
import openjframework.bpm.service.TaskInstanceInfoServiceUtil;
import openjframework.model.UserInfo;

import org.jbpm.taskmgmt.exe.TaskInstance;
import org.springframework.orm.hibernate3.HibernateTemplate;

import cccf.ma.common.JbpmUtil;
import cccf.ma.model.ApplicationInfo;
import cccf.ma.model.ProcessLog;
import cccf.ma.service.AcceptDivisionService;
import cccf.ma.service.WorkFlowService;

public class AcceptDivisionServiceImpl extends AbstractBaseService implements
		AcceptDivisionService  { 
	private WorkFlowService workFlowService; 
	
	@Override
	public void doAcceptDivision(List<Map> tasks,String opid,String doTeskUserID,String approveResult,String approveMemo) { 
		 for(Map item : tasks){
			 String applyno = (String) item.get("applyno");//申请号
			 StringBuffer hql = new StringBuffer("select pl from ApplicationInfo o , ProcessLog pl")
			                .append(" where pl.boId=o.id and pl.end is null ")
			                .append(" and o.sioid ='").append(applyno).append("'");
			 List<ProcessLog> pList = getResultList(hql.toString());
			 
			 for(ProcessLog pItem : pList){
				 ApplicationInfo application = (ApplicationInfo) getHibernateTemplate().load(ApplicationInfo.class, pItem.getBoId()); 
				 application.setAcceptUser((UserInfo) getHibernateTemplate().load(UserInfo.class, opid));
				 application.setReportUser(doTeskUserID);  
				 getHibernateTemplate().update(application);
				 
				 ProcessLog processLog = new ProcessLog("ApplicationInfo", pItem.getBoId());
				 workFlowService.doTaskSubmit(pItem.getTaskInstanceId(), opid, processLog, approveResult, approveMemo, doTeskUserID); 
			 } 
		 } 
	}
 
	@Override
	public List<Map> getToAcceptDivisionList(){
		StringBuffer hql = new StringBuffer("select DISTINCT new map(")
	      .append("o.applicationPublic.id as applyId")     
	      .append(",")
	      .append("o.applicationPublic.applyno as applyno")        //规格型号
	      .append(",")
	      .append("o.applicationPublic.applyEnterprise.name as enterprisename")   // 申请企业名称
	      .append(",")
	      .append("o.applicationPublic.applyEnterprise.state as enterpriseState") //申请企业国家
	      .append(",")
	      .append("o.applicationPublic.applyEnterprise.location as enterpriseLocation") //申请企业区域
	      .append(",")
	      .append("o.applicationPublic.productCatalog as productCatalog")   //申请产品大类 
	      .append(",")
	      .append("o.applicationPublic.applyType  as applytype")   //申请类型
	      .append(",")
	      .append("o.applicationPublic.businessType  as businesstype")   //业务类型
	      .append(",")
	      .append("o.applicationPublic.applydate  as applydate")   //申请时间
	      .append(")")
	      .append(" from ApplicationInfo o")
	      .append(" where")
	      .append(" o.stauts=1");  
	   List<Map> retList =   getResultList(hql.toString());
	   Map tmpMap = new HashMap ();
	   for(Map item: retList){
		   tmpMap.put((String)item.get("applyId"), item);
		   item.put("tasks", new ArrayList()); 
	   }
	   hql = new StringBuffer("select DISTINCT new map(")
		      .append("o.id  as applicationId")   //业务类型
		      .append(",")
		      .append("o.applicationPublic.id  as applicationPublicId")   //公共信息ID
		      .append(",")
		      .append("o.processInstanceId  as processInstanceId")   //业务类型
		      .append(",")
		      .append("o.production.productName  as productname")   //产品名称
		      .append(")")
		      .append(" from ApplicationInfo o")
		      .append(" where")
		      .append(" o.stauts=1");  
	   List<Map> tmpList =   getResultList(hql.toString());
	   for(Map item: tmpList){
		    Map appPub = (Map) tmpMap.get(item.get("applicationPublicId")); 
		    List tasks =  (List) appPub.get("tasks");
		    tasks.add(item); 
		    
		    Long processInstanceId= (Long) item.get("processInstanceId");
		    TaskInstance ti = JbpmUtil.getCurrentTaskInstance(processInstanceId);
		    if(ti==null)continue; 
		    
		    item.put("taskId", ti.getId());  
			//任务名称
		    item.put("taskname",ti.getName());
		    
		    // 前一任务
			Long taskInstanceId = ti.getId();
			TaskInstanceInfo preTaskInstanceInfo = TaskInstanceInfoServiceUtil.getPreTaskInstanceInfo(taskInstanceId);
			//发起人员
		    item.put("username",preTaskInstanceInfo.getActorName());
			//发起时间
		    item.put("date",preTaskInstanceInfo.getActorName());
		    item.put("sn", tasks.size());
	   }
		
	   return retList;
	}

	public void setWorkFlowService(WorkFlowService workFlowService) {
		this.workFlowService = workFlowService;
	}


}
