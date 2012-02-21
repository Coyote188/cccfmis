package cccf.ma.bpm;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import openjframework.bpm.model.TaskInstanceInfo;
import openjframework.bpm.service.TaskInstanceInfoServiceUtil;
import openjframework.model.UserInfo;
import openjframework.service.UserInfoServiceUtil;

import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.db.TaskMgmtSession;
import org.jbpm.taskmgmt.def.Task;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Center;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import com.aidi.bpm.service.BpmUtil;

import cccf.ma.model.AppStatusRecordInfo;
import cccf.ma.model.ApplicationInfo;
import cccf.ma.service.AppStatusRecordInfoServiceUtil;
import cccf.ma.service.ApplicationInfoServiceUtil;


public class MyTaskListStatisticsWindow extends Window {

	private static final long serialVersionUID = 1L;

	static TaskMgmtSession taskMgmtSession;
	Map params = Executions.getCurrent().getArg();
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
		Window myTaskListWindow = (Window) this.getFellow("myTLSWindow");

		listbox = this.getListbox();

		List list=(List) params.get("tasklist");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		for (int i = 0; i < list.size(); i++) {

			if (myTaskListWindow.getAttribute("pnamelen") != null)
				pnameLen = Integer.valueOf(myTaskListWindow.getAttribute(
						"pnamelen").toString());
			if (myTaskListWindow.getAttribute("enamelen") != null)
				pnameLen = Integer.valueOf(myTaskListWindow.getAttribute(
						"enamelen").toString());
			if (myTaskListWindow.getAttribute("tnamelen") != null)
				tnameLen = Integer.valueOf(myTaskListWindow.getAttribute(
						"tnamelen").toString());

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

	
	public void refreshListbox() {
		
		Listbox listbox = this.getListbox();
		List list = (List) listbox.getItems();
		list.clear();
		onCreate();

	}
}

