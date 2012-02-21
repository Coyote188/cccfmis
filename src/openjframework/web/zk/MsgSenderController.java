package openjframework.web.zk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import openjframework.model.MessageInfo;
import openjframework.model.NoticeInfo;
import openjframework.model.UserInfo;
import openjframework.myenum.NoticeRating;
import openjframework.myenum.NoticeType;
import openjframework.service.NoticeInfoServiceUtil;
import openjframework.service.UserInfoServiceUtil;
import openjframework.util.ZkFileUtil;

import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import cccf.ma.model.EnterpriseInfo;
import cccf.ma.service.EnterpriseNoticeInfoServiceUtil;
import cccf.ma.web.zk.InputValidateUtil;

@SuppressWarnings( { "unchecked", "unused", "serial" })
public class MsgSenderController extends GenericForwardComposer {

	public static final String ON_RELOAD_E_LIST = "onReload";

	private List<EnterpriseInfo> enterpriseList;
	private NoticeInfo msg;
	private Map<String, Media> fileMap;
	private List<String> fileList;
	private List<String> noticeType;
	private List<String> noticeRating;

	private Listbox lbxEtpList;
	private Div divSearchEngine;
	private Textbox tbxSubject, tbxMsgContent;
	private Bandbox bdEList;
	private Toolbarbutton btnAddAttch;
	private Media media;
	private Grid grdFileList;
//	private Combobox cbxMsgRating, cbxMsgType;
	private Window frmNoticeEditer,Indexwindow;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		msg = new NoticeInfo();
		fileList = new ArrayList<String>();
		fileMap = new HashMap<String, Media>();
//		init();
	}

	public void onClick$btnAddEnterprise() {
		Window objWindow = (Window) Executions.createComponents(
				"/SysForm/EnterpriseNotice/msg-e-search-engine.zul",
				divSearchEngine, null);
		
		try {
			objWindow.doModal();
		} catch (SuspendNotAllowedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

//	private void bindingData() {
//		msg.setType(NoticeType.valueOf(
//				cbxMsgType.getSelectedItem().getLabel().toString()).ordinal());
//		msg
//				.setRating(NoticeRating.valueOf(
//						cbxMsgRating.getSelectedItem().getLabel().toString())
//						.ordinal());
//	}

	public void onClick$btnMsgSend() {
		validateData();
//		bindingData();
		try {
			String filePath = "/attachments/"
					+ UserInfoServiceUtil.getCurrentLoginUser().getId()
					+ "/application/"
					+ String.valueOf((new java.util.Date()).getTime()) + "/";
			if (Messagebox.YES == Messagebox.show("是否确认发送："
					+ tbxSubject.getText() + "，该信息将发送给："
					+ enterpriseList.size() + " 家企业!", "操作提示", Messagebox.YES
					| Messagebox.NO, Messagebox.QUESTION)) {
				Set<UserInfo> eSet = new HashSet<UserInfo>();
				for (EnterpriseInfo enter : enterpriseList) {
					eSet.add(enter.getAccount());
				}
				msg.setSendUser(UserInfoServiceUtil.getCurrentLoginUser());
				msg.setSendDate(new Date());
				msg.setAttachment(filePath);
				NoticeInfoServiceUtil.create(msg);
				for (EnterpriseInfo enterprise : enterpriseList) {
					EnterpriseNoticeInfoServiceUtil.sendMsg(enterprise, msg);
				}
				for (Iterator it = fileMap.entrySet().iterator(); it.hasNext();) {
					Map.Entry entry = (Map.Entry) it.next();
					Media media = (Media) entry.getValue();
					try {
						ZkFileUtil.uploadFileWithoutMsg(media, filePath, "",
								"upload");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				Messagebox.show("消息成功发送给：" + enterpriseList.size() + "家企业!");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		cleanInfomation();
	}

	public void onUpload$btnAddAttch(UploadEvent event) {
		media = event.getMedia();
		if (fileMap.get(media.getName()) == null) {
			fileMap.put(media.getName(), media);
			generateFileList(media.getName());
		} else {
			try {
				Messagebox.show("您上传的文件已经存在或文件名重复，请修改文件名后再上传!");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public void generateFileList(String fileName) {
		fileList.add(fileName);
		reGenerateGrid();
	}

	private void reGenerateGrid() {
		ListModelList model = new ListModelList(fileList);
		grdFileList.setModel(model);
	}

	public void onRemove(ForwardEvent event) {
		System.out.println("1"
				+ event.getOrigin().getTarget().getAttribute("fileName"));
		String fileName = (String) event.getOrigin().getTarget().getAttribute(
				"fileName");
		Iterator it = fileList.iterator();
		while (it.hasNext()) {
			String str = (String) it.next();
			if (str.equals(fileName)) {
				it.remove();
				fileMap.remove(str);
			}
		}
		reGenerateGrid();
	}

	public void onReload(Event event) {
		enterpriseList = (List<EnterpriseInfo>) event.getData();
		ListModelList model = new ListModelList(enterpriseList);
		lbxEtpList.setModel(model);
	}

//	public void onClick$btnCancel() {
//		cleanInfomation();
//	}
	
	public void onClick$btnClose(){
		//TODO post event to close the tab!
		Events.postEvent(IndexPersonController.ON_DETACHTAB, Indexwindow, null);
	}

	public List<EnterpriseInfo> getEnterpriseList() {
		return enterpriseList;
	}

	public void setEnterpriseList(List<EnterpriseInfo> enterpriseList) {
		this.enterpriseList = enterpriseList;
	}

	public NoticeInfo getMsg() {
		return msg;
	}

	public void setMsg(NoticeInfo msg) {
		this.msg = msg;
	}

	void validateData() {
		tbxSubject.getValue();
		InputValidateUtil.validate(InputValidateUtil.LENGTH_SUBJECT, tbxSubject);
		tbxMsgContent.getValue();
		InputValidateUtil.validate(InputValidateUtil.LENGTH_CONTENT, tbxMsgContent);
		if (lbxEtpList.getItems().isEmpty())
			throw new WrongValueException(bdEList, "请添加接收消息的企业");
//		if (cbxMsgRating.getSelectedItem() == null
//				|| cbxMsgType.getSelectedItem() == null)
//			throw new WrongValueException(cbxMsgType, "请选择消息类型与消息级别");
	}

	public List<String> getFileList() {
		return fileList;
	}

	public void setFileList(List<String> fileList) {
		this.fileList = fileList;
	}

//	private void init() {
//		noticeRating = new ArrayList<String>();
//		noticeType = new ArrayList<String>();
//		for (NoticeType str : NoticeType.values()) {
//			noticeType.add(str.toString());
//		}
//		for (NoticeRating str : NoticeRating.values()) {
//			noticeRating.add(str.toString());
//		}
//		ListModelList rModel = new ListModelList(noticeRating);
//		cbxMsgRating.setModel(rModel);
//		ListModelList tModel = new ListModelList(noticeType);
//		cbxMsgType.setModel(tModel);
//	}

	private void cleanInfomation() {
		msg = new NoticeInfo();
		tbxMsgContent.setConstraint("");
		tbxMsgContent.setText("");
		tbxSubject.setConstraint("");
		tbxSubject.setText("");
		enterpriseList.clear();
		ListModelList model = new ListModelList(enterpriseList);
		lbxEtpList.setModel(model);
		fileList.clear();
		ListModelList modelList = new ListModelList(getFileList());
		grdFileList.setModel(modelList);
	}
}
