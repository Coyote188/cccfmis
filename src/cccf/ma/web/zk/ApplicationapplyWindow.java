package cccf.ma.web.zk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import openjframework.model.UserInfo;
import openjframework.service.UserInfoServiceUtil;
import openjframework.util.ZkFileUtil;

import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import cccf.ma.model.ApplicationInfo;
import cccf.ma.model.EnterpriseInfo;
import cccf.ma.model.ManufactureInfo;
import cccf.ma.model.ProductCatalogueInfo;
import cccf.ma.model.ProductionEnterpriseInfo;
import cccf.ma.service.ApplicationInfoServiceUtil;
import cccf.ma.service.EnterpriseInfoServiceUtil;
import cccf.ma.service.ManufactureInfoServiceUtil;
import cccf.ma.service.ProductCatalogueInfoServiceUtil;
import cccf.ma.service.ProductionEnterpriseInfoServiceUtil;
import cccf.myenum.ApplicationStatusUtil;

import com.aidi.bpm.service.BpmUtil;
import com.aidi.bpm.service.ProcessFormServiceUtil;

@SuppressWarnings({ "serial", "unused", "unchecked" })
public class ApplicationapplyWindow extends Window {
	public ApplicationInfo application;
	ManufactureInfo manu;
	ProductionEnterpriseInfo pro;
	Map params = Executions.getCurrent().getArg();
	String userId;
	String processId = "0";
	String rowId;
	String cdir = "", attachfilePath = "";
	Combobox production,apptype,bustype;
	// Combobox productionEnterprise;
	// Combobox manufacture;
	Window Indexwindow;
	Textbox address;
	Button btSubmit;
	Listbox lbxManu;
	Listbox lbxPro;
	Label sioidlab;
	boolean editStatus = false;

	String[] fileFieldArr = { "businessLisence", "organizationCode", "inspectionDeviceList", "layoutGraph", "brand", "qualityManual", "originalCertificate" };

	public void onCreate() {
		Components.wireVariables(this, this);
		production = (Combobox) this.getFellow("production");
		apptype = (Combobox) this.getFellow("apptype");
		bustype = (Combobox) this.getFellow("bustype");
		address = (Textbox) this.getFellow("address");
		application = (ApplicationInfo) this.getPage().getAttribute("inspectionApplication");
		sioidlab = (Label) this.getFellow("sioidlab");
		if (application.getStauts() == 0) {
			String sio = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
			sioidlab.setValue(sio);
			application.setSioid(sio);
			apptype.setSelectedIndex(0);
			bustype.setSelectedIndex(0);
			application.setApptype(apptype.getSelectedItem().getLabel());
			application.setBusinesstype(bustype.getSelectedItem().getLabel());
		}
		address.setValue("不再使用");
		application.setAddress("不再使用");
		editStatus = ApplicationStatusUtil.isEditEnabled(application.getStauts(), application.getStauts0());
		processId = (String) params.get("processId");
		userId = (String) params.get("userId");
		if (processId == null || processId.trim().length() == 0) {
			UserInfo user = UserInfoServiceUtil.getCurrentLoginUser();
			userId = String.valueOf(user.getId());
			// 取得工作流ID
			processId = String.valueOf(ProcessFormServiceUtil.getProcessID("application-apply.zul"));
		}

		// 当前企业
		EnterpriseInfo enterprise = EnterpriseInfoServiceUtil.getCurrentEnterprise();
		application.setEnterprise(enterprise);
		// 查找受理申请已上传文件
		if (enterprise != null && application.getStauts() == 0) {
			List<ApplicationInfo> lista = ApplicationInfoServiceUtil.findByQuery(" from ApplicationInfo where enterpriseid='" + enterprise.getId() + "' order by aid desc");
			if (lista != null && lista.size() > 0) {
				ApplicationInfo firstApp = lista.get(0);
				application.setBusinessLisence(firstApp.getBusinessLisence());
				application.setOrganizationCode(firstApp.getOrganizationCode());
				application.setInspectionDeviceList(firstApp.getInspectionDeviceList());
				application.setLayoutGraph(firstApp.getLayoutGraph());
			}
		}

		// 取时间生成本次上传附件的子目录名

		cdir = sioidlab.getValue();
		// 上传目录
		attachfilePath = "/Product/" + enterprise.getName() + "/application/" + cdir + "/";

		// 是否允许分型号
		// if (application.getProduction() != null) {
		// if (application.getProduction().isModelStatus()) {
		if (application.getStauts() != 0)
			onOpenModelWindow();
		// }
		// }

		setButtonStatus();

		setFileNameLabel();

		setEndedTaskList();

		setContractChoice();

	}

	private void setEndedTaskList() {
		Groupbox gbTasklist = (Groupbox) this.getFellow("gbTasklist");
		if (application.getProcessInstanceId() != null)
			if (application.getProcessInstanceId() > 0)
				gbTasklist.setVisible(true);
			else
				gbTasklist.setVisible(false);
	}

	private void setFileNameLabel() {

		String s1 = application.getBusinessLisence();
		if (s1 != null && !s1.isEmpty()) {
			Toolbarbutton label_businessLisence = (Toolbarbutton) this.getFellow("label_businessLisence");
			label_businessLisence.setLabel(s1.substring(s1.lastIndexOf("/") + 1));
			label_businessLisence.setContext(s1);
			Toolbarbutton del_businessLisence = (Toolbarbutton) this.getFellow("del_businessLisence");
			del_businessLisence.setVisible(editStatus);
			Button up_businessLisence = (Button) this.getFellow("fpd_businessLisence");
			up_businessLisence.setDisabled(true);
		}

		String s2 = application.getOrganizationCode();
		if (s2 != null && !s2.isEmpty()) {
			Toolbarbutton label_organizationCode = (Toolbarbutton) this.getFellow("label_organizationCode");
			label_organizationCode.setLabel(s2.substring(s2.lastIndexOf("/") + 1));
			label_organizationCode.setContext(s2);
			Toolbarbutton del_organizationCode = (Toolbarbutton) this.getFellow("del_organizationCode");
			del_organizationCode.setVisible(editStatus);
			Button up_organizationCode = (Button) this.getFellow("fpd_organizationCode");
			up_organizationCode.setDisabled(true);
		}

		String s3 = application.getInspectionDeviceList();
		if (s3 != null && !s3.isEmpty()) {
			Toolbarbutton label_inspectionDeviceList = (Toolbarbutton) this.getFellow("label_inspectionDeviceList");
			label_inspectionDeviceList.setLabel(s3.substring(s3.lastIndexOf("/") + 1));
			label_inspectionDeviceList.setContext(s3);
			Toolbarbutton del_inspectionDeviceList = (Toolbarbutton) this.getFellow("del_inspectionDeviceList");
			del_inspectionDeviceList.setVisible(editStatus);
			Button up_inspectionDeviceList = (Button) this.getFellow("fpd_inspectionDeviceList");
			up_inspectionDeviceList.setDisabled(true);
		}

		String s4 = application.getLayoutGraph();
		if (s4 != null && !s4.isEmpty()) {
			Toolbarbutton label_layoutGraph = (Toolbarbutton) this.getFellow("label_layoutGraph");
			label_layoutGraph.setLabel(s4.substring(s4.lastIndexOf("/") + 1));
			label_layoutGraph.setContext(s4);
			Toolbarbutton del_layoutGraph = (Toolbarbutton) this.getFellow("del_layoutGraph");
			del_layoutGraph.setVisible(editStatus);
			Button up_layoutGraph = (Button) this.getFellow("fpd_layoutGraph");
			up_layoutGraph.setDisabled(true);
		}

		String s5 = application.getBrand();
		if (s5 != null && !s5.isEmpty()) {
			Toolbarbutton label_brand = (Toolbarbutton) this.getFellow("label_brand");
			label_brand.setLabel(s5.substring(s5.lastIndexOf("/") + 1));
			label_brand.setContext(s5);
			Toolbarbutton del_brand = (Toolbarbutton) this.getFellow("del_brand");
			del_brand.setVisible(editStatus);
			Button up_brand = (Button) this.getFellow("fpd_brand");
			up_brand.setDisabled(true);
		}

		String s6 = application.getOriginalCertificate();
		if (s6 != null && !s6.isEmpty()) {
			Toolbarbutton label_originalCertificate = (Toolbarbutton) this.getFellow("label_originalCertificate");
			label_originalCertificate.setLabel(s6.substring(s6.lastIndexOf("/") + 1));
			label_originalCertificate.setContext(s6);
			Toolbarbutton del_originalCertificate = (Toolbarbutton) this.getFellow("del_originalCertificate");
			del_originalCertificate.setVisible(editStatus);
			Button up_originalCertificate = (Button) this.getFellow("fpd_originalCertificate");
			up_originalCertificate.setDisabled(true);
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
					Toolbarbutton tb = new Toolbarbutton();
					tb.setId("toolbar_" + fileName);
					tb.setImage("../image/common/close.png");
					String[] eventDate = new String[2];
					eventDate[0] = fileName;
					eventDate[1] = "qualityManual";
					tb.addForward("onClick", this, "onDeleteAttachfileList", eventDate);
					tb.setContext(filrUrl);
					tb.setParent(hbox);
					tb.setVisible(editStatus);
				}
			}
		}
	}

	public void onSave() {
		application.setStauts(0);
		application.setAddress("不再使用");
		save();
		try {
			Messagebox.show("数据保存成功!", "操作提示", Messagebox.OK, Messagebox.INFORMATION);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void save() {
		if (validateData() == false)
			return;
		String cmd = "add";
		String pcmd = (String) params.get("cmd");
		if (pcmd != null)
			cmd = pcmd;
		if (cmd.equals("add")) {
			application.setApplyDate(new Date());

		}
		application.setStauts0(-1);
		// 合同条款5
		Radiogroup contractChoice1 = (Radiogroup) getFellow("constractChoice1");
		if (contractChoice1.getSelectedItem() != null) {
			application.setContractChoice1(contractChoice1.getSelectedItem().getLabel());
		} else {
			throw new WrongValueException(contractChoice1, "必须进行选择!");
		}
		Radiogroup constractChoice2 = (Radiogroup) getFellow("constractChoice2");
		if (constractChoice2.getSelectedItem() != null) {
			application.setContractChoice2(constractChoice2.getSelectedItem().getLabel());
		} else {
			throw new WrongValueException(constractChoice2, "必须进行选择!");
		}
		ApplicationInfoServiceUtil.saveOrUpdate(application);
		rowId = application.getId();

	}

	/**
	 * Cancel this screen
	 * 
	 */
	public void onCancel() {
		this.detach();
	}

	public void onproductionSelect(org.zkoss.zk.ui.event.SelectEvent evt) {
		Iterator items = evt.getSelectedItems().iterator();
		while (items.hasNext()) {
			Comboitem item = (Comboitem) items.next();
			String id = item.getValue().toString();
			String qstr = "from ProductCatalogueInfo where id='" + id + "'";
			List list = ProductCatalogueInfoServiceUtil.findByQuery(qstr);
			ProductCatalogueInfo production = new ProductCatalogueInfo();
			if (list.size() > 0)
				production = (ProductCatalogueInfo) list.get(0);
			application.setProduction(production);
			// if (application.getProduction() != null) {
			// if (application.getProduction().isModelStatus()) {
			onOpenModelWindow();
			// }
			// }

		}
	}

	public void onProductionEnterpriseSelect(ForwardEvent evt) {
		SelectEvent event = (SelectEvent) evt.getOrigin();
		Iterator items = event.getSelectedItems().iterator();
		while (items.hasNext()) {
			Listitem item = (Listitem) items.next();
			ProductionEnterpriseInfo productionEnterprise = (ProductionEnterpriseInfo) item.getValue();
			application.setProductionEnterprise(productionEnterprise);

		}
	}

	public void onManufactureSelect(ForwardEvent evt) {
		SelectEvent event = (SelectEvent) evt.getOrigin();
		Iterator items = event.getSelectedItems().iterator();
		while (items.hasNext()) {
			Listitem item = (Listitem) items.next();
			ManufactureInfo manufactureInfo = (ManufactureInfo) item.getValue();
			application.setManufacture(manufactureInfo);

		}
	}

	public void onSubmit() {
		if (null == application.getProduction())
			return;
		if (0 == application.getProduction().getActivateStatus()) {
			try {
				Messagebox.show("该产品为禁用状态,不能提交!", "操作提示", Messagebox.OK, Messagebox.INFORMATION);
				return;
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}

		if (processId != null && processId != "0") {
			application.setStauts(1);
			save();
			Page page = this.getPage();
			// 发起工作流
			Map vs = new HashMap();
			vs.put("rowId", rowId);
			vs.put("entityName", "ApplicationInfo");
			Long pid = BpmUtil.startProcess(Long.parseLong(processId), vs, userId);
			try {
				Messagebox.show("受理申请成功提交，请等待审批!", "操作提示", Messagebox.OK, Messagebox.INFORMATION);
				application.setProcessInstanceId(pid);
				ApplicationInfoServiceUtil.update(application);
				// this.detach(); 发送事件，关闭当前窗口,disable the submit button
				btSubmit.setDisabled(true);
				if (Messagebox.YES == Messagebox.show("是否关闭当前窗口？", "操作提示", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION))
					// Events.postEvent(IndexEnterpriseController.ON_DETACHTAB,Indexwindow,null);
					this.detach();

			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} else {
			try {
				Messagebox.show("工作流未绑定，不能发起工作流!");
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	public void onFileUP(ForwardEvent event) {
		// method modified here
		UploadEvent ent = (UploadEvent) event.getOrigin();

		String fieldName = event.getData().toString();
		// Media media = null;
		Media media = ent.getMedia();
		String fileName;
		try {
			// media = Fileupload.get();
			if (ZkFileUtil.uploadFile(media, attachfilePath)) {
				fileName = media.getName();
				File file = new File(this.getPage().getDesktop().getWebApp().getRealPath(attachfilePath) + "\\" + fileName);
				fileName = UUID.randomUUID().toString() + fileName.substring(fileName.lastIndexOf("."), fileName.length());
				file.renameTo(new File(this.getPage().getDesktop().getWebApp().getRealPath(attachfilePath) + "\\" + fileName));

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
				if (fieldName.equals("businessLisence"))
					application.setBusinessLisence(filrUrl);
				if (fieldName.equals("organizationCode"))
					application.setOrganizationCode(filrUrl);
				if (fieldName.equals("inspectionDeviceList"))
					application.setInspectionDeviceList(filrUrl);
				if (fieldName.equals("layoutGraph"))
					application.setLayoutGraph(filrUrl);
				if (fieldName.equals("brand"))
					application.setBrand(filrUrl);
				if (fieldName.equals("qualityManual"))
					application.setQualityManual(filrUrl);
				if (fieldName.equals("originalCertificate"))
					application.setOriginalCertificate(filrUrl);

				Fileupload upButton = (Fileupload) getFellow("fpd_" + fieldName);
				upButton.setDisabled(true);

				// a nullpointer here "qualityManual"
				Toolbarbutton tb = (Toolbarbutton) getFellow("del_" + fieldName);
				tb.setVisible(true);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void onFileListUP(ForwardEvent event) {
		UploadEvent ent = (UploadEvent) event.getOrigin();
		String fieldName = event.getData().toString();
		Media media = ent.getMedia();
		// Media media = null;
		String fileName;

		try {
			// media = Fileupload.get();
			if (ZkFileUtil.uploadFile(media, attachfilePath)) {
				fileName = media.getName();
				// 文件的真实url
				String filrUrl = attachfilePath + fileName;

				Hbox hbox = (Hbox) getFellow("box_" + fieldName);
				Toolbarbutton lb = new Toolbarbutton("[" + fileName + "]");
				lb.setId("label_" + fileName);
				lb.setContext(filrUrl);
				lb.setParent(hbox);
				lb.addForward("onClick", this, "onFileListDownload", filrUrl);
				Toolbarbutton tb = new Toolbarbutton();
				tb.setId("toolbar_" + fileName);
				tb.setImage("../image/common/close.png");
				String[] eventDate = new String[2];
				eventDate[0] = fileName;
				eventDate[1] = fieldName;
				tb.addForward("onClick", this, "onDeleteAttachfileList", eventDate);
				tb.setContext(filrUrl);
				tb.setParent(hbox);
				if (fieldName.equals("qualityManual")) {
					String str = application.getQualityManual();
					if (str == null)
						str = "";
					application.setQualityManual(str + "|" + filrUrl);
				}

			}

		} catch (Exception e) {
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
			if (lbFileName.getContext().indexOf(attachfilePath) != -1)
				file.delete();

			Fileupload upButton = (Fileupload) getFellow("fpd_" + fieldName);
			upButton.setDisabled(false);

			toolbar.setVisible(false);

			lbFileName.setLabel("");
			lbFileName.setContext("");
			tb.setValue("");

			// 文件的真实url
			String filrUrl = "";

			if (fieldName.equals("businessLisence"))
				application.setBusinessLisence(filrUrl);
			if (fieldName.equals("organizationCode"))
				application.setOrganizationCode(filrUrl);
			if (fieldName.equals("inspectionDeviceList"))
				application.setInspectionDeviceList(filrUrl);
			if (fieldName.equals("layoutGraph"))
				application.setLayoutGraph(filrUrl);
			if (fieldName.equals("brand"))
				application.setBrand(filrUrl);
			if (fieldName.equals("qualityManual"))
				application.setQualityManual(filrUrl);
			if (fieldName.equals("originalCertificate"))
				application.setOriginalCertificate(filrUrl);
		}
	}

	// 删除附件
	public void onDeleteAttachfileList(ForwardEvent event) {
		String[] eventDate = (String[]) event.getData();
		String fieldName = eventDate[1];
		String fileName = eventDate[0];

		Toolbarbutton tb = (Toolbarbutton) getFellow("toolbar_" + fileName);
		Toolbarbutton lb = (Toolbarbutton) getFellow("label_" + fileName);
		// 文件的真实url
		String contextPath = Executions.getCurrent().getDesktop().getWebApp().getRealPath(tb.getContext());

		File file = new File(contextPath);
		if (file.isFile()) {
			file.delete();

			tb.detach();
			lb.detach();
			String filrUrl = application.getQualityManual();
			filrUrl = filrUrl.replaceAll(fileName, "");

			if (fieldName.equals("qualityManual")) {
				application.setQualityManual(filrUrl);
			}
		}
	}

	boolean validateData() {

		production.getValue();
		if (lbxManu.getSelectedCount() < 1 || lbxPro.getSelectedCount() < 1) {
			throw new WrongValueException(lbxManu, "请选择生产企业和制造商");
		}
		address.getValue();
		Textbox businessLisence = (Textbox) this.getFellow("businessLisence");
		businessLisence.setConstraint("no empty:营业执照不能为空");
		businessLisence.getValue();

		Textbox organizationCode = (Textbox) this.getFellow("organizationCode");
		organizationCode.setConstraint("no empty:代码证不能为空");
		organizationCode.getValue();

		Textbox inspectionDeviceList = (Textbox) this.getFellow("inspectionDeviceList");
		inspectionDeviceList.setConstraint("no empty:检验设备清单不能为空");
		inspectionDeviceList.getValue();

		Textbox layoutGraph = (Textbox) this.getFellow("layoutGraph");
		layoutGraph.setConstraint("no empty:企业生产配置平面图不能为空");
		layoutGraph.getValue();

		Intbox txtContractYear = (Intbox) getFellow("contractYear");
		txtContractYear.getValue();
		Intbox contractMonth = (Intbox) getFellow("contractMonth");
		contractMonth.getValue();

		return true;

	}

	public void onOpenModelWindow() {
		System.out.println("in apply window!");
		Div modelwin = (Div) getFellow("modelwin");
		if (modelwin.getChildren() != null) {
			modelwin.getChildren().clear();
		}
		HashMap params = new HashMap();
		params.put("application", application);
		// params.put("attachfilePath", attachfilePath);
		Window objWindow = (Window) Executions.createComponents("productionmodel-manage.zul", null, params);
		objWindow.setParent(modelwin);
		objWindow.doEmbedded();

	}

	public void onOpenPumperWindow() {
		HashMap params = new HashMap();
		params.put("application", application);
		params.put("attachfilePath", attachfilePath);
		Window objWindow = (Window) Executions.createComponents("pumperdocuments-edit.zul", null, params);

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
		if (fieldName.equals("contractFileUrl"))
			fileUrl = application.getContractFileUrl();

		String downUrl = ZkFileUtil.getDownUrl(fileUrl);
		Iframe downframe = (Iframe) this.getFellow("downframe");
		if (downframe != null)
			downframe.detach();
		Iframe dframe = new Iframe();
		dframe.setSrc(downUrl);
		dframe.setId("downframe");
		dframe.setParent(this);
		dframe.setWidth("0px");
		dframe.setHeight("0px");

	}

	// 实现下载
	public void onFileListDownload(ForwardEvent event) throws FileNotFoundException, UnsupportedEncodingException {
		String fileUrl = event.getData().toString();

		String downUrl = ZkFileUtil.getDownUrl(fileUrl);

		Iframe downframe = (Iframe) this.getFellow("downframe");
		if (downframe != null)
			downframe.detach();
		Iframe dframe = new Iframe();
		dframe.setSrc(downUrl);
		dframe.setId("downframe");
		dframe.setParent(this);
		dframe.setWidth("0px");
		dframe.setHeight("0px");

	}

	private void setButtonStatus() {

		Button btSave = (Button) getFellow("btSave");
		btSave.setDisabled(!editStatus);
		Button btSubmit = (Button) getFellow("btSubmit");
		btSubmit.setDisabled(!editStatus);
		Toolbarbutton btChangeStatusList = (Toolbarbutton) getFellow("btChangeStatusList");

		Toolbarbutton btChaneStatus = (Toolbarbutton) getFellow("btChangeStatus");
		btChaneStatus.setDisabled(true);
		// 工厂检查之前
		if ((application.getStauts() == 2 || application.getStauts() == 3) && application.getStauts0() == -1 || application.getStauts0() == 5) {
			btChaneStatus.setDisabled(false);
		}

		if (application.getStauts() < 2) {
			btChangeStatusList.setDisabled(true);
		}

		production.setButtonVisible(editStatus);
		bustype.setButtonVisible(editStatus);
		apptype.setButtonVisible(editStatus);
		// productionEnterprise.setButtonVisible(editStatus);

		manu = application.getManufacture();
		pro = application.getProductionEnterprise();
		// lbxManu.setDisabled(editStatus);
		// lbxPro.setDisabled(editStatus);

		address.setReadonly(!editStatus);

		// 合同条款5
		Radio rb1 = (Radio) getFellow("rb1");
		rb1.setDisabled(!editStatus);
		Radio rb2 = (Radio) getFellow("rb2");
		rb2.setDisabled(!editStatus);
		Radio rb3 = (Radio) getFellow("rb3");
		rb3.setDisabled(!editStatus);
		Radio rb4 = (Radio) getFellow("rb4");
		rb4.setDisabled(!editStatus);
		Intbox txtContractYear = (Intbox) getFellow("contractYear");
		txtContractYear.setReadonly(!editStatus);
		Intbox txtContractMonth = (Intbox) getFellow("contractMonth");
		txtContractMonth.setReadonly(!editStatus);

		// 上传按钮状态
		for (String field : fileFieldArr) {
			Button bt = (Button) this.getFellow("fpd_" + field);
			bt.setDisabled(!editStatus);
		}

		// 合同下载
		if (application.getStauts() == 9) {
			Toolbarbutton btContractFileUrl = (Toolbarbutton) getFellow("btContractFileUrl");
			btContractFileUrl.setVisible(true);
			btContractFileUrl.setContext(application.getContractFileUrl());
		}

	}

	// set selected item for two listbox for this page
	public void onSelectADefaultItemForManufacture() {
		lbxManu = (Listbox) this.getFellow("lbxManu");
		List<Listitem> manuItems = lbxManu.getItems();
		if (null != manu)
			for (Listitem item : manuItems) {
				ManufactureInfo m = (ManufactureInfo) item.getValue();
				if (m.getId().equals(manu.getId())) {
					item.setSelected(true);
					item.setStyle("color:red");
					break;
				}
			}
	}

	public void onSelectADefaultItemForProductionEnterprise() {
		lbxPro = (Listbox) this.getFellow("lbxPro");
		List<Listitem> manuItems = lbxPro.getItems();
		if (null != pro)
			for (Listitem item : manuItems) {
				ProductionEnterpriseInfo m = (ProductionEnterpriseInfo) item.getValue();
				if (m.getId().equals(pro.getId())) {
					item.setSelected(true);
					item.setStyle("color:red");
					break;
				}
			}
	}

	public void onChangeStatus() {
		HashMap params = new HashMap();
		params.put("application", application);
		Window objWindow = (Window) Executions.createComponents("appstatusrecord-apply.zul", null, params);

		try {

			objWindow.doModal();
			Label statusName = (Label) this.getFellow("statusName");
			statusName.setValue(application.getStatusName());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	public void setContractChoice() {
		// 合同条款5
		Radio rb1 = (Radio) getFellow("rb1");
		Radio rb2 = (Radio) getFellow("rb2");
		if (application.getContractChoice1() != null) {
			if (application.getContractChoice1().equals("同意"))
				rb1.setChecked(true);
			if (application.getContractChoice1().equals("不同意"))
				rb2.setChecked(true);
		}

		Radio rb3 = (Radio) getFellow("rb3");
		Radio rb4 = (Radio) getFellow("rb4");
		if (application.getContractChoice2() != null) {
			if (application.getContractChoice2().equals("需要"))
				rb3.setChecked(true);
			if (application.getContractChoice2().equals("不需要"))
				rb4.setChecked(true);
		}
	}

	
	public void onSelectItem(SelectEvent evt){
		System.out.println(evt.getTarget().getId());
		if(evt.getTarget().getId().equals("apptype")){
			application.setApptype(apptype.getSelectedItem().getLabel());
		}
		else if(evt.getTarget().getId().equals("bustype")){
			application.setBusinesstype(bustype.getSelectedItem().getLabel());
		}
	}
}
