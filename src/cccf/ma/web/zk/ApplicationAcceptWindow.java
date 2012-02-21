package cccf.ma.web.zk;

import java.util.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import openjframework.service.*;
import openjframework.util.ZkFileUtil;
import openjframework.model.*;

import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.*;
import org.zkoss.zul.*;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.InputEvent;

import com.aidi.bpm.model.Approve;
import com.aidi.bpm.service.*;
import com.aidi.bpm.zk.BpmZkUtil;
import com.aidi.core.spring.*;
import com.aidi.core.zk.*;
import cccf.ma.service.*;
import cccf.ma.model.*;

public class ApplicationAcceptWindow extends Window {

	public ApplicationInfo application;
	Map params = Executions.getCurrent().getArg();
	String userId;
	String processId = "0";
	String rowId;
	String cdir = "", attachfilePath = "";
	Long taskInstanceId;
	String entityName = "ApplicationInfo";

	public void onCreate() {
		application = (ApplicationInfo) this.getPage().getVariable("inspectionApplication");
		if (params.get("taskInstanceId") != null) {
			taskInstanceId = Long.parseLong(params.get("taskInstanceId").toString());
		}
		userId = params.get("userId").toString();
		Grid appoveGrid = (Grid) this.getSpaceOwner().getFellow("appoveGrid");
		BpmZkUtil.generateApproveGrid(appoveGrid, taskInstanceId);
		setFileNameLabel();
		onOpenModelWindow();
	}

	private void setFileNameLabel() {

		String s1 = application.getBusinessLisence();
		if (s1 != null && !s1.isEmpty()) {
			Toolbarbutton label_businessLisence = (Toolbarbutton) this.getFellow("label_businessLisence");
			label_businessLisence.setLabel(s1.substring(s1.lastIndexOf("/") + 1));
			label_businessLisence.setContext(s1);
		}

		String s2 = application.getOrganizationCode();
		if (s2 != null && !s2.isEmpty()) {
			Toolbarbutton label_organizationCode = (Toolbarbutton) this.getFellow("label_organizationCode");
			label_organizationCode.setLabel(s2.substring(s2.lastIndexOf("/") + 1));
			label_organizationCode.setContext(s2);
		}

		String s3 = application.getInspectionDeviceList();
		if (s3 != null && !s3.isEmpty()) {
			Toolbarbutton label_inspectionDeviceList = (Toolbarbutton) this.getFellow("label_inspectionDeviceList");
			label_inspectionDeviceList.setLabel(s3.substring(s3.lastIndexOf("/") + 1));
			label_inspectionDeviceList.setContext(s3);
		}

		String s4 = application.getLayoutGraph();
		if (s4 != null && !s4.isEmpty()) {
			Toolbarbutton label_layoutGraph = (Toolbarbutton) this.getFellow("label_layoutGraph");
			label_layoutGraph.setLabel(s4.substring(s4.lastIndexOf("/") + 1));
			label_layoutGraph.setContext(s4);
		}

		String s5 = application.getBrand();
		if (s5 != null && !s5.isEmpty()) {
			Toolbarbutton label_brand = (Toolbarbutton) this.getFellow("label_brand");
			label_brand.setLabel(s5.substring(s5.lastIndexOf("/") + 1));
			label_brand.setContext(s5);
		}

		String s6 = application.getOriginalCertificate();
		if (s6 != null && !s6.isEmpty()) {
			Toolbarbutton label_originalCertificate = (Toolbarbutton) this.getFellow("label_originalCertificate");
			label_originalCertificate.setLabel(s6.substring(s6.lastIndexOf("/") + 1));
			label_originalCertificate.setContext(s6);
		}

		// manual的多个文件
		String multiFile = application.getQualityManual();
		if (multiFile != null) {
			String[] fileArr = multiFile.split("\\|");
			Hbox hbox = (Hbox) getFellow("box_qualityManual");
			for (String filrUrl : fileArr) {
				if (!filrUrl.isEmpty()) {
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
	 * 
	 */
	public void onCancel() {
		this.detach();
	}

	public void onSave() {
		if (validateData() == false)
			return;
		application.setAcceptUser(UserInfoServiceUtil.getCurrentLoginUser());
		ApplicationInfoServiceUtil.update(application);
		rowId = application.getId();

	}

	public void onSubmit() {
		if (application.getStauts0() == 7) {
			try {
				Messagebox.show("企业已提交变更申请该流程处于挂起状态!", "操作提示", Messagebox.OK, Messagebox.INFORMATION);
				return;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		onSave();
		if (taskInstanceId > 0) {
			Grid appoveGrid = (Grid) this.getSpaceOwner().getFellow("appoveGrid");

			BpmZkUtil.saveApprove(appoveGrid, taskInstanceId, userId, rowId, entityName);

			List<Row> approveRows = (List<Row>) appoveGrid.getRows().getChildren();
			Row row = approveRows.get(0);
			Listbox resultListbox = (Listbox) row.getFellow("resultListbox");
			String approveResult = null;
			if (resultListbox.getSelectedItem() != null)
				approveResult = resultListbox.getSelectedItem().getLabel();
			// 结束tasktanc
			BpmUtil.endTaskPreviousActor(taskInstanceId, approveResult, application.getReveiwUser().getId());

			try {
				this.detach();
				Messagebox.show("数据成功提交!", "操作提示", Messagebox.OK, Messagebox.INFORMATION);

				// 向桌面发送消息以刷新endtasklist
				EventQueues.lookup(userId + "refreshEndTaskListEvent", EventQueues.APPLICATION, true).publish(new Event("onMsgEventQueue", null, ""));

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				Messagebox.show("任务结点绑定错误!");
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public void onOpenModelWindow() {
		HashMap params = new HashMap();
		params.put("application", application);
		Groupbox otherFiles = (Groupbox) getFellow("otherFiles");
		Window objWindow = (Window) Executions.createComponents("productionmodel-manage.zul", otherFiles, params);
		objWindow.doEmbedded();
	}

	public void onOpenPumperWindow() {
		HashMap params = new HashMap();
		params.put("application", application);
		params.put("attachfilePath", attachfilePath);
		Window objWindow = (Window) Executions.createComponents("pumperdocuments-view.zul", null, params);

		try {

			objWindow.doModal();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 实现下载
	public void onFiledown(ForwardEvent event) throws FileNotFoundException, UnsupportedEncodingException {
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

		String downUrl = ZkFileUtil.getDownUrl(fileUrl);

		Iframe downframe = (Iframe) this.getFellow("downframe");
		downframe.setSrc(downUrl);

	}

	// 实现下载
	public void onFileListDownload(ForwardEvent event) throws FileNotFoundException, UnsupportedEncodingException {
		String fileUrl = event.getData().toString();

		String downUrl = ZkFileUtil.getDownUrl(fileUrl);
		Iframe downframe = (Iframe) this.getFellow("downframe");
		downframe.setSrc(downUrl);

	}

	public void onReveiwUserSelect(org.zkoss.zk.ui.event.SelectEvent evt) {
		Iterator items = evt.getSelectedItems().iterator();
		while (items.hasNext()) {
			Comboitem item = (Comboitem) items.next();
			String id = item.getValue().toString();
			String qstr = "from UserInfo where id='" + id + "'";
			List list = UserInfoServiceUtil.findByQuery(qstr);
			UserInfo user = new UserInfo();
			if (list.size() > 0)
				user = (UserInfo) list.get(0);
			application.setReveiwUser(user);

		}
	}

	boolean validateData() {

		Combobox reveiwUser = (Combobox) this.getFellow("reveiwUser");
		reveiwUser.getValue();

		return true;
	}

	public void onChangeStatusList() {
		HashMap params = new HashMap();
		params.put("rowId", application.getId());
		Window objWindow = (Window) Executions.createComponents("appstatusrecord-list.zul", null, params);
		try {

			objWindow.doModal();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
