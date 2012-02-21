package cccf.ma.service;

import cccf.ma.model.TechnicalEvaluationReport;

/**
 *  评定审批 服务
 */
public interface TechnicalEvaluationApprovalService extends BaseService{
	/**
	 * 评定审批
	 */
   public void doEvaluationApproval(TechnicalEvaluationReport opinion,String usid,String approveResult,String approveMemo);
}
