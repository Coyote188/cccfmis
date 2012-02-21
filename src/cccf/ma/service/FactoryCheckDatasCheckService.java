package cccf.ma.service;

/**
 * 工厂检查资料审核 
 *
 */
public interface FactoryCheckDatasCheckService extends BaseService {
	public void doSubmit(String taskNo,String opid ,String approveResult,String approveMemo);
}
