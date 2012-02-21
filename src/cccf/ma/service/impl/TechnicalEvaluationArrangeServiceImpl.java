package cccf.ma.service.impl;

 
import java.util.List;

import cccf.ma.model.ProcessLog;
import cccf.ma.model.TechnicalEvaluationReport;
import cccf.ma.model.TechnicalEvaluationReportOpinion;
import cccf.ma.service.TechnicalEvaluationArrangeService;
import cccf.ma.service.WorkFlowService;

public class TechnicalEvaluationArrangeServiceImpl extends AbstractBaseService
		implements TechnicalEvaluationArrangeService {
	private WorkFlowService workFlowService; 
	@Override
	public void doEvaluationArrange(String reportNo,String opid,List<TechnicalEvaluationReportOpinion> opinions,String approveResult,String approveMemo) {
		if(reportNo ==null|| reportNo.isEmpty()){
			throw new RuntimeException("技术评定报告编号不能为空！");
		} 
		if(opinions.size()!=3){
			throw new RuntimeException("技术评定人员必须为3个！");
		} 
		
		StringBuffer hql = new StringBuffer("select o from TechnicalEvaluationReport o ")
		                  .append(" where o.reportNo ='").append(reportNo).append("'"); 
		TechnicalEvaluationReport report = (TechnicalEvaluationReport) querySingleResult(hql.toString());
			
	    String[] counterSignUsers = new String[opinions.size()];
	    for(int i=0 ;i< opinions.size() ; i++){ 
	    	TechnicalEvaluationReportOpinion opinion = opinions.get(i);
	    	if(opinion.getEvaluationPersonId()==null|| opinion.getEvaluationPersonId().isEmpty()){
	    		throw new RuntimeException("技术评定人员ID不能为空！");
	    	} 
	    	counterSignUsers[i]=opinion.getEvaluationPersonId();
	    	opinion.setTechnicalEvaluationReport(report);
	    	getHibernateTemplate().save(opinion);
	    }
	    
	    //流程处理
	    hql = new StringBuffer("select pl from ApplicationInfo o , ProcessLog pl ,TechnicalEvaluationReport r ")
	        .append(" where pl.boId=o.id and pl.end is null and o.sioid=r.applyno ")
	        .append(" and r.reportNo ='").append(reportNo).append("'");
        List<ProcessLog> pList = getResultList(hql.toString()); 

		for(ProcessLog pItem : pList){ 
			 ProcessLog processLog = new ProcessLog("ApplicationInfo", pItem.getBoId()); 
			 workFlowService.doTaskSubmitToCountSign(pItem.getTaskInstanceId(), opid, processLog, approveResult, approveMemo, counterSignUsers); 
	    }  

	}
	 
	public void setWorkFlowService(WorkFlowService workFlowService) {
		this.workFlowService = workFlowService;
	}
}
