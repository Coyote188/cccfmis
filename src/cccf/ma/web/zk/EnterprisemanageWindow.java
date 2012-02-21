package cccf.ma.web.zk;

import java.util.*;
import java.io.IOException;

import openjframework.model.UserInfo;
import openjframework.util.ZkFileUtil;

import org.zkoss.image.Image;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.metainfo.EventHandler;
import org.zkoss.zk.ui.util.Configuration;
import org.zkoss.zul.*;

import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;

import cccf.ma.service.*;
import cccf.ma.model.*;

@SuppressWarnings( { "serial", "unchecked", "deprecation", "unused" })
public class EnterprisemanageWindow extends Window {

	public EnterpriseInfo enterprise;
	public UserInfo user;
	Map params = Executions.getCurrent().getArg();
	String userId;
	String processId = "0";
	String rowId;

	private Row rEName, rEState, rELocation, rECAdd, rECPerson, rETel, rEFax,
			rEPostalcode, rEEadd;
	private Label name, state, location, contactAddress, contactPerson,
			telephoneNum, faxNum, postalcode, emailAddress;
	private Textbox t0, t1, t2, t3, t4, t5, t6, t7, t8, t9;
	private Button btnModify, btnSubmit, btnCancel;
	private Grid grdFileList;
	private Media media;
	private Window enterpriseEditWindow;
	private Grid grdEditEnterpriseInfo;

	public void onCreate() {
		enterprise = EnterpriseInfoServiceUtil.getCurrentEnterprise();
		// enterprise = (EnterpriseInfo)
		// EnterpriseInfoServiceUtil.getAll().get(3);
		user = (UserInfo) this.getPage().getVariable("user");
		rowId = enterprise.getId();
		Components.wireVariables(this, this);
		List<Attachment> fileNames = enterprise.getAttachments();
		if (!fileNames.isEmpty()) {
			ListModel fileListModel = new SimpleListModel(fileNames);
			grdFileList.setRowRenderer(new EnterpriseFileEngine());
			grdFileList.setModel(fileListModel);
		}

	}

	public void onSave() throws InterruptedException {
		int reply = Messagebox.show("是否确认修改这些信息?", "警告", Messagebox.OK
				| Messagebox.NO, Messagebox.QUESTION);
		if (reply == Messagebox.OK) {
			bindData();
			if (validateData()) {
				enterprise.setStatus(0);
				EnterpriseInfoServiceUtil.saveOrUpdate(enterprise);
				rowId = enterprise.getId();
				btnSubmit.setVisible(false);
				btnModify.setVisible(true);
				btnCancel.setVisible(false);
//				onCancel();
			}
		}
	}

	/**
	 * Cancel this screen
	 * 
	 */
//	public void onCancel() {
//		contactPerson.setParent(rECPerson);
//		contactPerson.setValue(t4.getText());
//		t4.detach();
//		telephoneNum.setParent(rETel);
//		telephoneNum.setValue(t5.getText());
//		t5.detach();
//		faxNum.setParent(rEFax);
//		faxNum.setValue(t6.getText());
//		t6.detach();
//		postalcode.setParent(rEPostalcode);
//		postalcode.setValue(t7.getText());
//		t7.detach();
//	}

	public void onDetach() {
		this.detach();
	}

	private void bindData() {
		// enterprise.setName(t0.getValue());
		// enterprise.setState(t1.getValue());
		// enterprise.setLocation(t2.getValue());
		// enterprise.setContactAddress(t3.getValue());
		enterprise.setContactPerson(t4.getValue());
		enterprise.setTelephoneNum(t5.getValue());
		enterprise.setFaxNum(t6.getValue());
		enterprise.setPostalcode(t7.getValue());
		// enterprise.setEmailAddress(t8.getValue());
	}

	public void onModify() {
		Map params = new HashMap();
		params.put("enterprise", enterprise);
		Window objWindow = (Window) Executions.createComponents(
				"/SysForm/enterprise-info-edit.zul", null, params);
		try {
			objWindow.doModal();
		} catch (SuspendNotAllowedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// name.detach();
		// t0 = new Textbox(name.getValue());t0.setParent(rEName);
		// state.detach();
		// t1 = new Textbox(state.getValue());t1.setParent(rEState);
		// location.detach();
		// t2 = new Textbox(location.getValue());t2.setParent(rELocation);
		// contactAddress.detach();
		// t3 = new Textbox(contactAddress.getValue());t3.setParent(rECAdd);

		// contactPerson.detach();
		// t4 = new Textbox(contactPerson.getValue());
		// t4.setConstraint("no empty:联系人不能为空");
		// t4.setParent(rECPerson);
		// telephoneNum.detach();
		// t5 = new Textbox(telephoneNum.getValue());
		// t5.setParent(rETel);
		// t5.setConstraint("no empty:联系电话不能为空");
		// faxNum.detach();
		// t6 = new Textbox(faxNum.getValue());
		// t6.setParent(rEFax);
		// t6.setConstraint("no empty:传真号码不能为空");
		// postalcode.detach();
		// t7 = new Textbox(postalcode.getValue());
		// t7.setParent(rEPostalcode);
		// t7.setConstraint("no empty:邮编不能为空");

		// emailAddress.detach();
		// t8=new Textbox(emailAddress.getValue());t8.setParent(rEEadd);

		// enterprise = new EnterpriseInfo();
		// enterprise.setId(rowId);

		// btnModify.setVisible(false);
		// btnSubmit.setDisabled(false);
		// btnSubmit.setVisible(true);

	}

	private boolean validateData() {
		boolean isDone = false;
		isDone = t4.getText().equals(null) || t5.getText().equals(null)
				|| t6.getText().equals(null) || t7.getText().equals(null);
		return !isDone;
	}

	private void onViewFileOnline(String path) {
		String downUrl = ZkFileUtil.getFileUrl(enterprise, path)
				+ "&online=true";
		openFileOnlineWindow(downUrl);
	}

	private void onRemove(Attachment fileName) throws InterruptedException {
		int reply = Messagebox.show("是否确认删除 :" + fileName.getName() + "？",
				"提示", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		if (reply == Messagebox.YES) {
			fileName.remove();
			List atts = enterprise.getAttachments();
			Iterator it = atts.iterator();
			while (it.hasNext()) {
				Attachment at = (Attachment) it.next();
				if (at.getId().equals(fileName.getId()))
					it.remove();
			}
			ListModel model = new SimpleListModel(enterprise.getAttachments());
			grdFileList.setModel(model);
		}
	}

	private void onReplace(Attachment att) throws InterruptedException {

		// String realPath = enterprise.getCopyOfBusinessLicense();

		int reply = Messagebox.show("是否确认替换 ：" + att.getName() + " 文件？", "提示",
				Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		if (reply == Messagebox.YES) {
			try {

				media = Fileupload.get();
				if (null != media) {
					att.removeFile();
					att.setAttfile(media);
					att.updata();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// List<String> fileNames =
		// ZkFileUtil.getFileNameList(enterprise.getCopyOfBusinessLicense());
		// ListModel fileListModel = new SimpleListModel(fileNames);
		// grdFileList.setModel(fileListModel);
	}

	private class EnterpriseFileEngine implements RowRenderer {
		@Override
		public void render(Row row, Object data) throws Exception {
			row.setSpans("7,3");
			final Attachment fileName = (Attachment) data;
			final Toolbarbutton btnFile = new Toolbarbutton(fileName.getName());
			btnFile.addEventListener(Events.ON_CLICK, new EventListener() {
				@Override
				public void onEvent(Event arg0) throws Exception {
					onViewFileOnline(fileName.getPath());
				}
			});
			btnFile.setParent(row);
			final Toolbarbutton btnRemove = new Toolbarbutton("删除");
			btnRemove.addEventListener(Events.ON_CLICK, new EventListener() {
				@Override
				public void onEvent(Event arg0) throws Exception {
					onRemove(fileName);
				}
			});
			// btnRemove.setParent(row);
			final Toolbarbutton btnReplace = new Toolbarbutton("修改");
			btnReplace.addEventListener(Events.ON_CLICK, new EventListener() {
				@Override
				public void onEvent(Event arg0) throws Exception {
					onReplace(fileName);
				}
			});
			// btnReplace.addForward("onClick",
			// enterpriseEditWindow,"onUpload",fileName);
			// btnReplace.setParent(row);

			Hbox box = new Hbox();
			box.appendChild(btnRemove);
			box.appendChild(btnReplace);
			row.appendChild(box);
		}

	}

	public void onAttachUpload(ForwardEvent event) {
		UploadEvent ent = (UploadEvent) event.getOrigin();
		Media attFile = ent.getMedia();
		String path = enterprise.getAttachments().get(0).getFilePath();
		Attachment at = new Attachment(path, attFile.getName(), attFile);
		at.persist();
		enterprise.getAttachments().add(at);
		EnterpriseInfoServiceUtil.update(enterprise);
		ListModel model = new SimpleListModel(enterprise.getAttachments());
		grdFileList.setModel(model);
	}

	public void openFileOnlineWindow(String fileUrl) {
		Window objWindow = (Window) Executions.createComponents(
				"attachment-onlinewindow.zul", null, null);
		try {
			Iframe downframe = (Iframe) objWindow.getFellow("downframe");
			if (downframe != null)
				downframe.detach();
			Iframe dframe = new Iframe();
			dframe.setParent(objWindow);
			dframe.setSrc(fileUrl);
			dframe.setId("downframe");
			dframe.setWidth("800px");
			dframe.setHeight("600px");
			System.out.println("out:" + fileUrl);
			try {
				objWindow.doModal();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (SuspendNotAllowedException e) {
			e.printStackTrace();
		}

	}
}
