package cccf.ma.web.zk;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import openjframework.model.UserInfo;
import openjframework.util.ZkFileUtil;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;
import cccf.ma.model.ApplicationInfo;
import cccf.ma.service.AttachFileMemoInfoServiceUtil;
import com.aidi.bpm.service.BpmUtil;
import com.aidi.bpm.zk.BpmZkUtil;

public class ApplicationReviewWindow
		extends Window
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3032773152024683255L;
	public ApplicationInfo		application;
	Map							params				= Executions.getCurrent().getArg();
	String						userId;
	String						processId			= "0";
	String						rowId;
	String						cdir				= "", attachfilePath = "";
	Long						taskInstanceId;
	String						entityName			= "ApplicationInfo";
	String[]					counterSignUsers;
	public void onCreate()
	{
		application = (ApplicationInfo) this.getPage().getAttribute("inspectionApplication");
		rowId = application.getId();
		if (params.get("taskInstanceId") != null)
		{
			taskInstanceId = Long.parseLong(params.get("taskInstanceId").toString());
		}
		userId = params.get("userId").toString();
		Grid appoveGrid = (Grid) this.getSpaceOwner().getFellow("appoveGrid");
		BpmZkUtil.generateApproveGrid(appoveGrid, taskInstanceId);
		setFileNameLabel();
		// 是否允许分型号
		// if (application.getProduction() != null) {
		// if (application.getProduction().isModelStatus()) {
		onOpenModelWindow();
		// }
		// }
		// if (application.getProduction() != null) {
		// Button button_pumper = (Button) this.getFellow("button_pumper");
		// if (application.getProduction().isFireEngineStatus()) {
		// button_pumper.setDisabled(false);
		// } else {
		// button_pumper.setDisabled(true);
		// }
		// }
	}
	private void setFileNameLabel()
	{
		String s1 = application.getBusinessLisence();
		if (s1 != null && !s1.isEmpty())
		{
			Toolbarbutton label_businessLisence = (Toolbarbutton) this.getFellow("label_businessLisence");
			label_businessLisence.setLabel(s1.substring(s1.lastIndexOf("/") + 1));
			label_businessLisence.setContext(s1);
		}
		String s2 = application.getOrganizationCode();
		if (s2 != null && !s2.isEmpty())
		{
			Toolbarbutton label_organizationCode = (Toolbarbutton) this.getFellow("label_organizationCode");
			label_organizationCode.setLabel(s2.substring(s2.lastIndexOf("/") + 1));
			label_organizationCode.setContext(s2);
		}
		String s3 = application.getInspectionDeviceList();
		if (s3 != null && !s3.isEmpty())
		{
			Toolbarbutton label_inspectionDeviceList = (Toolbarbutton) this.getFellow("label_inspectionDeviceList");
			label_inspectionDeviceList.setLabel(s3.substring(s3.lastIndexOf("/") + 1));
			label_inspectionDeviceList.setContext(s3);
		}
		String s4 = application.getLayoutGraph();
		if (s4 != null && !s4.isEmpty())
		{
			Toolbarbutton label_layoutGraph = (Toolbarbutton) this.getFellow("label_layoutGraph");
			label_layoutGraph.setLabel(s4.substring(s4.lastIndexOf("/") + 1));
			label_layoutGraph.setContext(s4);
		}
		String s5 = application.getBrand();
		if (s5 != null && !s5.isEmpty())
		{
			Toolbarbutton label_brand = (Toolbarbutton) this.getFellow("label_brand");
			label_brand.setLabel(s5.substring(s5.lastIndexOf("/") + 1));
			label_brand.setContext(s5);
		}
		String s6 = application.getOriginalCertificate();
		if (s6 != null && !s6.isEmpty())
		{
			Toolbarbutton label_originalCertificate = (Toolbarbutton) this.getFellow("label_originalCertificate");
			label_originalCertificate.setLabel(s6.substring(s6.lastIndexOf("/") + 1));
			label_originalCertificate.setContext(s6);
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
					lb.addForward("onClick", this, "onFileListDownload", filrUrl);
				}
			}
		}
	}
	/**
	 * Cancel this screen
	 */
	public void onCancel()
	{
		this.detach();
	}
	public void onNext()
	{
		// 向桌面发送消息以刷新endtasklist
		EventQueues.lookup(userId + "nextListEvent", EventQueues.APPLICATION, true).publish(new Event("onMsgEventQueue", null, ""));
	}
	public void onSave()
	{
		// if (validateData() == false)
		// return;
		//
		// ApplicationInfoServiceUtil.update(application);
		// rowId = application.getId();
		// 保存基本文件的意见
		Grid memoGrid = (Grid) getFellow("baseFileGrid");
		AttachFileMemoInfoServiceUtil.saveAttachFileMemo(memoGrid, application.getId(), taskInstanceId, "0");
		// 保存型号文件的意见
		Groupbox otherFiles = (Groupbox) getFellow("otherFiles");
		if (application.getProduction().isModelStatus())
		{
			Window modelwin = (Window) getFellow("productionmodelmanageWindow");
			if (modelwin != null && false)
			{
				Listbox listbox = (Listbox) modelwin.getFellow("productionModelInfoListbox");
				AttachFileMemoInfoServiceUtil.saveModelAttachFileMemo(listbox, application.getId(), taskInstanceId);
			}
		}
	}
	public void onSubmit()
	{
		if (application.getStauts0() == 7)
		{
			try
			{
				Messagebox.show("企业已提交变更申请该流程处于挂起状态!", "操作提示", Messagebox.OK, Messagebox.INFORMATION);
				return;
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		onSave();
		if (taskInstanceId > 0)
		{
			Grid appoveGrid = (Grid) this.getSpaceOwner().getFellow("appoveGrid");
			BpmZkUtil.saveApprove(appoveGrid, taskInstanceId, userId, rowId, entityName);
			List<Row> approveRows = (List<Row>) appoveGrid.getRows().getChildren();
			Row row = approveRows.get(0);
			Listbox resultListbox = (Listbox) row.getFellow("resultListbox");
			String approveResult = null;
			if (resultListbox.getSelectedItem() != null)
				approveResult = resultListbox.getSelectedItem().getLabel();
			// 结束tasktanc
			if (approveResult.equals("进行会审"))
			{
				Bandbox bdReviewUserList = (Bandbox) this.getFellow("bdReviewUserList");
				if (counterSignUsers == null)
				{
					throw new WrongValueException(bdReviewUserList, "会审人员不能空!");
				} else if (counterSignUsers.length == 0)
				{
					throw new WrongValueException(bdReviewUserList, "会审人员不能空!");
				}
				BpmUtil.endTaskToCountSign(taskInstanceId, approveResult, counterSignUsers);
			} else
			{
				BpmUtil.endTaskPreviousActor(taskInstanceId, approveResult);
			}
			try
			{
				this.detach();
				Messagebox.show("数据成功提交!", "操作提示", Messagebox.OK, Messagebox.INFORMATION);
				// 向桌面发送消息以刷新endtasklist
				EventQueues.lookup(userId + "refreshEndTaskListEvent", EventQueues.APPLICATION, true).publish(new Event("onMsgEventQueue", null, ""));
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
	public void onOpenModelWindow()
	{
		HashMap params = new HashMap();
		params.put("application", application);
		Groupbox otherFiles = (Groupbox) getFellow("otherFiles");
		Window objWindow = (Window) Executions.createComponents("productionmodel-manage.zul", otherFiles, params);
		objWindow.doEmbedded();
	}
	public void onOpenPumperWindow()
	{
		HashMap params = new HashMap();
		params.put("application", application);
		params.put("attachfilePath", attachfilePath);
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
	// 实现下载
	public void onFiledown(ForwardEvent event)
			throws FileNotFoundException, UnsupportedEncodingException
	{
		String fieldName = event.getData().toString();
		// 文件的真实url
		String fileUrl = "";
		if (fieldName.equals("businessLisence"))
			fileUrl = application.getBusinessLisence().toString();
		if (fieldName.equals("organizationCode"))
			fileUrl = application.getOrganizationCode();
		if (fieldName.equals("inspectionDeviceList"))
			fileUrl = application.getInspectionDeviceList();
		if (fieldName.equals("layoutGraph"))
			fileUrl = application.getLayoutGraph();
		if (fieldName.equals("brand"))
			fileUrl = application.getBrand();
		if (fieldName.equals("qualityManual"))
			fileUrl = application.getQualityManual();
		if (fieldName.equals("originalCertificate"))
			fileUrl = application.getOriginalCertificate();
		String downUrl = ZkFileUtil.getDownUrl(fileUrl) + "&online=true";
		openFileOnlineWindow(downUrl);
	}
	// 实现下载
	public void onFileListDownload(ForwardEvent event)
			throws FileNotFoundException, UnsupportedEncodingException
	{
		String fileUrl = event.getData().toString();
		String downUrl = ZkFileUtil.getDownUrl(fileUrl) + "&online=true";;
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
			dframe.setWidth("780px");
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
	public void onResultSelect(org.zkoss.zk.ui.event.SelectEvent evt)
	{
		Listbox resultListbox = (Listbox) this.getFellow("resultListbox");
		Bandbox bdReviewUserList = (Bandbox) this.getFellow("bdReviewUserList");
		String approveResult = null;
		if (resultListbox.getSelectedItem() != null)
			approveResult = resultListbox.getSelectedItem().getLabel();
		if (approveResult.equals("进行会审"))
		{
			bdReviewUserList.setDisabled(false);
		}
	}
	public void onReviewUserSelect()
	{
		Listbox lbxReviewUserList = (Listbox) this.getFellow("lbxReviewUserList");
		Bandbox bdReviewUserList = (Bandbox) this.getFellow("bdReviewUserList");
		Set<Listitem> list = lbxReviewUserList.getSelectedItems();
		String uName = "";
		if (list.size() > 0)
		{
			counterSignUsers = new String[list.size()];
			int i = 0;
			for (Listitem item : list)
			{
				UserInfo user = (UserInfo) item.getValue();
				uName = user.getName() + "," + uName;
				counterSignUsers[i] = user.getId();
				i++;
			}
			bdReviewUserList.setText(uName);
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
	public void onAttachFileMemo(ForwardEvent event)
	{
		HashMap params = new HashMap();
		Toolbarbutton btn = (Toolbarbutton) event.getOrigin().getTarget();
		if (btn.getAttribute("taskInstanceId") != null)
		{
			Long taskInstanceId = Long.valueOf(btn.getAttribute("taskInstanceId").toString());
			params.put("taskInstanceId", taskInstanceId);
		}
		params.put("fieldNameArr", application.getFieldNameArr());
		Window objWindow = (Window) Executions.createComponents("attachfilememo-view.zul", null, params);
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
