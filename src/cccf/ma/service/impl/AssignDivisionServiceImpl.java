package cccf.ma.service.impl;

import java.util.List;
import java.util.Map;

import openjframework.model.UserInfo;

import cccf.ma.model.ApplicationInfo;
import cccf.ma.model.ProcessLog;
import cccf.ma.service.AssignDivisionService;
import cccf.ma.service.WorkFlowService;

public class AssignDivisionServiceImpl extends AbstractBaseService implements
		AssignDivisionService {
	private WorkFlowService workFlowService; 
	@Override
	public void doAssignDivision(List<Map> tasks, String opid,
			String doTeskUserID, String approveResult, String approveMemo) {
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
	public void setWorkFlowService(WorkFlowService workFlowService) {
		this.workFlowService = workFlowService;
	}

}
