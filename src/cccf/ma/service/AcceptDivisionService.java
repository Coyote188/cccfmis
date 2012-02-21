package cccf.ma.service;

import java.util.List;
import java.util.Map;

/**
 *  受理分工服务
 *
 */
public interface AcceptDivisionService extends BaseService {
	//获取待受理分工列表
	public List<Map> getToAcceptDivisionList();
	
	/**
	 * 受理分工
	 * @param tasks   任务列表
	 * @param opid    任务分配人员
	 * @param doTeskUserID  任务接收人员
	 * @param approveResult  一步环节
	 * @param approveMemo   
	 */
	public void doAcceptDivision(List<Map> tasks,String opid,String doTeskUserID,String approveResult,String approveMemo);
}
