package cccf.ma.bpm;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import openjframework.bpm.model.TaskInstanceInfo;
import openjframework.bpm.service.TaskInstanceInfoServiceUtil;
import openjframework.model.UserInfo;
import openjframework.service.UserInfoServiceUtil;

import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.db.TaskMgmtSession;
import org.jbpm.taskmgmt.exe.PooledActor;
import org.jbpm.taskmgmt.exe.TaskInstance;

import cccf.ma.model.AppStatusRecordInfo;
import cccf.ma.model.ApplicationInfo;
import cccf.ma.service.AppStatusRecordInfoServiceUtil;
import cccf.ma.service.ApplicationInfoServiceUtil;

import com.aidi.bpm.model.Approve;
import com.aidi.bpm.service.ApproveServiceUtil;
import com.aidi.bpm.service.BpmUtil;

public class MyTaskInstanceInfoUtil {

	static TaskMgmtSession taskMgmtSession;

	static JbpmConfiguration jbpmConfiguration = JbpmConfiguration
			.getInstance();

	/**
	 * 指定用户的已办事务(须在approve有记录)
	 * 
	 * @param userId
	 * @return
	 */
	public static List<MyTaskInstanceInfo> getTaskInstanceInfoListByUserId(
			String userId) {

		List<MyTaskInstanceInfo> taskInstanceInfoList = new ArrayList();
		List<Approve> approveList = ApproveServiceUtil
				.getApproveListByUserId(userId);
		if (approveList != null) {
			for (Approve approve : approveList) {
				TaskInstance taskInstance = BpmUtil.getTaskInstance(approve
						.getTaskInstanceId());
				if (taskInstance != null) {
					MyTaskInstanceInfo task = new MyTaskInstanceInfo();
					task.setTaskName(taskInstance.getName());
					UserInfo user = UserInfoServiceUtil.getById(approve
							.getUserId());
					task.setActorName(user.getName());
					task.setApproveMemo(approve.getApproveMemo());
					task.setApproveResult(approve.getApproveResult());
					task.setApproveDate(approve.getApproveDate());
					task.setTaskInstanceId(taskInstance.getId());

					// 前一任务
					Long taskInstanceId = taskInstance.getId();
					TaskInstanceInfo preTaskInstanceInfo = TaskInstanceInfoServiceUtil
							.getPreTaskInstanceInfo(taskInstanceId);
					task.setPreTask(preTaskInstanceInfo);

					// 申请
					if (approve.getEntityName().equals("ApplicationInfo")) {
						ApplicationInfo applicationInfo = ApplicationInfoServiceUtil
								.getById(approve.getRowId());
						task.setApplicationInfo(applicationInfo);
					}
					if (approve.getEntityName().equals("AppStatusRecordInfo")) {
						AppStatusRecordInfo appStatusRecordInfo = AppStatusRecordInfoServiceUtil
								.getById(approve.getRowId());
						ApplicationInfo applicationInfo = appStatusRecordInfo
								.getApplication();
						task.setApplicationInfo(applicationInfo);
					}
					taskInstanceInfoList.add(task);
				}
			}
		}

		return taskInstanceInfoList;
	}

	/**
	 * 取得所有待处理任务信息
	 * 
	 * @return
	 */
	public static List<MyTaskInstanceInfo> getAllInstanceInfoListOfOpened() {
		List<MyTaskInstanceInfo> taskInstanceInfoList = new ArrayList();
		List<TaskInstance> taskInstanceList = BpmUtil
				.getAllTaskInstanceListOfOpened();
		for (TaskInstance ti : taskInstanceList) {
			Long taskInstanceId = ti.getId();
			JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
			taskMgmtSession = jbpmContext.getTaskMgmtSession();
			TaskInstance taskInstance = jbpmContext
					.getTaskInstance(taskInstanceId);

			MyTaskInstanceInfo task = new MyTaskInstanceInfo();
			task.setTaskInstance(taskInstance);
			task.setTaskName(taskInstance.getName());
			if (taskInstance.getActorId() != null) {
				UserInfo user = UserInfoServiceUtil.getById(taskInstance
						.getActorId());
				task.setActorName(user.getName());
			} else {
				Set poolActors = taskInstance.getPooledActors();
				if (poolActors != null) {
					String actors="";
					for(Object actor:poolActors){
						String aid=((PooledActor)actor).getActorId();
						UserInfo user = UserInfoServiceUtil.getById(aid);
						actors="["+user.getName()+"]";
						if(poolActors.size()>1) actors=actors+"等";
						task.setActorName(actors);
						break;
					}
				}

			}

			task.setTaskInstanceId(taskInstance.getId());
			task.setDueDate(taskInstance.getDueDate());

			// 申请
			if (taskInstance.getVariable("entityName") != null) {
				String entityName = taskInstance.getVariable("entityName")
						.toString();
				String rowId = taskInstance.getVariable("rowId").toString();
				if (entityName.equals("ApplicationInfo")) {
					ApplicationInfo applicationInfo = ApplicationInfoServiceUtil
							.getById(rowId);
					applicationInfo.getEnterprise().setSimpleNameLen(12);// 企业简名用12个字
					applicationInfo.getProduction().setSimpleNameLen(12);// 产品简名用12个字
					task.setApplicationInfo(applicationInfo);
				}
				if (entityName.equals("AppStatusRecordInfo")) {
					AppStatusRecordInfo appStatusRecordInfo = AppStatusRecordInfoServiceUtil
							.getById(rowId);
					ApplicationInfo applicationInfo = appStatusRecordInfo
							.getApplication();
					applicationInfo.getEnterprise().setSimpleNameLen(12);
					applicationInfo.getProduction().setSimpleNameLen(12);// 产品简名用12个字
					task.setApplicationInfo(applicationInfo);
				}

			}
			jbpmContext.close();

			// 前一任务
			Long pretaskInstanceId = taskInstance.getId();
			TaskInstanceInfo preTaskInstanceInfo = TaskInstanceInfoServiceUtil
					.getPreTaskInstanceInfo(pretaskInstanceId);
			task.setPreTask(preTaskInstanceInfo);

			taskInstanceInfoList.add(task);

		}
		return taskInstanceInfoList;
	}
	
	/**根据application条件查询待处理任务
	 * @param sql
	 * @return
	 */
	public static List findOpenTaskListByApplication(String sql){
		List<MyTaskInstanceInfo> taskInstanceInfoList = new ArrayList();
		List<ApplicationInfo> applicationList=ApplicationInfoServiceUtil.findByQuery(sql);
		List<String> aidList= new ArrayList();
		for(ApplicationInfo app:applicationList){
			aidList.add(app.getId());
		}
		
		List<MyTaskInstanceInfo>  tiList=getAllInstanceInfoListOfOpened();
		for(MyTaskInstanceInfo task:tiList){
			if(aidList.indexOf(task.getApplicationInfo().getId())!=-1){
				taskInstanceInfoList.add(task);
			}
		}
		
		return taskInstanceInfoList;
	}
}
