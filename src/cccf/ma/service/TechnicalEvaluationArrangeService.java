package cccf.ma.service;

import java.util.List;
import java.util.Map;

import cccf.ma.model.TechnicalEvaluationReportOpinion;

/**
 *  评定安排 服务
 */
public interface TechnicalEvaluationArrangeService extends BaseService{
	/**
	 * 评定安排
	 * @param reportNo 技术评定报告编号
	 * @param opid
	 * @param doTeskUserID
	 * @param approveResult
	 * @param approveMemo
	 */
  public void doEvaluationArrange(String reportNo,String opid,List<TechnicalEvaluationReportOpinion> doTeskUsers,String approveResult,String approveMemo) ;
}
