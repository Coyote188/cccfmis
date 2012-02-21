package cccf.ma.service.impl;

import java.util.List;
 
import cccf.ma.model.AssignPlan; 
import cccf.ma.model.ProcessLog;
import cccf.ma.service.WorkFlowService;

public class AssignPlanServiceImpl extends AbstractBaseService implements
		cccf.ma.service.AssignPlanService {
	private WorkFlowService workFlowService;  
	@Override
	public void doAssignPlan(AssignPlan plan, String usid,
			String approveResult, String approveMemo) {
		if(plan.getApplyno()==null|| plan.getApplyno().isEmpty()){
			throw new RuntimeException("申请号不能为空！");
		}
		if(approveResult==null|| approveResult.isEmpty()){
			throw new RuntimeException("流程路径不能为空！");
		}   
		  
		getHibernateTemplate().save(plan);  
		
		//处理流程
		StringBuffer hql = new StringBuffer("select pl from ApplicationInfo o ,ProcessLog pl")
                    .append(" where pl.boId=o.id and pl.end is null ") 
                    .append(" and o.sioid ='").append(plan.getApplyno()).append("'");
		List<ProcessLog> pList = getResultList(hql.toString());
		if(pList.size()>0){
			for(ProcessLog pItem : pList){  
				 ProcessLog processLog = new ProcessLog("ApplicationInfo", pItem.getBoId());
				 workFlowService.doTaskSubmit(pItem.getTaskInstanceId(), usid, processLog, approveResult, approveMemo ,null); 
			}  
		}
 

	}
	public void setWorkFlowService(WorkFlowService workFlowService) {
		this.workFlowService = workFlowService;
	}

}
