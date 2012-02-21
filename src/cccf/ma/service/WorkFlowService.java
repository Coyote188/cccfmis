package cccf.ma.service;

import java.util.List; 

import cccf.ma.model.ProcessLog;

public interface WorkFlowService extends BaseService{
	/**
	 * 
	 * @param usid
	 * @param taskNodeName
	 * @return
	 */
	public List getUnDoTaskIdsByUsid(String usid,String taskNodeName);
	
    /**
     * 启动并且执行任务		
     * @param formUrl
     * @param userId
     * @param variable
     * @param approveResult
     * @param approveMemo
     * @return
     */
	public long doStartProcessAndDoTask(String formUrl, String userId, List<ProcessLog> variables,String approveResult,String approveMemo);
	
	public long doStartProcessAndDoTask(String formUrl, String userId, ProcessLog processLog ,String approveResult,String approveMemo);

	/**
	 * 提交任务，并发起下一任务
	 * @param taskInstanceId
	 * @param approveResult
	 * @param approveMemo
	 * @param  accepterId 
	 */
    public void doTaskSubmit(Long taskInstanceId, String userId , List<ProcessLog> variables,String approveResult,String approveMemo,String accepterId);
	
    public void doTaskSubmit(Long taskInstanceId, String userId ,ProcessLog processLog,String approveResult,String approveMemo,String accepterId);

    /**
     * 会签
     * @param taskInstanceId
     * @param userId
     * @param variables
     * @param approveResult
     * @param approveMemo
     * @param counterSignUsers
     */
    public void doTaskSubmitToCountSign(Long taskInstanceId, String userId , List<ProcessLog> variables,String approveResult,String approveMemo,String... counterSignUsers);
    
    public void doTaskSubmitToCountSign(Long taskInstanceId, String userId ,ProcessLog processLog,String approveResult,String approveMemo,String... counterSignUsers);
    

}
