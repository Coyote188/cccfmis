package cccf.ma.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.aidi.core.service.BaseDAOServcieUtil;

import cccf.ma.model.ApplicationPublicInfo;
import cccf.ma.model.Contract; 
import cccf.ma.model.ContractFeeItem;
import cccf.ma.model.ProcessLog;
import cccf.ma.service.ContractService;
import cccf.ma.service.WorkFlowService;

public class ContractServiceImpl extends AbstractBaseService implements
		ContractService { 
	private WorkFlowService workFlowService;  
	
	@Override
	public void doCheck(String contractNo,int status, String usid, String approveResult, String approveMemo ) {
		if(contractNo ==null|| contractNo.isEmpty()){
			throw new RuntimeException("合同号不能为空！");
		}
		StringBuffer hql = new StringBuffer("from Contract where contractNo='").append(contractNo).append("'");
		Contract contract = (Contract) querySingleResult(hql.toString());
		if(contract ==null ){
			throw new RuntimeException("合同号为"+contractNo+"的合同找不到！");
		}
		
		contract.setStatus(status);
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
	
	@Override
	public void doDrawUpContract(Contract contract,List<ContractFeeItem> feeItems, String usid,
			String approveResult, String approveMemo) { 
		if(contract.getApplyno()==null|| contract.getApplyno().isEmpty()){
			throw new RuntimeException("申请号不能为空！");
		}
		if(approveResult==null|| approveResult.isEmpty()){
			throw new RuntimeException("流程路径不能为空！");
		}
		StringBuffer hql = new StringBuffer("select o from ApplicationPublicInfo o where  o.applyno ='").append(contract.getApplyno()).append("'");
		ApplicationPublicInfo api = (ApplicationPublicInfo) querySingleResult(hql.toString());
		contract.setApplicationPublicInfo(api); 
		if("强制性认证".equals(api.getBusinessType())){
			contract.setContractNo(getContractNo("CCC"));
		}else{
			contract.setContractNo(getContractNo("HT"));
		}
		
		contract.setStatus(1);  
		getHibernateTemplate().save(contract);
		
		if(feeItems!=null){
			int i=0;
			
			for(ContractFeeItem item : feeItems){
				String sn = "000" + i;
				i++;
				sn= sn.substring(sn.length() - 3, sn.length());
				item.setItemName(sn);
				// 关联合同
				item.setContract(contract);
				getHibernateTemplate().save(item); 
			}
		}
		
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

	/**生成合同号***/
	private static int countAppNo = 0;
	private static String oldNo = null;
	private synchronized static String getContractNo(String h) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String no = h + sdf.format(new Date());
		if(oldNo==null || !oldNo.equals(no)){
			countAppNo=0;
			oldNo = no;
		} 
		if(countAppNo==0){               //3C201106060001
			StringBuffer hql =new StringBuffer("select new map( max( substring(")
			                  .append("o.contractNo").append(",").append(9+h.length()).append(",4)) as no )")
			                  .append(" from ").append("Contract o ")
			                  .append(" where substring(").append("o.contractNo").append(",1,")
			                  .append(8+h.length()).append(") ='").append(no).append("'");
		 
			List list =   BaseDAOServcieUtil.findByQueryString(hql.toString());
			if (list.size() == 1) {
				Map m = (Map) list.get(0);
				String applyno = (String) m.get("contractNo");
				if(applyno!=null){
				   countAppNo = new Integer(no); 
				}
			}  
		}
		
		countAppNo++;
		String sn = "0000" + countAppNo;
		no += sn.substring(sn.length() - 4, sn.length());
		return no;
	}

	
   public static void main(String[] args){
	   
   }
}
