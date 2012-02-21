package cccf.ma.service;

import java.util.List;

import cccf.ma.model.Contract;
import cccf.ma.model.ContractFeeItem;

public interface ContractService extends BaseService{
	
	/**
	 * 制定合同 
	 * @param contract  合同
	 * @param feeItems  合同收费项
	 * @param usid      用户ID
	 * @param approveResult  结果（流程路径）
	 * @param approveMemo    备注
	 */
   public void doDrawUpContract(Contract contract,List<ContractFeeItem> feeItems,String usid,String approveResult,String approveMemo);
   
   /**
    * 合同审批审核
    * @param contractNo 合同号
    * @param usid 用户ID
    * @param approveResult  结果（流程路径）
    * @param approveMemo   备注
    */
   public void doCheck(String contractNo,int status,String usid,String approveResult,String approveMemo ); 

}
