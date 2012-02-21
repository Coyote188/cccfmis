package cccf.ma.bpm.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import openjframework.bpm.model.TaskInstanceInfo;
import openjframework.bpm.service.TaskInstanceInfoServiceUtil;
import openjframework.model.MessageInfo;
import openjframework.model.UserInfo;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.taskmgmt.exe.PooledActor;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;

import com.aidi.bpm.model.Approve;
import com.aidi.bpm.service.ApproveServiceUtil;
import com.aidi.bpm.service.BpmUtil;

import openjframework.service.*;
import openjframework.util.CommonDateUtil;

public class NoticeCurrentActorAction implements ActionHandler {

	private static final long serialVersionUID = 1L;
	//private String rowId, entityName;
	String actorId = null;

	public void execute(ExecutionContext executionContext) throws Exception {

		//rowId = executionContext.getVariable("rowId").toString();
		//entityName = executionContext.getVariable("entityName").toString();

		TaskInstance ti = executionContext.getTaskInstance();

		String taskName = ti.getName();
		String msgSubject = "您有新的待处理任务：" + taskName;
		if (ti.getActorId() == null && ti.getPooledActors() != null && ti.getPooledActors().size()>0) {
			Set as = ti.getPooledActors();
			PooledActor p = (PooledActor) as.iterator().next();
			actorId = p.getActorId();
			// 发送消息
			doMessage(msgSubject, executionContext.getTaskInstance());
		} else {
			if (ti.getActorId() != null) {
				actorId = ti.getActorId();
				UserInfo loginUser = UserInfoServiceUtil.getCurrentLoginUser();
				// if (!loginUser.getId().equals(actorId)) {
				// 发送消息
				doMessage(msgSubject, executionContext.getTaskInstance());
				// }
			}

		}

	}

	private void doMessage(String msgSubject, TaskInstance taskInstance) {
		StringBuffer msgContent = new StringBuffer();

		UserInfo user = UserInfoServiceUtil.getCurrentLoginUser();

		if (actorId != null) {
			msgContent.append("【任务名称】"
					+ taskInstance.getName()
					+ "\r【发起时间】"
					+ CommonDateUtil.getSimpleDateStringYMDHm(taskInstance
							.getCreate()) + "\r【发起人】" + user.getName());

			if (!MessageInfoServiceUtil.isExistMsg(msgContent.toString())) {
				// 将消息存入数据库用于离线
				MessageInfo msg = new MessageInfo();
				msg.setReceiveUser(UserInfoServiceUtil.getById(actorId));
				msg.setContent(msgContent.toString());
				Date dt = CommonDateUtil.getSimpleToDay();
				msg.setSendDate(dt);
				msg.setReadStatus(0);
				msg.setType(1);
				msg.setTaskinstanceId(taskInstance.getId());
				msg.setSendUser(user);
				msg.setSubject(msgSubject);
				MessageInfoServiceUtil.sendMessageToDesktop(msg);
			}

			// msg.setType(1);
			// MessageInfoServiceUtil.create(msg);
			//
			// // 发送消息
			// EventQueues.lookup(actorId + "msgEventQueue",
			// EventQueues.APPLICATION, true).publish(
			// new Event("onMsgEventQueue", null, msg));

		}
	}
}
