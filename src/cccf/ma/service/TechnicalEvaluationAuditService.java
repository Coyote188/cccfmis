package cccf.ma.service;

import cccf.ma.model.TechnicalEvaluationReport;

/**
 *  评定审核 服务
 */
public interface TechnicalEvaluationAuditService extends BaseService{
	/**
	 * 评定审核
	 */
   public void doEvaluationAudit(TechnicalEvaluationReport opinion,String usid,String approveResult,String approveMemo);
}
