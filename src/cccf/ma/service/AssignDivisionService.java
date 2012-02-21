package cccf.ma.service;

import java.util.List;
import java.util.Map;

/**
 * 派组分工 
 *
 */
public interface AssignDivisionService extends BaseService {
	/**
	 * 派组分工 
	 * @param tasks 任务列表
	 * @param opid  
	 * @param doTeskUserID
	 * @param approveResult
	 * @param approveMemo
	 */
	public void doAssignDivision(List<Map> tasks,String opid,String doTeskUserID,String approveResult,String approveMemo);
}
