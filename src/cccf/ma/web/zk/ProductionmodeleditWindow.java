package cccf.ma.web.zk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import openjframework.model.UserInfo;
import openjframework.service.UserInfoServiceUtil;
import openjframework.util.ZkFileUtil;

import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import cccf.ma.model.ApplicationInfo;
import cccf.ma.model.EnterpriseProductModel;
import cccf.ma.model.ProductionModelInfo;
import cccf.ma.service.EnterpriseInfoServiceUtil;
import cccf.ma.service.EnterpriseProductModelServiceUtil;

public class ProductionmodeleditWindow extends Window {

	public ProductionModelInfo productionModel;
	
	private List<ProductionModelInfo> models;
	private Combobox cbxModelList;
	private List<EnterpriseProductModel> epmList;
	
	Map params = Executions.getCurrent().getArg();
	String userId;
	String processId = "0";
	String rowId;
	String attachfilePath = params.get("attachfilePath").toString();
	ApplicationInfo application;
	Window productionmodelmanageWindow;
	private Checkbox cbxIsMain;
	public void onCreate() {
		Components.wireVariables(this, this);
		application = (ApplicationInfo) params.get("application");
		productionModel = (ProductionModelInfo) this.getPage().getVariable("productionModel");
		setEpmList(EnterpriseProductModelServiceUtil.findByProductEnterprise(application.getProduction(), EnterpriseInfoServiceUtil.getCurrentEnterprise()));		
		ListModelList model = new ListModelList(getEpmList());
		cbxModelList.setModel(model);
		
		//是消防车则增加项
		Grid modelgrid=(Grid)this.getFellow("modelgrid");
		Row r=(Row)modelgrid.getFellow("row_pumper");
		r.setVisible(false);

		setFileNameLabel();

	}
	
	public void onSelectCbxModelList(ForwardEvent event){
		SelectEvent evt = (SelectEvent) event.getOrigin();
		EnterpriseProductModel emp = (EnterpriseProductModel) cbxModelList.getSelectedItem().getAttribute("model");
		productionModel.setModel(emp.getModel());
	}
	
	public void onCbxIsMainCheck(ForwardEvent event){
		CheckEvent evt = (CheckEvent) event.getOrigin();
		Checkbox cbxIsMain = (Checkbox) evt.getTarget();
		boolean isExist = false;
		if(cbxIsMain.isChecked() && !application.getModels().isEmpty()){
			for(ProductionModelInfo model : application.getModels()){
				if(model.getIsMain() == true){
					cbxIsMain.setChecked(false);
					throw new WrongValueException(cbxIsMain,"该产品已经添加主型文件");
				}
			}
		}
		
	}

	private void setFileNameLabel() {
		
		cbxIsMain.setChecked(productionModel.getIsMain());
		
		String s1 = productionModel.getFullReport();
		if (s1 != null && !s1.isEmpty() && false) {
			Toolbarbutton label_fullReport = (Toolbarbutton) this.getFellow("label_fullReport");
			label_fullReport.setContext(productionModel.getFullReport());
			label_fullReport.setLabel(s1.substring(s1.lastIndexOf("/") + 1));
			Toolbarbutton del_fullReport=(Toolbarbutton)this.getFellow("del_fullReport");
			del_fullReport.setVisible(true);
			Button up_fullReport=(Button)this.getFellow("up_fullReport");
			up_fullReport.setDisabled(true);
		}
		
		String s2 = productionModel.getReport();
		if (s2 != null && !s2.isEmpty()) {
			Toolbarbutton label_report = (Toolbarbutton) this.getFellow("label_report");
			label_report.setContext(productionModel.getReport());
			label_report.setLabel(s2.substring(s2.lastIndexOf("/") + 1));
			Toolbarbutton del_report=(Toolbarbutton)this.getFellow("del_report");
			del_report.setVisible(true);
			Button up_report=(Button)this.getFellow("up_report");
			up_report.setDisabled(true);
		}
		
		String s3 = productionModel.getCharacterForm();
		if (s3 != null && !s3.isEmpty()) {
			Toolbarbutton label_characterForm = (Toolbarbutton) this.getFellow("label_characterForm");
			label_characterForm.setContext(productionModel.getCharacterForm());
			label_characterForm.setLabel(s3.substring(s3.lastIndexOf("/") + 1));
			Toolbarbutton del_characterForm=(Toolbarbutton)this.getFellow("del_characterForm");
			del_characterForm.setVisible(true);
			Button up_characterForm=(Button)this.getFellow("up_characterForm");
			up_characterForm.setDisabled(true);
		}
		
		String s4 = productionModel.getFlowChart();
		if (s4 != null && !s4.isEmpty()) {
			Toolbarbutton label_flowChart = (Toolbarbutton) this.getFellow("label_flowChart");
			label_flowChart.setContext(productionModel.getFlowChart());
			label_flowChart.setLabel(s4.substring(s4.lastIndexOf("/") + 1));
			Toolbarbutton del_flowChar=(Toolbarbutton)this.getFellow("del_flowChart");
			del_flowChar.setVisible(true);
			Button up_flowChar=(Button)this.getFellow("up_flowChart");
			up_flowChar.setDisabled(true);
		}
		
		String s5 = productionModel.getPumperCcc();
		if (s5 != null && !s5.isEmpty()) {
			Toolbarbutton label_pumperCcc = (Toolbarbutton) this.getFellow("label_pumperCcc");
			label_pumperCcc.setContext(s5);
			label_pumperCcc.setLabel(s5.substring(s5.lastIndexOf("/") + 1));
			Toolbarbutton del_pumperCcc=(Toolbarbutton)this.getFellow("del_pumperCcc");
			del_pumperCcc.setVisible(true);
			Button up_pumperCcc=(Button)this.getFellow("up_pumperCcc");
			up_pumperCcc.setDisabled(true);
		}
		
		//multiple files
		String multiFile = productionModel.getProductImg();
		if (multiFile != null) {
			String[] fileArr = multiFile.split("\\|");
			Hbox hbox = (Hbox) getFellow("box_productImg");
			for (String filrUrl : fileArr) {
				if (!filrUrl.isEmpty()) {
					String fileName = filrUrl.substring(filrUrl
							.lastIndexOf("/") + 1);
					Toolbarbutton lb = new Toolbarbutton("[" + fileName + "]");
					lb.setId("label_" + fileName);
					lb.setContext(filrUrl);
					lb.setParent(hbox);
					lb.addForward("onClick", this, "onFileListDownload",
							filrUrl);
					Toolbarbutton tb = new Toolbarbutton();
					tb.setId("toolbar_" + fileName);
					tb.setImage("../image/common/close.png");
					String[] eventDate = new String[2];
					eventDate[0] = fileName;
					eventDate[1] = "productImg";
					tb.addForward("onClick", this, "onDeleteAttachfileList",
							eventDate);
					tb.setContext(filrUrl);
					tb.setParent(hbox);
					tb.setVisible(true);
				}
			}
		}

	}

	public void onSave() {
		validateData();
		productionModel.setIsMain(cbxIsMain.isChecked());
		String cmd = "add";
		String pcmd = (String) params.get("cmd");
		if (pcmd != null)
			cmd = pcmd;
		if (application.getProductionModel() == null) {
			application.setProductionModel(new HashSet());
		}
		if (cmd.equals("add")){
			application.getProductionModel().add(productionModel);
		}else {

		}
//		Events.postEvent(ProductionmodelmanageWindow.onInitProductModelMultiple,productionmodelmanageWindow,productionModel);
		this.detach();
	}

	/**
	 * Cancel this screen
	 * 
	 */
	public void onCancel() {
		this.detach();
	}

	public void onFileUP(ForwardEvent event) {
		String fieldName = event.getData().toString();
		UploadEvent ent = (UploadEvent) event.getOrigin();
//		Media media = null;
		Media media = ent.getMedia();
		String fileName;
		UserInfo user = UserInfoServiceUtil.getCurrentLoginUser();
		try {
//			media = Fileupload.get();
			if (ZkFileUtil.uploadFile(media, attachfilePath)) {
				fileName = media.getName();
				// 文件的真实url
				String filrUrl = attachfilePath+ fileName;
				
				// 显示文件名的textbox
				Toolbarbutton lbFileName = (Toolbarbutton) this.getFellow("label_" + fieldName);
				lbFileName.setLabel(fileName);
				lbFileName.setContext(filrUrl);
				Textbox tbFile = (Textbox) this.getFellow(fieldName);
				tbFile.setValue(filrUrl);
				
				if (fieldName.equals("fullReport"))
					productionModel.setFullReport(filrUrl);
				if (fieldName.equals("report"))
					productionModel.setReport(filrUrl);
				if (fieldName.equals("flowChart"))
					productionModel.setFlowChart(filrUrl);
				if (fieldName.equals("characterForm"))
					productionModel.setCharacterForm(filrUrl);
				if (fieldName.equals("pumperCcc"))
					productionModel.setPumperCcc(filrUrl);
				

				Fileupload upButton = (Fileupload) getFellow("up_" + fieldName);
				upButton.setDisabled(true);

				Toolbarbutton tb = (Toolbarbutton) getFellow("del_" + fieldName);
				tb.setVisible(true);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	// upload multiple files
	public void onFileListUP(ForwardEvent event) {
		UploadEvent ent = (UploadEvent) event.getOrigin();
		String fieldName = event.getData().toString();
		Media media = ent.getMedia();
		String attachfilePath = "";
		String fileName;

		try {
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
				tb.addForward("onClick", this, "onDeleteAttachfileList",
						eventDate);
				tb.setContext(filrUrl);
				tb.setParent(hbox);
				if (fieldName.equals("productImg")) {
					String str = productionModel.getProductImg();
					if (str == null)
						str = "";
					productionModel.setProductImg(str + "|" + filrUrl);
				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	// remove the file from list
	// 删除附件
	public void onDeleteAttachfileList(ForwardEvent event) {
		String[] eventDate = (String[]) event.getData();
		String fieldName = eventDate[1];
		String fileName = eventDate[0];

		Toolbarbutton tb = (Toolbarbutton) getFellow("toolbar_" + fileName);
		Toolbarbutton lb = (Toolbarbutton) getFellow("label_" + fileName);
		// 文件的真实url
		String contextPath = Executions.getCurrent().getDesktop().getWebApp()
				.getRealPath(tb.getContext());

		File file = new File(contextPath);
		if (file.isFile()) {
			file.delete();

			tb.detach();
			lb.detach();
			String filrUrl = productionModel.getProductImg();
			filrUrl = filrUrl.replaceAll(fileName, "");

			if (fieldName.equals("productImg")) {
				productionModel.setProductImg(filrUrl);
			}
		}
	}

	// 删除附件
	public void onDeleteAttachfile(ForwardEvent event) {
		String fieldName = event.getData().toString();

		Toolbarbutton toolbar = (Toolbarbutton) this.getFellow("del_"
				+ fieldName);
		Toolbarbutton lbFileName = (Toolbarbutton) this.getFellow("label_" + fieldName);
		Textbox tb = (Textbox) this.getFellow(fieldName);

		String contextPath = Executions.getCurrent().getDesktop().getWebApp()
				.getRealPath(lbFileName.getContext());

		File file = new File(contextPath);
		if (file.isFile()) {
			file.delete();

			Fileupload upButton = (Fileupload) getFellow("up_" + fieldName);
			upButton.setDisabled(false);

			toolbar.setVisible(false);
			lbFileName.setLabel("");
			lbFileName.setContext("");
			tb.setValue("");

			// 文件的真实url
			String filrUrl = "";
			if (fieldName.equals("fullReport"))
				productionModel.setFullReport(filrUrl);
			if (fieldName.equals("report"))
				productionModel.setReport(filrUrl);
			if (fieldName.equals("flowChart"))
				productionModel.setFlowChart(filrUrl);
			if (fieldName.equals("characterForm"))
				productionModel.setCharacterForm(filrUrl);
			if (fieldName.equals("pumperCcc"))
				productionModel.setPumperCcc(filrUrl);
		}
	}

	void validateData() {

		Textbox flowChart = (Textbox) this.getFellow("flowChart");
		flowChart.setConstraint("no empty:产品工艺流程图不能为空");
		flowChart.getValue();
		
		Textbox characterForm = (Textbox) this.getFellow("characterForm");
		characterForm.setConstraint("no empty:关键件或产品特性描述填报表不能为空");
		characterForm.getValue();
		
//		Textbox tbxProductImg = (Textbox) this.getFellow("tbxProductImg");
//		tbxProductImg.setConstraint("no empty:产品图片不能为空");
//		tbxProductImg.getValue();
	}
	
	// 实现下载
	public void onFiledown(ForwardEvent event) throws FileNotFoundException,
			UnsupportedEncodingException {
		String fieldName = event.getData().toString();
		
		Textbox tb = (Textbox) this.getFellow(fieldName);
		
		// 文件的真实url
		String fileUrl = tb.getValue();
		String downUrl = ZkFileUtil.getDownUrl(fileUrl);

		Iframe downframe = (Iframe) this.getFellow("downframe");
		if(downframe!=null) downframe.detach();
		Iframe dframe=new Iframe();
		dframe.setSrc(downUrl);
		dframe.setId("downframe");
		dframe.setParent(this);
		dframe.setWidth("0px");
		dframe.setHeight("0px");

		
	}

	public void setModels(List<ProductionModelInfo> models) {
		this.models = models;
	}

	public List<ProductionModelInfo> getModels() {
		return models;
	}

	public void setEpmList(List<EnterpriseProductModel> epmList) {
		this.epmList = epmList;
	}

	public List<EnterpriseProductModel> getEpmList() {
		return epmList;
	}


}
