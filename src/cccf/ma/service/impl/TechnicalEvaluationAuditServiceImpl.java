package cccf.ma.service.impl;

import java.util.List;

import cccf.ma.model.ProcessLog;
import cccf.ma.model.TechnicalEvaluationReport; 
import cccf.ma.service.TechnicalEvaluationAuditService;
import cccf.ma.service.WorkFlowService;

public class TechnicalEvaluationAuditServiceImpl extends AbstractBaseService
		implements TechnicalEvaluationAuditService {
	private WorkFlowService workFlowService; 
	
	@Override
	public void doEvaluationAudit(TechnicalEvaluationReport report,
			String usid, String approveResult, String approveMemo) {
		StringBuffer hql = new StringBuffer("select o from TechnicalEvaluationReport o")
		                      .append(" where o.reportNo ='").append(report.getReportNo()).append("'");  
		TechnicalEvaluationReport entity = (TechnicalEvaluationReport)  querySingleResult(hql.toString());
		if(entity==null){
			throw new RuntimeException("评定报告找不到！");
		}
		entity.setSymposiumDate(report.getSymposiumDate());
		entity.setOpinion(report.getOpinion());
		entity.setParticipant(report.getParticipant());
		getHibernateTemplate().update(entity);
		
		 //流程处理
	    hql = new StringBuffer("select pl from ApplicationInfo o , ProcessLog pl ,TechnicalEvaluationReport r ")
		        .append(" where pl.boId=o.id and pl.end is null  and o.sioid=r.applyno ")
		        .append(" and r.reportNo ='").append(entity.getReportNo()).append("'");
	    List<ProcessLog> pList = getResultList(hql.toString()); 

		for(ProcessLog pItem : pList){ 
			 ProcessLog processLog = new ProcessLog("ApplicationInfo", pItem.getBoId());
			 workFlowService.doTaskSubmit(pItem.getTaskInstanceId(),usid, processLog, approveResult, approveMemo,null);
	    }  
		
	}
	
	public void setWorkFlowService(WorkFlowService workFlowService) {
		this.workFlowService = workFlowService;
	} 
}
