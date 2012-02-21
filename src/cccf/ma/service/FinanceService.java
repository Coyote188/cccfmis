package cccf.ma.service;

/**
 * 财务管理服务 
 */
public interface FinanceService extends BaseService{
	 /**
	    * 收费确认
	    * @param contractNo
	    * @param usid
	    * @param approveResult
	    * @param approveMemo
	    */
	   public void doChargeConfirm(String contractNo,String usid,String approveResult,String approveMemo);
}
