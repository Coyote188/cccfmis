package cccf.ma.service.impl;

import java.util.Date;
import java.util.List;

import cccf.ma.model.ProcessLog;
import cccf.ma.model.TechnicalEvaluationReportOpinion;
import cccf.ma.service.TechnicalEvaluationResultUpService;
import cccf.ma.service.WorkFlowService;

public class TechnicalEvaluationResultUpServiceImpl extends AbstractBaseService implements
		TechnicalEvaluationResultUpService {
	private WorkFlowService workFlowService;  
	@Override
	public void doEvaluationResultUp(TechnicalEvaluationReportOpinion opinion,
			String approveResult, String approveMemo) { 
		
		StringBuffer hql = new StringBuffer("select o from TechnicalEvaluationReportOpinion o where o.status=0 ")
					        .append(" and technicalEvaluationReport.reportNo='").append(opinion.getTechnicalEvaluationReport().getReportNo()).append("'") 
					        .append(" and o.evaluationPersonId ='").append(opinion.getEvaluationPersonId()).append("'");
		TechnicalEvaluationReportOpinion entity = (TechnicalEvaluationReportOpinion) querySingleResult(hql.toString());
		if(entity==null){
			throw new RuntimeException("您没有安排为技术评定人员或已经完成评定！");
		}
		 
		entity.setEvaluationTime(new Date());
		entity.setMemo(opinion.getMemo());
		entity.setOpinionContent(opinion.getOpinionContent());
		entity.setApproveResult(opinion.getApproveResult());
		entity.setStatus(1);
	 
		//保存评定结论
		 getHibernateTemplate().update(entity);  
		 
		 //流程处理
	    hql = new StringBuffer("select pl from ApplicationInfo o , ProcessLog pl ,TechnicalEvaluationReport r ")
		        .append(" where pl.boId=o.id and pl.end is null and o.sioid=r.applyno ")
		        .append(" and r.reportNo ='").append(entity.getTechnicalEvaluationReport().getReportNo()).append("'");
	    List<ProcessLog> pList = getResultList(hql.toString()); 

		for(ProcessLog pItem : pList){  
			 workFlowService.doTaskSubmit(pItem.getTaskInstanceId(), entity.getEvaluationPersonId(), new ProcessLog("ApplicationInfo", pItem.getBoId()), approveResult, approveMemo,null);
	    }  
	}
	public void setWorkFlowService(WorkFlowService workFlowService) {
		this.workFlowService = workFlowService;
	}
	 
}
