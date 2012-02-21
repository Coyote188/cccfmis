package cccf.ma.bpm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import openjframework.bpm.model.TaskInstanceInfo;
import openjframework.bpm.service.TaskInstanceInfoServiceUtil;
import openjframework.model.*;
import openjframework.service.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.*;

import cccf.ma.model.AppStatusRecordInfo;
import cccf.ma.model.ApplicationInfo;
import cccf.ma.service.AppStatusRecordInfoServiceUtil;
import cccf.ma.service.ApplicationInfoServiceUtil;

import com.aidi.bpm.service.*;
import com.aidi.bpm.model.*;

import com.aidi.bpm.service.BpmUtil;

import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.JbpmException;
import org.jbpm.graph.def.*;
import org.jbpm.db.*;
import org.jbpm.*;
import org.jbpm.db.GraphSession;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.graph.exe.Token;
import org.jbpm.taskmgmt.*;
import org.jbpm.taskmgmt.def.*;
import org.jbpm.taskmgmt.exe.*;
import org.jbpm.taskmgmt.impl.*;
import org.jbpm.context.exe.*;

public class EnterpriseMyTaskListWindow extends Window {

	static TaskMgmtSession taskMgmtSession;

	static JbpmConfiguration jbpmConfiguration = JbpmConfiguration
			.getInstance();
	UserInfo user;
	String userId;
	Listbox listbox;

	/**
	 * Select the first item in the list so that we dont have to worry about
	 * Null Pointer Exceptions in other methods
	 * 
	 */
	public void onCreate() {

		// 企业名及产品名的长度
		int nameLen = 6;
		int pnameLen = 6;
		int tnameLen = 10;
		Window myTaskListWindow = (Window) this.getFellow("myTaskListWindow");
		

		listbox = this.getListbox();

		UserInfo user = UserInfoServiceUtil.getCurrentLoginUser();
		userId = String.valueOf(user.getId());
		// Page page = this.getPage();
		// page.setVariable("userId", userId);
		List list = BpmUtil.getMyTaskList(userId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		for (int i = 0; i < list.size(); i++) {
			
			if (myTaskListWindow.getAttribute("pnamelen") != null)
				pnameLen = Integer.valueOf(myTaskListWindow
						.getAttribute("pnamelen").toString());
			if (myTaskListWindow.getAttribute("enamelen") != null)
				pnameLen = Integer.valueOf(myTaskListWindow
						.getAttribute("enamelen").toString());
			if (myTaskListWindow.getAttribute("tnamelen") != null)
				tnameLen = Integer.valueOf(myTaskListWindow
						.getAttribute("tnamelen").toString());
			
			TaskInstance taskInstance = (TaskInstance) list.get(i);
			Listitem item = new Listitem();
			item.setParent(listbox);
			item.setId(String.valueOf(taskInstance.getId()));

			JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
			taskMgmtSession = jbpmContext.getTaskMgmtSession();
			TaskInstance ti = jbpmContext.getTaskInstance(taskInstance.getId());

			String entityName = "";
			// 产品
			String rowId = "", pname = "", ename = "";
			if (ti.getVariable("rowId") != null) {
				rowId = ti.getVariable("rowId").toString();
			}
			if (ti.getVariable("entityName") != null) {
				entityName = ti.getVariable("entityName").toString();
			}
			if (entityName.equals("ApplicationInfo")) {
				ApplicationInfo app = ApplicationInfoServiceUtil.getById(rowId);
				pname = app.getProduction().getProductName();
				ename = app.getEnterprise().getName();
			}

			jbpmContext.close();

			if (entityName.equals("AppStatusRecordInfo")) {
				AppStatusRecordInfo appStatusRecordInfo = AppStatusRecordInfoServiceUtil
						.getById(rowId);
				ApplicationInfo applicationInfo = appStatusRecordInfo
						.getApplication();
				pname = applicationInfo.getProduction().getProductName();
				ename = applicationInfo.getEnterprise().getName();
			}

			if (ename.length() < nameLen)
				nameLen = ename.length();
			Listcell cellEName = new Listcell(ename.substring(0, nameLen));
			cellEName.setParent(item);
			cellEName.setTooltiptext(ename);

			if (pname.length() < pnameLen)
				pnameLen = pname.length();
			Listcell cell0 = new Listcell(pname.substring(0, pnameLen));
			cell0.setParent(item);
			cell0.setTooltiptext(pname);

			String tname = taskInstance.getName();
			if (tname.length() < tnameLen)
				tnameLen = tname.length();
			Listcell cell = new Listcell(tname.substring(0, tnameLen));
			cell.setParent(item);
			cell.setTooltiptext(tname);

			// 前一任务
			Long taskInstanceId = taskInstance.getId();
			TaskInstanceInfo preTaskInstanceInfo = TaskInstanceInfoServiceUtil
					.getPreTaskInstanceInfo(taskInstanceId);
			Listcell cell1 = new Listcell(preTaskInstanceInfo.getActorName());
			cell1.setParent(item);

			String cdate = sdf.format(taskInstance.getCreate());
			Listcell cell2 = new Listcell(cdate);
			cell2.setParent(item);

		}

		

		

	}

	/**
	 * Extract the list of webforms from the Model in the listbox
	 * 
	 * @return
	 */
	public List<TaskInstance> getTaskInstanceList() {
		Listbox listbox = getListbox();
		List<TaskInstance> list = (List<TaskInstance>) listbox.getModel();
		return list;
	}

	/**
	 * Gets the Main user listbox element
	 * 
	 * @return
	 */
	public Listbox getListbox() {
		return (Listbox) this.getFellow("taskListbox");
	}

	/**
	 * Creates the Edit/Add User box in the Model format
	 * 
	 * @param params
	 */
	public void openWebFormWindow(Map params) {

		Window objWindow = (Window) Executions.createComponents(
				"/mytask_form.zul", null, params);

		try {

			objWindow.doModal();
			// refreshListbox();
			refreshListbox();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void openURLFormWindow(Map params) {
	
		String formUrl = params.get("formURL").toString();
		Window objWindow = (Window) Executions.createComponents(formUrl, null,
				params);
	
		try {
			objWindow.doModal();
		} catch (SuspendNotAllowedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// refreshListbox();
		 refreshListbox();
		 // 向桌面发送消息以刷新endtasklist
		 EventQueues.lookup(userId + "refreshEndTaskListEvent",
		 EventQueues.APPLICATION, true).publish(
		 new Event("onMsgEventQueue", null, ""));
	}

	/**
	 * Refresh the listbox by clearing is and reloading it from the database
	 * 
	 */
	public void refreshListbox() {
		// Listbox listbox = this.getListbox();
		// Listitem selected = listbox.getSelectedItem();
		// Long tid = Long.parseLong(selected.getId());
		//
		// UserInfo user = UserInfoServiceUtil.getCurrentLoginUser();
		// userId = String.valueOf(user.getId());
		// List list = BpmUtil.getMyTaskList(userId);
		// Boolean isDel = true;
		// for (int i = 0; i < list.size(); i++) {
		// TaskInstance taskInstance = (TaskInstance) list.get(i);
		// if (taskInstance.getId() == tid)
		// isDel = false;
		// }
		// if (isDel)
		// listbox.removeChild(selected);

		Listbox listbox = this.getListbox();
		List list = (List) listbox.getItems();
		list.clear();
		onCreate();

	}

	public void onDoTask() {

		if (!selectTaskError())
			return;

		Listbox listbox = this.getListbox();
		Listitem item = listbox.getSelectedItem();
		String taskInstanceId = item.getId();
		JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
		taskMgmtSession = jbpmContext.getTaskMgmtSession();
		TaskInstance taskInstance = jbpmContext.getTaskInstance(Long
				.parseLong(taskInstanceId));
		Task task = taskInstance.getTask();

		String rowId = "", entityName = "";
		if (taskInstance.getVariable("rowId") != null)
			rowId = taskInstance.getVariable("rowId").toString();
		if (taskInstance.getVariable("entityName") != null)
			entityName = taskInstance.getVariable("entityName").toString();
		TaskForm taskForm = TaskFormServiceUtil.getByTaskId(task.getId());
		String formURL = "";
		if (taskForm != null) {
			formURL = taskForm.getFormURL();
		}

		final Execution exec = Executions.getCurrent();
		HttpServletRequest req = (HttpServletRequest) exec.getNativeRequest();
		HttpSession session = req.getSession();

		// formURL = formURL+";jsessionid="+session.getId() + "?taskInstanceId="
		// + String.valueOf(taskInstance.getId()) + "&rowId=" + rowId
		// + "&entityName=" + entityName + "&userId=" + userId;
		if (taskInstance.getActorId() == null) {
			taskInstance.setActorId(userId);
		}
		jbpmContext.save(taskInstance);
		jbpmContext.close();

		HashMap params = new HashMap();
		params.put("formURL", formURL);
		params.put("width", taskForm.getWindowWidth());
		params.put("height", taskForm.getWindowHeight());

		params.put("taskInstanceId", taskInstanceId);
		params.put("entityName", entityName);
		params.put("userId", userId);
		params.put("rowId", rowId);
		openURLFormWindow(params);

	}

	public void onDetail(ForwardEvent event) {

		Toolbarbutton btn = (Toolbarbutton) event.getOrigin().getTarget();
		
		Long taskInstanceId = Long.valueOf(btn.getAttribute("taskInstanceId")
				.toString());
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

		HashMap params = new HashMap();
		params.put("entityName", entityName);
		params.put("userId", userId);
		params.put("rowId", rowId);
		params.put("taskInstanceId", taskInstanceId);
		Window objWindow = (Window) Executions.createComponents(formURL, null,
				params);
		try {

			objWindow.doModal();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean selectTaskError() {
		Listbox listbox = this.getListbox();
		Listitem item = listbox.getSelectedItem();
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
}
