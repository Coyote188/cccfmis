package cccf.ma.service;

import java.util.List;
 

/**
 *  评定资料打印 服务
 */
public interface TechnicalEvaluationPrintService extends BaseService{
	/**
	 * 评定结束
	 * @param usid
	 * @param approveResult
	 * @param approveMemo
	 */
	public void doEvaluationEnd(String reportNo,String usid,String approveResult, String approveMemo);
	/**
	 * 根据报告编号得到证书里
	 * @param certNumber
	 * @return
	 */
	public List  getCertificationList(String reportNo);
	
	/**
	 * 根据applicationInfoId评定报告数据
	 * @param applicationInfoId
	 *      申请项ID
	 * @return
	 */
	public List  getEvaluationReportData(String reportNo);
	
}
