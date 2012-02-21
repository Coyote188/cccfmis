package cccf.ma.web.zk;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import openjframework.bpm.model.TaskInstanceInfo;
import openjframework.bpm.service.TaskInstanceInfoServiceUtil;
import openjframework.service.UserInfoServiceUtil;
import openjframework.util.ZkFileUtil;

import org.jbpm.taskmgmt.exe.TaskInstance;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import cccf.ma.model.AppStatusRecordInfo;
import cccf.ma.model.ApplicationInfo;
import cccf.ma.service.AppStatusRecordInfoServiceUtil;
import cccf.ma.service.ApplicationInfoServiceUtil;
import cccf.myenum.ApplicationChangeStatus;

import com.aidi.bpm.service.BpmUtil;
import com.aidi.bpm.zk.BpmZkUtil;

public class AppstatusRecordApproveWindow
		extends Window
{
	public AppStatusRecordInfo	appStatusRecord;
	Map							params		= Executions.getCurrent().getArg();
	String						userId;
	String						processId	= null;
	String						rowId;
	ApplicationInfo				application;
	Combobox					changeTypeCombo;
	Long						taskInstanceId;
	String						entityName	= "AppStatusRecordInfo";
	String[]					fileFieldArr;
	String[]					fileUrlArr;
	
	Groupbox otherFiles;
	
	public void onCreate()
	{
		Components.wireVariables(this, this);
		
		changeTypeCombo = (Combobox) this.getFellow("changeType");
		Textbox approveMemo = (Textbox) this.getFellow("approveMemo");
		try
		{
		appStatusRecord = (AppStatusRecordInfo) this.getPage().getVariable("appStatusRecord");
		rowId = appStatusRecord.getAsid();
		}catch (Exception e) {
			// TODO: handle exception
		}
		application = (ApplicationInfo) this.getPage().getVariable("inspectionApplication");
		fileFieldArr = application.getFieldNameArr();
		fileUrlArr = application.getFileUrlArr();
		if (params.get("taskInstanceId") != null)
		{
			taskInstanceId = Long.parseLong(params.get("taskInstanceId").toString());
			// 该任务已结束,用于已办事务详情
			TaskInstanceInfo taskInstanceInfo = TaskInstanceInfoServiceUtil.getEndedTaskInstanceInfo(taskInstanceId);
			if (taskInstanceInfo != null)
			{
				try
				{
				Textbox resultTextbox = (Textbox) this.getFellow("resultTextbox");
				 
				resultTextbox.setText(taskInstanceInfo.getApproveResult());
				approveMemo.setText(taskInstanceInfo.getSimpleMemo());
				}catch (Exception e) {
					// TODO: 有问题
				}
				 
			}
		}
		userId = params.get("userId").toString();
		Grid appoveGrid = (Grid) this.getSpaceOwner().getFellow("appoveGrid");
		BpmZkUtil.generateApproveGrid(appoveGrid, taskInstanceId);
		changeTypeCombo.setValue(ApplicationChangeStatus.values()[appStatusRecord.getChangeType()].toString());
		setFileNameLabel();
		// 是否允许分型号
		if (application.getProduction() != null)
		{
			if (application.getProduction().isModelStatus())
			{
				HashMap params = new HashMap();
				params.put("application", application);
				Window objWindow = (Window) Executions.createComponents("productionmodel-listview.zul", null, params);
			 
				objWindow.setParent(otherFiles);
				// otherFiles
				// objWindow
				objWindow.doEmbedded();
			}
		}
	}
	private void setFileNameLabel()
	{
		for (int i = 0; i < fileFieldArr.length; i++)
		{
			String s = fileUrlArr[i];
			if (s != null && !s.isEmpty() && !fileFieldArr[i].equals("qualityManual"))
			{
				Toolbarbutton tb = (Toolbarbutton) this.getFellow("label_" + fileFieldArr[i]);
				tb.setLabel(s.substring(s.lastIndexOf("/") + 1));
				tb.setContext(s);
				tb.addForward("onClick", this, "onFileDownload", s);
			}
		}
		// manual的多个文件
		String multiFile = application.getQualityManual();
		if (multiFile != null)
		{
			String[] fileArr = multiFile.split("\\|");
			Hbox hbox = (Hbox) getFellow("box_qualityManual");
			for (String filrUrl : fileArr)
			{
				if (!filrUrl.isEmpty())
				{
					String fileName = filrUrl.substring(filrUrl.lastIndexOf("/") + 1);
					Toolbarbutton lb = new Toolbarbutton("[" + fileName + "]");
					lb.setId("label_" + fileName);
					lb.setContext(filrUrl);
					lb.setParent(hbox);
					lb.addForward("onClick", this, "onFileDownload", filrUrl);
				}
			}
		}
	}
	public void onSave()
	{
		appStatusRecord.setApproveUser(UserInfoServiceUtil.getCurrentLoginUser());
		appStatusRecord.setApproveDate(new Date());
		AppStatusRecordInfoServiceUtil.update(appStatusRecord);
	}
	/**
	 * Cancel this screen
	 */
	public void onCancel()
	{
		this.detach();
	}
	public void onSubmit()
	{
		if (taskInstanceId > 0)
		{
			onSave();
			Grid appoveGrid = (Grid) this.getSpaceOwner().getFellow("appoveGrid");
			BpmZkUtil.saveApprove(appoveGrid, taskInstanceId, userId, rowId, entityName);
			List<Row> approveRows = (List<Row>) appoveGrid.getRows().getChildren();
			Row row = approveRows.get(0);
			Listbox resultListbox = (Listbox) row.getFellow("resultListbox");
			String approveResult = null;
			if (resultListbox.getSelectedItem() != null)
				approveResult = resultListbox.getSelectedItem().getLabel();
			int chageType = appStatusRecord.getChangeType();
			Map vs = new HashMap();
			vs.put("changeType", chageType);
			vs.put("appstatus", application.getStauts0());
			// 结束tasktanc
			BpmUtil.endTask(taskInstanceId, approveResult, vs);
			if (approveResult.equals("同意"))
			{
				Long pid = application.getProcessInstanceId();
				TaskInstance ti = BpmUtil.getCurrentTaskInstance(pid);
				if (chageType == 0)
				{
					// 申请状态设为暂停
					BpmUtil.suspendTask(ti.getId());
					application.setStauts0(5);
				}
				if (chageType == 1)
				{
					// 申请状态设为撤销
					BpmUtil.endProcessInstance(ti.getId());
					application.setStauts0(4);
					application.setAcceptUser(null);
					application.setReveiwUser(null);
				}
				if (chageType == 2)
				{
					// 申请状态恢复
					BpmUtil.resumeTask(ti.getId());
					application.setStauts0(-1);
				}
				ApplicationInfoServiceUtil.update(application);
			} else
			{
				// 如果已经是暂停状态，不同意则仍为暂停; 否则恢复为原有状态
				if (chageType == 2)
					application.setStauts0(5);
				else
					application.setStauts0(-1);
				ApplicationInfoServiceUtil.update(application);
			}
			try
			{
				this.detach();
				Messagebox.show("数据成功提交!", "操作提示", Messagebox.OK, Messagebox.INFORMATION);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else
		{
			try
			{
				Messagebox.show("任务结点绑定错误!");
			} catch (InterruptedException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	// 实现在线打开
	public void onFileDownload(ForwardEvent event)
			throws FileNotFoundException, UnsupportedEncodingException
	{
		String fileUrl = event.getData().toString();
		String downUrl = ZkFileUtil.getDownUrl(fileUrl) + "&online=true";
		openFileOnlineWindow(downUrl);
	}
	public void openFileOnlineWindow(String fileUrl)
	{
		Window objWindow = (Window) Executions.createComponents("attachment-onlinewindow.zul", null, null);
		try
		{
			Iframe downframe = (Iframe) objWindow.getFellow("downframe");
			if (downframe != null)
				downframe.detach();
			Iframe dframe = new Iframe();
			dframe.setParent(objWindow);
			dframe.setSrc(fileUrl);
			dframe.setId("downframe");
			dframe.setWidth("800px");
			dframe.setHeight("600px");
			try
			{
				objWindow.doModal();
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SuspendNotAllowedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void onOpenModelWindow()
	{
		HashMap params = new HashMap();
		params.put("application", application);
		Window objWindow = (Window) Executions.createComponents("productionmodel-listview.zul", null, params);
		try
		{
			 
			// objWindow
			objWindow.doModal();
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void onOpenPumperWindow()
	{
		HashMap params = new HashMap();
		params.put("application", application);
		Window objWindow = (Window) Executions.createComponents("pumperdocuments-view.zul", null, params);
		try
		{
			objWindow.doModal();
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void onChangeStatusList()
	{
		HashMap params = new HashMap();
		params.put("rowId", application.getId());
		Window objWindow = (Window) Executions.createComponents("appstatusrecord-list.zul", null, params);
		try
		{
			objWindow.doModal();
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
