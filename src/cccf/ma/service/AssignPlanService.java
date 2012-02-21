package cccf.ma.service;

import cccf.ma.model.AssignPlan;

/**
 * 派组检验计划 服务
 */
public interface AssignPlanService extends BaseService {
	/**
	 * 派组检验计划
	 * @param plan
	 * @param usid
	 * @param approveResult
	 * @param approveMemo
	 */
   public void doAssignPlan(AssignPlan plan,String usid,String approveResult,String approveMemo);
}
