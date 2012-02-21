package cccf.ma.web.zk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import openjframework.service.UserInfoServiceUtil;
import openjframework.util.ZkFileUtil;

import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import cccf.ma.model.AccountItemInfo;
import cccf.ma.model.AccountTypeInfo;
import cccf.ma.model.ApplicationInfo;
import cccf.ma.model.ApplicationPublicInfo;
import cccf.ma.model.FeeDetailInfo;
import cccf.ma.pdfutil.ApplicationPdfDataBeanImpl;
import cccf.ma.pdfutil.PdfDataBean;
import cccf.ma.pdfutil.PdfUtil;
import cccf.ma.service.AccountItemInfoServiceUtil;
import cccf.ma.service.AccountTypeInfoServiceUtil;
import cccf.ma.service.ApplicationInfoServiceUtil;
import cccf.ma.service.FeeDetailInfoServiceUtil;

import com.aidi.bpm.service.BpmUtil;
import com.aidi.bpm.zk.BpmZkUtil;
import com.itextpdf.text.DocumentException;

public class ApplicationCasherWindow extends Window {

	public ApplicationInfo application;
	Map params = Executions.getCurrent().getArg();
	String userId;
	String processId = "0";
	String rowId;
	String cdir = "", attachfilePath = "";
	Long taskInstanceId;
	String entityName = "ApplicationInfo";
	String[] counterSignUsers;
	Label amountFee;
	private ApplicationPublicInfo appPublic;
	
	public void onCreate() {
		Div divApplyInfo = (Div) this.getFellow("divApplyInfo");
		amountFee = (Label) getFellow("amountFee");

		application = (ApplicationInfo) this.getPage().getVariable("inspectionApplication");
		rowId = application.getId();

		if (params.get("taskInstanceId") != null) {
			taskInstanceId = Long.parseLong(params.get("taskInstanceId").toString());
		}
		// application.get
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

		// 取时间生成本次上传附件的子目录名
		cdir = String.valueOf((new java.util.Date()).getTime());

		// 上传目录
		attachfilePath = "/attachments/" + UserInfoServiceUtil.getCurrentLoginUser().getId() + "/application/" + cdir + "/";

		generateFeeForm();
		
		appPublic = new ApplicationPublicInfo();
		appPublic.setApplyno(application.getSioid());
		params.put("applPublicInfo", appPublic);
		Window objWin = (Window) Executions.createComponents("/SysForm/contract/contract_info.zul", divApplyInfo, params);
		objWin.doEmbedded();
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

		String s7 = application.getContractFileUrl();
		if (s7 != null && !s7.isEmpty()) {
			Toolbarbutton label_contractFileUrl = (Toolbarbutton) this.getFellow("label_contractFileUrl");
			label_contractFileUrl.setLabel(s7.substring(s7.lastIndexOf("/") + 1));
			label_contractFileUrl.setContext(s7);

			Toolbarbutton del_contractFileUrl = (Toolbarbutton) this.getFellow("del_contractFileUrl");
			del_contractFileUrl.setVisible(false);
			Button up_contractFileUrl = (Button) this.getFellow("up_contractFileUrl");
			up_contractFileUrl.setDisabled(true);
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
		validateData();

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
			BpmUtil.endTaskPreviousActor(taskInstanceId, approveResult);
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
		if (fieldName.equals("contractFileUrl"))
			fileUrl = application.getContractFileUrl();

		String downUrl = ZkFileUtil.getDownUrl(fileUrl) + "&online=true";
		openFileOnlineWindow(downUrl);

	}

	// 实现下载
	public void onFileListDownload(ForwardEvent event) throws FileNotFoundException, UnsupportedEncodingException {
		String fileUrl = event.getData().toString();

		String downUrl = ZkFileUtil.getDownUrl(fileUrl) + "&online=true";
		;
		openFileOnlineWindow(downUrl);

	}

	public void openFileOnlineWindow(String fileUrl) {
		Window objWindow = (Window) Executions.createComponents("attachment-onlinewindow.zul", null, null);
		try {
			Iframe downframe = (Iframe) objWindow.getFellow("downframe");
			if (downframe != null)
				downframe.detach();
			Iframe dframe = new Iframe();
			dframe.setParent(objWindow);
			dframe.setSrc(fileUrl);
			dframe.setId("downframe");
			dframe.setWidth("780px");
			dframe.setHeight("600px");

			try {
				objWindow.doModal();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SuspendNotAllowedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void onFileUP(ForwardEvent event) throws IOException {
		String fieldName = event.getData().toString();
		Media media = null;
		String fileName;
		try {
			media = Fileupload.get();
			if (ZkFileUtil.uploadFile(media, attachfilePath)) {
				fileName = media.getName();
				// 文件的真实url
				String filrUrl = attachfilePath + fileName;
				// 显示文件名的textbox
				Toolbarbutton lbFileName = (Toolbarbutton) this.getFellow("label_" + fieldName);
				lbFileName.setLabel(fileName);
				lbFileName.setContext(filrUrl);
				// lbFileName.setHref("../filedown.jsp?filename="+fileName+"&fileurl="+filrUrl);
				Textbox tbFile = (Textbox) this.getFellow(fieldName);
				tbFile.setValue(filrUrl);

				lbFileName.setContext(filrUrl);
				if (fieldName.equals("contractFileUrl"))
					application.setContractFileUrl(filrUrl);

				Button upButton = (Button) getFellow("up_" + fieldName);
				upButton.setDisabled(true);

				Toolbarbutton tb = (Toolbarbutton) getFellow("del_" + fieldName);
				tb.setVisible(true);
			}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 删除附件
	public void onDeleteAttachfile(ForwardEvent event) {
		String fieldName = event.getData().toString();

		Toolbarbutton toolbar = (Toolbarbutton) this.getFellow("del_" + fieldName);
		Toolbarbutton lbFileName = (Toolbarbutton) this.getFellow("label_" + fieldName);
		Textbox tb = (Textbox) this.getFellow(fieldName);

		String contextPath = Executions.getCurrent().getDesktop().getWebApp().getRealPath(lbFileName.getContext());

		File file = new File(contextPath);
		if (file.isFile()) {
			file.delete();

			Button upButton = (Button) getFellow("up_" + fieldName);
			upButton.setDisabled(false);

			toolbar.setVisible(false);

			lbFileName.setLabel("");
			lbFileName.setContext("");
			tb.setValue("");

			// 文件的真实url
			String filrUrl = "";

			if (fieldName.equals("contractFileUrl"))
				application.setContractFileUrl(filrUrl);
		}
	}

	void validateData() {
		Textbox contractFileUrl = (Textbox) this.getFellow("contractFileUrl");
		contractFileUrl.setConstraint("no empty:合同文件不能为空");
		contractFileUrl.getValue();
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

	// 生成合同
	public void onGenConstractFile() {
		try {
			ApplicationPdfDataBeanImpl appDate = new ApplicationPdfDataBeanImpl(application);
			PdfDataBean pdfDateBean = appDate;

			String contractPath = "/attachments/" + UserInfoServiceUtil.getCurrentLoginUser().getId() + "/application/";
			// 取时间生成本次上传附件的子目录名
			String fname = String.valueOf((new java.util.Date()).getTime());
			// 文件的真实url
			String filrUrl = contractPath + "constact" + fname + ".pdf";

			String templatePDF = Executions.getCurrent().getDesktop().getWebApp().getRealPath("pdffiles/contract_template.pdf");
			String filePath = Executions.getCurrent().getDesktop().getWebApp().getRealPath(filrUrl);
			PdfUtil.getPdfFile(templatePDF, filePath, pdfDateBean);

			String fieldName = "contractFileUrl";
			// 显示文件名的textbox
			Toolbarbutton lbFileName = (Toolbarbutton) this.getFellow("label_" + fieldName);
			lbFileName.setLabel("合同文件");
			lbFileName.setContext(filrUrl);
			Textbox tbFile = (Textbox) this.getFellow(fieldName);
			tbFile.setValue(filrUrl);
			lbFileName.setContext(filrUrl);
			if (fieldName.equals("contractFileUrl"))
				application.setContractFileUrl(filrUrl);

			Button upButton = (Button) getFellow("up_" + fieldName);
			upButton.setDisabled(true);

			Toolbarbutton tb = (Toolbarbutton) getFellow("del_" + fieldName);
			tb.setVisible(true);

			try {
				Messagebox.show("合同生成成功!", "操作提示", Messagebox.OK, Messagebox.INFORMATION);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void generateFeeForm() {
		Grid feeGrid = (Grid) this.getFellow("feeGrid");
		Rows rows = feeGrid.getRows();
		// 类别
		List<AccountTypeInfo> accountTypeList = AccountTypeInfoServiceUtil.getAll();
		for (AccountTypeInfo accountType : accountTypeList) {

			List<AccountItemInfo> accountItemList = AccountItemInfoServiceUtil.getItemListByType(accountType.getAtypeId());

			if (accountItemList != null)
				for (int i = 0; i < accountItemList.size(); i++) {
					Row rowType = new Row();
					rowType.setParent(rows);
					if (i == 0) {
						Cell cell = new Cell();
						cell.setRowspan(accountItemList.size());
						cell.setParent(rowType);
						Label label1 = new Label(accountType.getName());
						label1.setParent(cell);

						Cell cell2 = new Cell();
						cell2.setParent(rowType);
						Label label2 = new Label(accountItemList.get(i).getName());
						label2.setParent(cell2);

					} else {
						Cell cell = new Cell();
						cell.setParent(rowType);
						Label label1 = new Label(accountItemList.get(i).getName());
						label1.setParent(cell);
					}
					FeeDetailInfo fee = FeeDetailInfoServiceUtil.getApplicationFeeByItemId(accountItemList.get(i).getAccountItemId(), application.getId());

					// 单位
					Cell cellUnit = new Cell();
					cellUnit.setAlign("left");
					cellUnit.setParent(rowType);
					Textbox unitbox = new Textbox();
					unitbox.setWidth("80px");
					unitbox.setId("unitbox_" + accountItemList.get(i).getAccountItemId());
					unitbox.setParent(cellUnit);
					if (this.getId().equals("contractApproveWindow")) {
						unitbox.setReadonly(true);
					}
					unitbox.setReadonly(true);
					unitbox.setValue(accountItemList.get(i).getUnit());

					// 数量
					Cell cellNumber = new Cell();
					cellNumber.setAlign("left");
					cellNumber.setParent(rowType);
					if (accountItemList.get(i).getName().equals("工厂审查费") || accountItemList.get(i).getName().equals("监督费")) {
						Hbox hbox = new Hbox();
						hbox.setParent(cellNumber);
						Label l1 = new Label("规定人日");
						l1.setStyle("vertical-align: middle");
						l1.setParent(hbox);
						Intbox numberbox = new Intbox();
						numberbox.setWidth("40px");
						numberbox.setId("numberbox_" + accountItemList.get(i).getAccountItemId());
						numberbox.setParent(hbox);
						if (this.getId().equals("contractApproveWindow")) {
							numberbox.setReadonly(true);
						}
						numberbox.setValue(fee.getNumber());

						Label l2 = new Label("异地人日");
						l2.setStyle("vertical-align: middle");
						l2.setParent(hbox);
						Intbox numberbox2 = new Intbox();
						numberbox2.setWidth("40px");
						numberbox2.setId("numberbox2_" + accountItemList.get(i).getAccountItemId());
						numberbox2.setParent(hbox);
						if (this.getId().equals("contractApproveWindow")) {
							numberbox2.setReadonly(true);
						}
						numberbox2.setValue(fee.getNumber2());

					} else {
						Intbox numberbox = new Intbox();
						numberbox.setWidth("200px");
						numberbox.setId("numberbox_" + accountItemList.get(i).getAccountItemId());
						numberbox.setParent(cellNumber);
						numberbox.setReadonly(true);

						numberbox.setValue(fee.getNumber());
					}

					// 金额
					Cell cell3 = new Cell();
					cell3.setAlign("left");
					cell3.setParent(rowType);
					Decimalbox feebox = new Decimalbox();
					feebox.setWidth("150px");
					feebox.setFormat("#,##0.##");
					feebox.addForward("onChange", this, "onSumFee");
					feebox.setId("feebox_" + accountItemList.get(i).getAccountItemId());
					feebox.setParent(cell3);
					feebox.setReadonly(true);
					feebox.setValue(BigDecimal.valueOf(fee.getSum()));

				}

		}

	}

	public void onNext() {
		// 向桌面发送消息以刷新endtasklist
		EventQueues.lookup(userId + "nextListEvent", EventQueues.APPLICATION, true).publish(new Event("onMsgEventQueue", null, ""));
	}

}
