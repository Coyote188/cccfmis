package cccf.ma.service;

import cccf.ma.model.TechnicalEvaluationReportOpinion;
 
/**
 *  结论上报 
 */
public interface TechnicalEvaluationResultUpService extends BaseService{
	
	/**
	 * 
	 * @param reportNo
	 * @param usid
	 * @param approveResult
	 * @param approveMemo
	 */
   public void doEvaluationResultUp(TechnicalEvaluationReportOpinion opinion,String approveResult,String approveMemo);
}
