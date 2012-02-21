package cccf.ma.service;

import java.util.List;

import cccf.ma.model.AssignBatch; 

/**
 * 派组批次处理服务 
 *
 */
public interface AssignBatchService extends BaseService{ 
	/**
	 * 创建批次
	 * @param batch
	 * @param opid
	 * @param approveResult
	 * @param approveMemo
	 */
	 public void doCreateBatch(AssignBatch batch , String opid ,String approveResult,String approveMemo);
	     
    /**
     * 批次审核审批
     * @param batchNo
     * @param usid
     * @param approveResult
     * @param approveMemo
     */
    public void doCheck(String batchNo,int status,String usid,String approveResult,String approveMemo);
}
