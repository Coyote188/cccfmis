package cccf.ma.bpm;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import openjframework.bpm.model.TaskInstanceInfo;
import openjframework.model.MessageInfo;
import openjframework.model.UserInfo;
import openjframework.service.MessageInfoServiceUtil;
import openjframework.service.UserInfoServiceUtil;
import openjframework.util.CommonDateUtil;

import com.aidi.bpm.model.*;
import com.aidi.bpm.service.*;

import cccf.myenum.*;

import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.db.TaskMgmtSession;
import org.jbpm.taskmgmt.def.Task;
import org.jbpm.taskmgmt.exe.PooledActor;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class OpenedTaskListController extends GenericForwardComposer {

	private Window myEndTaskListWindow;
	private Combobox statusCombo;
	private Textbox enterpriseName;
	private Textbox productionName;
	//private Textbox actorName;
	private Combobox status;
	private Listbox taskListbox;
	
	static TaskMgmtSession taskMgmtSession;

	static JbpmConfiguration jbpmConfiguration = JbpmConfiguration
			.getInstance();

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		// 生成状态combobox
//		for (int i = 0; i < ApplicationStatusEnum.values().length; i++) {
//			Comboitem item = new Comboitem();
//			item.setValue(i);
//			item.setLabel(CommonStatusEnum.values()[i].toString());
//			statusCombo.appendChild(item);
//		}
	}

	public void onDetail(ForwardEvent event) {

		Toolbarbutton btn = (Toolbarbutton) event.getOrigin().getTarget();
		TaskInstanceInfo taskInstanceInfo = (TaskInstanceInfo) btn
				.getAttribute("taskInstanceInfo");

		Long taskInstanceId = taskInstanceInfo.getTaskInstanceId();
		JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
		taskMgmtSession = jbpmContext.getTaskMgmtSession();
		TaskInstance taskInstance = jbpmContext.getTaskInstance(taskInstanceId);

		String rowId = "", entityName = "";
		if (taskInstance.getVariable("rowId") != null)
			rowId = taskInstance.getVariable("rowId").toString();
		if (taskInstance.getVariable("entityName") != null)
			entityName = taskInstance.getVariable("entityName").toString();
		jbpmContext.close();

		String formURL = "";
		if (entityName.equals("ApplicationInfo")) {
			formURL = "application-detail.zul";
		}
		if (entityName.equals("AppStatusRecordInfo")) {
			formURL = "appstatusrecord-detail.zul";
		}

		if (formURL.length() > 0) {
			Map params = new HashMap();
			params.put("formURL", formURL);
			params.put("taskInstanceId", taskInstanceInfo.getTaskInstanceId());
			params.put("entityName", entityName);
			params.put("userId", UserInfoServiceUtil.getCurrentLoginUser()
					.getId());
			params.put("rowId", rowId);
			Window objWindow = (Window) Executions.createComponents(formURL,
					null, params);
			try {

				objWindow.doModal();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void onRemind(ForwardEvent event) {
		Toolbarbutton btn = (Toolbarbutton) event.getOrigin().getTarget();
		TaskInstanceInfo taskInstanceInfo = (TaskInstanceInfo) btn
				.getAttribute("taskInstanceInfo");
		String actorId = taskInstanceInfo.getTaskInstance().getActorId();
		//如果是pooledactor只向第一个人发消息
		if(actorId==null){
			Set pooledActors=taskInstanceInfo.getTaskInstance().getPooledActors();
			if(pooledActors!=null){
				actorId=((PooledActor)pooledActors.iterator().next()).getActorId();
			}
		}
		MessageInfo msg = new MessageInfo();
		msg.setSubject("待处理事务催办");
		UserInfo actor = UserInfoServiceUtil.getById(actorId);
		msg.setReceiveUser(actor);
		msg.setSendUser(UserInfoServiceUtil.getCurrentLoginUser());
		msg.setSendDate(new Date());
		msg.setType(0);

		TaskInstanceInfo preTaskInstanceInfo = taskInstanceInfo.getPreTask();
		StringBuffer msgContent = new StringBuffer();
		msgContent.append("【任务名称】"
				+ taskInstanceInfo.getTaskName()
				+ "\r【发起时间】"
				+ CommonDateUtil.getSimpleDateStringYMDHm(preTaskInstanceInfo
						.getApproveDate()) + "\r【发起人】"
				+ preTaskInstanceInfo.getActorName() + "\r【执行结果】"
				+ preTaskInstanceInfo.getApproveResult() + "\r【意见】"
				+ preTaskInstanceInfo.getApproveMemo());

		msg.setContent(msgContent.toString());
		MessageInfoServiceUtil.sendMessageToDesktop(msg);

		try {
			Messagebox.show("催办消息成功发送!", "操作提示", Messagebox.OK,
					Messagebox.INFORMATION);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void onSearch() {
		String qstr = "";
	
		if (enterpriseName.getText() != "" && enterpriseName.getText() != null) {

			qstr = qstr + " and enterprise.name like ";

			qstr = qstr + "'%" + enterpriseName.getText() + "%'";

		}

		if (productionName.getText() != "" && productionName.getText() != null) {

			qstr = qstr + " and production.productName like ";

			qstr = qstr + "'%" + productionName.getText() + "%'";

		}

		
		if (qstr != "") {
			qstr = qstr.replaceFirst("and", "");
			qstr = "from ApplicationInfo where " + qstr;
		} 
		
		List<TaskInstanceInfo> searchResult =MyTaskInstanceInfoUtil.findOpenTaskListByApplication(qstr);
		List<TaskInstanceInfo> list = (List<TaskInstanceInfo>)taskListbox.getModel();
		list.clear();
		list.addAll(searchResult);
	}


}
