package cccf.ma.service.impl;

import java.util.List;

import cccf.ma.model.Contract;
import cccf.ma.model.ProcessLog;
import cccf.ma.service.FinanceService;
import cccf.ma.service.WorkFlowService;

public class FinanceServiceImpl extends AbstractBaseService implements FinanceService {
	private WorkFlowService workFlowService;   
	@Override
	public void doChargeConfirm(String contractNo, String usid,
			String approveResult, String approveMemo) {
		if(contractNo ==null|| contractNo.isEmpty()){
			throw new RuntimeException("合同号不能为空！");
		}
		StringBuffer hql = new StringBuffer("from Contract where contractNo='").append(contractNo).append("'");
		Contract contract = (Contract) querySingleResult(hql.toString());
		if(contract ==null ){
			throw new RuntimeException("合同号为"+contractNo+"的合同找不到！");
		}
		
		contract.setStatus(0);
		getHibernateTemplate().update(contract);
		
		//处理流程
		hql = new StringBuffer("select pl from ApplicationInfo o ,ProcessLog pl")
                    .append(" where pl.boId=o.id and pl.end is null ") 
                    .append(" and o.sioid ='").append(contract.getApplyno()).append("'");
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
