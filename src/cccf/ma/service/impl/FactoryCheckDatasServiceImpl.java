package cccf.ma.service.impl;

import java.util.List;

import cccf.ma.model.FactoryCheckTask;
import cccf.ma.model.ProcessLog;
import cccf.ma.service.FactoryCheckDatasService;
import cccf.ma.service.WorkFlowService;

public class FactoryCheckDatasServiceImpl extends AbstractBaseService implements
		FactoryCheckDatasService {
	private WorkFlowService workFlowService;  
	@Override
	public void doSubmit(String taskNo, String opid, String approveResult,
			String approveMemo) {
		if(taskNo ==null|| taskNo.isEmpty()){
			throw new RuntimeException("任务号不能为空！");
		}
		StringBuffer hql = new StringBuffer("from FactoryCheckTask where taskNo='").append(taskNo).append("'");
		FactoryCheckTask task = (FactoryCheckTask) querySingleResult(hql.toString());
		if(task ==null ){
			throw new RuntimeException("任务号为"+taskNo+"的任务找不到！");
		}
		
		
		//处理流程
		hql = new StringBuffer("select pl from ApplicationInfo o ,ProcessLog pl ")
                    .append(" where pl.boId=o.id and pl.end is null ")  
                    .append(" and o.sioid ='").append(task.getApplyNo()).append("'");
		List<ProcessLog> pList = getResultList(hql.toString());
		if(pList.size()>0){
			for(ProcessLog pItem : pList){  
				 ProcessLog processLog = new ProcessLog("ApplicationInfo", pItem.getBoId());
				 workFlowService.doTaskSubmit(pItem.getTaskInstanceId(), opid, processLog, approveResult, approveMemo ,null); 
			}  
		}

	}
	public void setWorkFlowService(WorkFlowService workFlowService) {
		this.workFlowService = workFlowService;
	}

}
