package cccf.ma.service;

/**
 * 文件审查服务
 * @author Administrator
 *
 */
public interface FactoryFileReviewService extends BaseService {
  
	/**
	 * 提交 审查结果
	 * @param taskNo
	 * @param opid
	 * @param approveResult
	 * @param approveMemo
	 */
	public void doSubmit(String taskNo,String opid ,String approveResult,String approveMemo);
}
