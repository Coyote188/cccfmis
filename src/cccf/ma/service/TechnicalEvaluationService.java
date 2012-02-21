package cccf.ma.service;

import java.util.List;
import java.util.Map;

import com.aidi.bpm.model.Approve;

import cccf.ma.model.TechnicalEvaluation;
import cccf.ma.model.TechnicalEvaluationOpinion;

/**
 * 技术评定
 */
public interface TechnicalEvaluationService extends BaseService {
	
	/**
	 * 根据证书编号得到证书里
	 * @param certNumber
	 * @return
	 */
	public List  getCertificationList(String applicationInfoId);
	
	/**
	 * 根据applicationInfoId评定报告数据
	 * @param applicationInfoId
	 *      申请项ID
	 * @return
	 */
	public List  getEvaluationReportData(String applicationInfoId);

	/**
	 * 初评
	 * 
	 * @param te
	 *            技术评定
	 * @param mdids
	 *            产品型号ID列表
	 * @param opinion
	 *            评定意见
	 * @param applicationInfoID
	 *            申请项ID
     * @param approve
	 *            流程任务信息
	 */
	public void doInitializeEvaluation(TechnicalEvaluation te,
			List<String> mdids, TechnicalEvaluationOpinion opinion,
			String applicationInfoID,Approve approve );

	/**
	 * 执行评定
	 * 
	 * @param opinion
	 *            评定意见
	 * @param applicationInfoID
	 *            申请项ID
	 * @return
	 */
	public void doEvaluation(TechnicalEvaluationOpinion opinion,
			String applicationInfoID,Approve approve );

	/**
	 * 汇总
	 * 
	 * @param te
	 *            技术评定
	 * @param mdids
	 *            产品型号ID列表
	 * @param opinion
	 *            评定意见
	 * @param applicationInfoID
	 *            申请项ID
	 */
	public void doSummaryEvaluation(TechnicalEvaluation te, List<String> mdids,
			TechnicalEvaluationOpinion opinion, String applicationInfoID,Approve approve );

	/**
	 * 评定结果审核
	 
	 * @param auditor
	 *            审核人ID
	 * @param result
	 *            审核结论：通过/不通过
	 * @param memo
	 *            审核备注
	 * @param applicationInfoID
	 *            申请项ID
	 */
	public void doResultAudit( String auditorId,
			String result, String memo, String applicationInfoID);

}
