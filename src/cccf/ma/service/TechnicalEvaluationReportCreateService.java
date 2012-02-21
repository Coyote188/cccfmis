package cccf.ma.service;

import java.util.List;

import cccf.ma.model.TechnicalEvaluationReport;
import cccf.ma.model.TechnicalEvaluationReportOpinion;

import com.aidi.bpm.model.Approve;

/**
 * 技术评定报告生成
 */
public interface TechnicalEvaluationReportCreateService extends BaseService {
	/**
	 * 报告生成
	 * @param report  报告
	 * @param opinion 意见
	 * @param mdids   选中的产品型号列表 
	 * @param usid    操作人
	 * @param approveResult  流程路径
	 * @param approveMemo
	 */
	public void doReportCreate(TechnicalEvaluationReport report,
			TechnicalEvaluationReportOpinion opinion, List<String> mdids,
			String usid,String approveResult,String approveMemo);
}
