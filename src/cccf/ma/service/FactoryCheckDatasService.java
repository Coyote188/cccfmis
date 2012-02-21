package cccf.ma.service;

/**
 * 工厂检查资料填写
 */
public interface FactoryCheckDatasService extends BaseService {

	public void doSubmit(String taskNo,String opid ,String approveResult,String approveMemo);
}
