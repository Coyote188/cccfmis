package cccf.ma.bpm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import openjframework.bpm.model.TaskInstanceInfo;
import openjframework.model.MessageInfo;
import openjframework.model.UserInfo;
import openjframework.service.MessageInfoServiceUtil;
import openjframework.service.UserInfoServiceUtil;
import openjframework.util.CommonDateUtil;
import openjframework.util.ZkFileUtil;
import cccf.ma.model.ApplicationInfo;
import cccf.ma.service.ApplicationInfoServiceUtil;
import com.aidi.bpm.model.*;
import com.aidi.bpm.service.*;
import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.db.TaskMgmtSession;
import org.jbpm.taskmgmt.def.Task;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Center;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class MyEndTaskListController extends GenericForwardComposer {
	Window myEndTaskListWindow;
	static TaskMgmtSession taskMgmtSession;
	private Listbox taskListbox;
	static JbpmConfiguration jbpmConfiguration = JbpmConfiguration
			.getInstance();

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	}
	public void onClick$pdvTlb()
	{
		if (!selectTaskError())
			return;
		TaskInstanceInfo taskInstanceInfo = (TaskInstanceInfo) taskListbox
		.getSelectedItem().getValue();
		Long taskInstanceId = taskInstanceInfo.getTaskInstanceId();
		String dirPath = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/");//若流程改变,更改这里的路径
		String body=ExtractProcessDiagram.ProcessImageForCurrentTask(taskInstanceId, dirPath+"workflow\\app\\gpd.xml","workflow/app/processimage.jpg");
		String filename=ExtractProcessDiagramImpl.draw(taskInstanceInfo.getTaskName(), body);
		Executions.getCurrent().sendRedirect("/proDiagrams/"+filename, "_blank");
	}
	public void onDetail2() {
		if (!selectTaskError())
			return;
		TaskInstanceInfo taskInstanceInfo = (TaskInstanceInfo) taskListbox
				.getSelectedItem().getValue();
		// Toolbarbutton btn = (Toolbarbutton) event.getOrigin().getTarget();
		// TaskInstanceInfo taskInstanceInfo = (TaskInstanceInfo) btn
		// .getAttribute("taskInstanceInfo");
		Approve approve = ApproveServiceUtil
				.getByTaskInstanceId(taskInstanceInfo.getTaskInstanceId());
		Long taskInstanceId = taskInstanceInfo.getTaskInstanceId();
		JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
		taskMgmtSession = jbpmContext.getTaskMgmtSession();
		TaskInstance taskInstance = jbpmContext.getTaskInstance(taskInstanceId);
		Task task = taskInstance.getTask();
		String taskName = task.getName();
		String rowId = "", entityName = "";
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
//			Div divDetail = (Div) self.getFellow("divDetail2");
//			if (divDetail.getChildren() != null)
//				divDetail.getChildren().clear();
			Map<Object, Object> params = new HashMap<Object, Object>();
			params.put("formURL", formURL);
			params.put("taskInstanceId", taskInstanceInfo.getTaskInstanceId());
			params.put("entityName", entityName);
			params.put("userId", UserInfoServiceUtil.getCurrentLoginUser()
					.getId());
			params.put("rowId", approve.getRowId());
			Window frmPersonCenterModel = (Window) Executions.createComponents("person_center_form.zul", null, null);
			Window objWindow = (Window) Executions.createComponents(formURL,
					null, params);
			objWindow.setParent(frmPersonCenterModel);
			frmPersonCenterModel.setTitle(task.getName());
			objWindow.setWidth("auto");
			objWindow.setHeight("auto");
			objWindow.setBorder("none");
			try {
				frmPersonCenterModel.doModal();
				objWindow.doEmbedded();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private boolean selectTaskError() {
		Listitem item = taskListbox.getSelectedItem();
		if (item == null) {
			try {
				Messagebox.show("记选择一条记录!", "操作提示", Messagebox.OK,
						Messagebox.ERROR);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
		} else {
			return true;
		}
	}

	public void onRollbackTask() {
		if (!selectTaskError())
			return;
		try {
			if (Messagebox.show("任务回退后需重新执行，是否确定回退该任务?", "任务回退提示?",
					Messagebox.OK | Messagebox.NO, Messagebox.QUESTION) == Messagebox.OK) {
				MyTaskInstanceInfo taskInstanceInfo = (MyTaskInstanceInfo) taskListbox
						.getSelectedItem().getValue();
				if (BpmUtil.rollbackTaskInstance(taskInstanceInfo
						.getTaskInstanceId())) {
					taskListbox.getItems()
							.remove(taskListbox.getSelectedItem());
					if (taskInstanceInfo.getTaskName().equals("受理分工")) {
						ApplicationInfo application = taskInstanceInfo
								.getApplicationInfo();
						application.setStauts(1);
						ApplicationInfoServiceUtil.update(application);
					}
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
