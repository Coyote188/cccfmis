package cccf.ma.web.zk;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import openjframework.util.ZkFileUtil;

import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.*;
import org.zkoss.zul.*;
import org.zkoss.zk.ui.event.ForwardEvent;
import cccf.ma.model.*;

public class PumperdocumentseditWindow extends Window {

	public PumperDocumentsInfo pumperDocuments;
	Map params = Executions.getCurrent().getArg();
	ApplicationInfo application;
	String attachfilePath = params.get("attachfilePath").toString();

	public void onCreate() {
		application = (ApplicationInfo) params.get("application");
		pumperDocuments = (PumperDocumentsInfo) this.getPage().getVariable(
				"pumperDocuments");

		setFileNameLabel();

	}

	private void setFileNameLabel() {

		if (pumperDocuments.getBrandCertificate() != null
				&& !pumperDocuments.getBrandCertificate().isEmpty()) {
			Toolbarbutton label_brandCertificate = (Toolbarbutton) this
					.getFellow("label_brandCertificate");
			label_brandCertificate.setContext(pumperDocuments
					.getBrandCertificate());
			label_brandCertificate.setLabel(pumperDocuments
					.getBrandCertificate().substring(
							pumperDocuments.getBrandCertificate().lastIndexOf(
									"/") + 1));
			Toolbarbutton del_brandCertificate = (Toolbarbutton) this
					.getFellow("del_brandCertificate");
			del_brandCertificate.setVisible(true);
			Button up_brandCertificate = (Button) this
					.getFellow("up_brandCertificate");
			up_brandCertificate.setDisabled(true);
		}
		
		if (pumperDocuments.getFactoryForm() != null
				&& !pumperDocuments.getFactoryForm().isEmpty()) {
			Toolbarbutton label_factoryForm = (Toolbarbutton) this
					.getFellow("label_factoryForm");
			label_factoryForm.setContext(pumperDocuments
					.getFactoryForm());
			label_factoryForm.setLabel(pumperDocuments
					.getFactoryForm().substring(
							pumperDocuments.getFactoryForm().lastIndexOf(
									"/") + 1));
			Toolbarbutton del_factoryForm = (Toolbarbutton) this
					.getFellow("del_factoryForm");
			del_factoryForm.setVisible(true);
			Button up_factoryForm = (Button) this
					.getFellow("up_factoryForm");
			up_factoryForm.setDisabled(true);
		}
		
		if (pumperDocuments.getProduceIntro() != null
				&& !pumperDocuments.getProduceIntro().isEmpty()) {
			Toolbarbutton label_produceIntro = (Toolbarbutton) this
					.getFellow("label_produceIntro");
			label_produceIntro.setContext(pumperDocuments
					.getProduceIntro());
			label_produceIntro.setLabel(pumperDocuments
					.getProduceIntro().substring(
							pumperDocuments.getProduceIntro().lastIndexOf(
									"/") + 1));
			Toolbarbutton del_produceIntro = (Toolbarbutton) this
					.getFellow("del_produceIntro");
			del_produceIntro.setVisible(true);
			Button up_produceIntro = (Button) this
					.getFellow("up_produceIntro");
			up_produceIntro.setDisabled(true);
		}
		
		if (pumperDocuments.getMaterialsForm() != null
				&& !pumperDocuments.getMaterialsForm().isEmpty()) {
			Toolbarbutton label_materialsForm = (Toolbarbutton) this
					.getFellow("label_materialsForm");
			label_materialsForm.setContext(pumperDocuments
					.getMaterialsForm());
			label_materialsForm.setLabel(pumperDocuments
					.getMaterialsForm().substring(
							pumperDocuments.getMaterialsForm().lastIndexOf(
									"/") + 1));
			Toolbarbutton del_materialsForm = (Toolbarbutton) this
					.getFellow("del_materialsForm");
			del_materialsForm.setVisible(true);
			Button up_materialsForm = (Button) this
					.getFellow("up_materialsForm");
			up_materialsForm.setDisabled(true);
		}
		
		if (pumperDocuments.getInspectStandard() != null
				&& !pumperDocuments.getInspectStandard().isEmpty()) {
			Toolbarbutton label_inspectStandard = (Toolbarbutton) this
					.getFellow("label_inspectStandard");
			label_inspectStandard.setContext(pumperDocuments
					.getInspectStandard());
			label_inspectStandard.setLabel(pumperDocuments
					.getInspectStandard().substring(
							pumperDocuments.getInspectStandard().lastIndexOf(
									"/") + 1));
			Toolbarbutton del_inspectStandard = (Toolbarbutton) this
					.getFellow("del_inspectStandard");
			del_inspectStandard.setVisible(true);
			Button up_inspectStandard = (Button) this
					.getFellow("up_inspectStandard");
			up_inspectStandard.setDisabled(true);
		}
		
		if (pumperDocuments.getBrandCertificate() != null
				&& !pumperDocuments.getBrandCertificate().isEmpty()) {
			Toolbarbutton label_qualityStandard = (Toolbarbutton) this
					.getFellow("label_qualityStandard");
			label_qualityStandard.setContext(pumperDocuments
					.getQualityStandard());
			label_qualityStandard.setLabel(pumperDocuments
					.getQualityStandard().substring(
							pumperDocuments.getQualityStandard().lastIndexOf(
									"/") + 1));
			Toolbarbutton del_qualityStandard = (Toolbarbutton) this
					.getFellow("del_qualityStandard");
			del_qualityStandard.setVisible(true);
			Button up_qualityStandard = (Button) this
					.getFellow("up_qualityStandard");
			up_qualityStandard.setDisabled(true);
		}
		
		if (pumperDocuments.getQualityResponsibilityForm() != null
				&& !pumperDocuments.getQualityResponsibilityForm().isEmpty()) {
			Toolbarbutton label_qualityResponsibilityForm = (Toolbarbutton) this
					.getFellow("label_qualityResponsibilityForm");
			label_qualityResponsibilityForm.setContext(pumperDocuments
					.getQualityResponsibilityForm());
			label_qualityResponsibilityForm.setLabel(pumperDocuments
					.getQualityResponsibilityForm().substring(
							pumperDocuments.getQualityResponsibilityForm().lastIndexOf(
									"/") + 1));
			Toolbarbutton del_qualityResponsibilityForm = (Toolbarbutton) this
					.getFellow("del_qualityResponsibilityForm");
			del_qualityResponsibilityForm.setVisible(true);
			Button up_qualityResponsibilityForm = (Button) this
					.getFellow("up_qualityResponsibilityForm");
			up_qualityResponsibilityForm.setDisabled(true);
		}
		
		if (pumperDocuments.getJigouGraphy() != null
				&& !pumperDocuments.getJigouGraphy().isEmpty()) {
			Toolbarbutton label_jigouGraphy = (Toolbarbutton) this
					.getFellow("label_jigouGraphy");
			label_jigouGraphy.setContext(pumperDocuments
					.getJigouGraphy());
			label_jigouGraphy.setLabel(pumperDocuments
					.getJigouGraphy().substring(
							pumperDocuments.getJigouGraphy().lastIndexOf(
									"/") + 1));
			Toolbarbutton del_jigouGraphy = (Toolbarbutton) this
					.getFellow("del_jigouGraphy");
			del_jigouGraphy.setVisible(true);
			Button up_jigouGraphy = (Button) this
					.getFellow("up_jigouGraphy");
			up_jigouGraphy.setDisabled(true);
		}
		
		if (pumperDocuments.getBrandCertificate() != null
				&& !pumperDocuments.getBrandCertificate().isEmpty()) {
			Toolbarbutton label_chassisCertificate = (Toolbarbutton) this
					.getFellow("label_chassisCertificate");
			label_chassisCertificate.setContext(pumperDocuments
					.getChassisCertificate());
			label_chassisCertificate.setLabel(pumperDocuments
					.getChassisCertificate().substring(
							pumperDocuments.getChassisCertificate().lastIndexOf(
									"/") + 1));
			Toolbarbutton del_chassisCertificate = (Toolbarbutton) this
					.getFellow("del_chassisCertificate");
			del_chassisCertificate.setVisible(true);
			Button up_chassisCertificate = (Button) this
					.getFellow("up_chassisCertificate");
			up_chassisCertificate.setDisabled(true);
		}
		
		if (pumperDocuments.getUnitDesc() != null
				&& !pumperDocuments.getUnitDesc().isEmpty()) {
			Toolbarbutton label_unitDesc = (Toolbarbutton) this
					.getFellow("label_unitDesc");
			label_unitDesc.setContext(pumperDocuments
					.getUnitDesc());
			label_unitDesc.setLabel(pumperDocuments
					.getUnitDesc().substring(
							pumperDocuments.getUnitDesc().lastIndexOf(
									"/") + 1));
			Toolbarbutton del_unitDesc = (Toolbarbutton) this
					.getFellow("del_unitDesc");
			del_unitDesc.setVisible(true);
			Button up_unitDesc = (Button) this
					.getFellow("up_unitDesc");
			up_unitDesc.setDisabled(true);
		}
		
		if (pumperDocuments.getUnitTypeForm() != null
				&& !pumperDocuments.getUnitTypeForm().isEmpty()) {
			Toolbarbutton label_unitTypeForm = (Toolbarbutton) this
					.getFellow("label_unitTypeForm");
			label_unitTypeForm.setContext(pumperDocuments
					.getUnitTypeForm());
			label_unitTypeForm.setLabel(pumperDocuments
					.getUnitTypeForm().substring(
							pumperDocuments.getUnitTypeForm().lastIndexOf(
									"/") + 1));
			Toolbarbutton del_unitTypeForm = (Toolbarbutton) this
					.getFellow("del_unitTypeForm");
			del_unitTypeForm.setVisible(true);
			Button up_unitTypeForm = (Button) this
					.getFellow("up_unitTypeForm");
			up_unitTypeForm.setDisabled(true);
		}
		
		if (pumperDocuments.getUnitDiffDesc() != null
				&& !pumperDocuments.getUnitDiffDesc().isEmpty()) {
			Toolbarbutton label_unitDiffDesc = (Toolbarbutton) this
					.getFellow("label_unitDiffDesc");
			label_unitDiffDesc.setContext(pumperDocuments
					.getUnitDiffDesc());
			label_unitDiffDesc.setLabel(pumperDocuments
					.getUnitDiffDesc().substring(
							pumperDocuments.getUnitDiffDesc().lastIndexOf(
									"/") + 1));
			Toolbarbutton del_unitDiffDesc = (Toolbarbutton) this
					.getFellow("del_unitDiffDesc");
			del_unitDiffDesc.setVisible(true);
			Button up_unitDiffDesc = (Button) this
					.getFellow("up_unitDiffDesc");
			up_unitDiffDesc.setDisabled(true);
		}
		
		if (pumperDocuments.getTechnologyDesc() != null
				&& !pumperDocuments.getTechnologyDesc().isEmpty()) {
			Toolbarbutton label_technologyDesc = (Toolbarbutton) this
					.getFellow("label_technologyDesc");
			label_technologyDesc.setContext(pumperDocuments
					.getTechnologyDesc());
			label_technologyDesc.setLabel(pumperDocuments
					.getTechnologyDesc().substring(
							pumperDocuments.getTechnologyDesc().lastIndexOf(
									"/") + 1));
			Toolbarbutton del_technologyDesc = (Toolbarbutton) this
					.getFellow("del_technologyDesc");
			del_technologyDesc.setVisible(true);
			Button up_technologyDesc = (Button) this
					.getFellow("up_technologyDesc");
			up_technologyDesc.setDisabled(true);
		}
		
		
		if (pumperDocuments.getProductionFormReport() != null
				&& !pumperDocuments.getProductionFormReport().isEmpty()) {
			Toolbarbutton label_productionFormReport = (Toolbarbutton) this
					.getFellow("label_productionFormReport");
			label_productionFormReport.setContext(pumperDocuments
					.getProductionFormReport());
			label_productionFormReport.setLabel(pumperDocuments
					.getProductionFormReport().substring(
							pumperDocuments.getProductionFormReport().lastIndexOf(
									"/") + 1));
			Toolbarbutton del_productionFormReport = (Toolbarbutton) this
					.getFellow("del_productionFormReport");
			del_productionFormReport.setVisible(true);
			Button up_productionFormReport = (Button) this
					.getFellow("up_productionFormReport");
			up_productionFormReport.setDisabled(true);
		}
		
		if (pumperDocuments.getCcc() != null
				&& !pumperDocuments.getCcc().isEmpty()) {
			Toolbarbutton label_ccc = (Toolbarbutton) this
					.getFellow("label_ccc");
			label_ccc.setContext(pumperDocuments
					.getCcc());
			label_ccc.setLabel(pumperDocuments
					.getCcc().substring(
							pumperDocuments.getCcc().lastIndexOf(
									"/") + 1));
			Toolbarbutton del_ccc = (Toolbarbutton) this
					.getFellow("del_ccc");
			del_ccc.setVisible(true);
			Button up_ccc = (Button) this
					.getFellow("up_ccc");
			up_ccc.setDisabled(true);
		}
		
		if (pumperDocuments.getQzxApplicationForm() != null
				&& !pumperDocuments.getQzxApplicationForm().isEmpty()) {
			Toolbarbutton label_qzxApplicationForm = (Toolbarbutton) this
					.getFellow("label_qzxApplicationForm");
			label_qzxApplicationForm.setContext(pumperDocuments
					.getQzxApplicationForm());
			label_qzxApplicationForm.setLabel(pumperDocuments
					.getQzxApplicationForm().substring(
							pumperDocuments.getQzxApplicationForm().lastIndexOf(
									"/") + 1));
			Toolbarbutton del_qzxApplicationForm = (Toolbarbutton) this
					.getFellow("del_qzxApplicationForm");
			del_qzxApplicationForm.setVisible(true);
			Button up_qzxApplicationForm = (Button) this
					.getFellow("up_qzxApplicationForm");
			up_qzxApplicationForm.setDisabled(true);
		}
		
		// manual的多个文件
		String multiFile = pumperDocuments.getManual();
		if (multiFile != null) {
			String[] fileArr = multiFile.split("\\|");
			Hbox hbox = (Hbox) getFellow("box_manual");
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
					eventDate[1] = "manual";
					tb.addForward("onClick", this, "onDeleteAttachfileList",
							eventDate);
					tb.setContext(filrUrl);
					tb.setParent(hbox);
				}
			}
		}
		
		
		
		
	}

	public void onSave() {
		validateData();
		String cmd = "add";
		String pcmd = (String) params.get("cmd");
		if (pcmd != null)
			cmd = pcmd;

		application.setPumperDocuments(pumperDocuments);
		this.detach();
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
				Toolbarbutton lbFileName = (Toolbarbutton) this
						.getFellow("label_" + fieldName);
				lbFileName.setLabel(fileName);
				lbFileName.setContext(filrUrl);
				Textbox tbFile = (Textbox) this.getFellow(fieldName);
				tbFile.setValue(filrUrl);

				if (fieldName.equals("brandCertificate"))
					pumperDocuments.setBrandCertificate(filrUrl);
				if (fieldName.equals("ccc"))
					pumperDocuments.setCcc(filrUrl);
				if (fieldName.equals("factoryForm"))
					pumperDocuments.setFactoryForm(filrUrl);
				if (fieldName.equals("produceIntro"))
					pumperDocuments.setProduceIntro(filrUrl);
				if (fieldName.equals("materialsForm"))
					pumperDocuments.setMaterialsForm(filrUrl);
				if (fieldName.equals("inspectStandard"))
					pumperDocuments.setInspectStandard(filrUrl);
				if (fieldName.equals("qualityStandard"))
					pumperDocuments.setQualityStandard(filrUrl);
				if (fieldName.equals("qualityResponsibilityForm"))
					pumperDocuments.setQualityResponsibilityForm(filrUrl);
				if (fieldName.equals("jigouGraphy"))
					pumperDocuments.setJigouGraphy(filrUrl);
				if (fieldName.equals("chassisCertificate"))
					pumperDocuments.setChassisCertificate(filrUrl);
				if (fieldName.equals("unitDesc"))
					pumperDocuments.setUnitDesc(filrUrl);
				if (fieldName.equals("unitTypeForm"))
					pumperDocuments.setUnitTypeForm(filrUrl);
				if (fieldName.equals("unitDiffDesc"))
					pumperDocuments.setUnitDiffDesc(filrUrl);
				if (fieldName.equals("technologyDesc"))
					pumperDocuments.setTechnologyDesc(filrUrl);
				if (fieldName.equals("productionFormReport"))
					pumperDocuments.setProductionFormReport(filrUrl);
				if (fieldName.equals("qzxApplicationForm"))
					pumperDocuments.setQzxApplicationForm(filrUrl);

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

		Toolbarbutton toolbar = (Toolbarbutton) this.getFellow("del_"
				+ fieldName);
		Toolbarbutton lbFileName = (Toolbarbutton) this.getFellow("label_"
				+ fieldName);
		Textbox tb = (Textbox) this.getFellow(fieldName);

		String contextPath = Executions.getCurrent().getDesktop().getWebApp()
				.getRealPath(lbFileName.getContext());

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
			if (fieldName.equals("brandCertificate"))
				pumperDocuments.setBrandCertificate(filrUrl);
			if (fieldName.equals("ccc"))
				pumperDocuments.setCcc(filrUrl);
			if (fieldName.equals("factoryForm"))
				pumperDocuments.setFactoryForm(filrUrl);
			if (fieldName.equals("produceIntro"))
				pumperDocuments.setProduceIntro(filrUrl);
			if (fieldName.equals("materialsForm"))
				pumperDocuments.setMaterialsForm(filrUrl);
			if (fieldName.equals("inspectStandard"))
				pumperDocuments.setInspectStandard(filrUrl);
			if (fieldName.equals("qualityStandard"))
				pumperDocuments.setQualityStandard(filrUrl);
			if (fieldName.equals("qualityResponsibilityForm"))
				pumperDocuments.setQualityResponsibilityForm(filrUrl);
			if (fieldName.equals("JigouGraphy"))
				pumperDocuments.setJigouGraphy(filrUrl);
			if (fieldName.equals("chassisCertificate"))
				pumperDocuments.setChassisCertificate(filrUrl);
			if (fieldName.equals("unitDesc"))
				pumperDocuments.setUnitDesc(filrUrl);
			if (fieldName.equals("unitTypeForm"))
				pumperDocuments.setUnitTypeForm(filrUrl);
			if (fieldName.equals("unitDiffDesc"))
				pumperDocuments.setUnitDiffDesc(filrUrl);
			if (fieldName.equals("technologyDesc"))
				pumperDocuments.setTechnologyDesc(filrUrl);
			if (fieldName.equals("productionFormReport"))
				pumperDocuments.setProductionFormReport(filrUrl);
			if (fieldName.equals("qzxApplicationForm"))
				pumperDocuments.setQzxApplicationForm(filrUrl);
		}
	}

	/**
	 * Cancel this screen
	 * 
	 */
	public void onCancel() {
		this.detach();
	}

	void validateData() {

		Textbox brandCertificate = (Textbox) this.getFellow("brandCertificate");
		brandCertificate.setConstraint("no empty:商标注册证及核准注册续展证明不能为空");
		brandCertificate.getValue();

		Textbox factoryForm = (Textbox) this.getFellow("factoryForm");
		factoryForm.setConstraint("no empty:工厂情况调查表不能为空");
		factoryForm.getValue();

		Textbox produceIntro = (Textbox) this.getFellow("produceIntro");
		produceIntro.setConstraint("no empty:工厂情况调查表不能为空");
		produceIntro.getValue();

		Textbox materialsForm = (Textbox) this.getFellow("materialsForm");
		materialsForm.setConstraint("no empty:主要原材料、关键外购件登记表");
		materialsForm.getValue();

		Textbox inspectStandard = (Textbox) this.getFellow("inspectStandard");
		inspectStandard.setConstraint("no empty:检测项目所执行的技术标准目录不能为空");
		inspectStandard.getValue();

		Textbox qualityStandard = (Textbox) this.getFellow("qualityStandard");
		qualityStandard.setConstraint("no empty:质量管理体系文件目录不能为空");
		qualityStandard.getValue();

		Textbox qualityResponsibilityForm = (Textbox) this
				.getFellow("qualityResponsibilityForm");
		qualityResponsibilityForm.setConstraint("no empty:质量职责分配表为空");
		qualityResponsibilityForm.getValue();

		Textbox jigouGraphy = (Textbox) this.getFellow("jigouGraphy");
		jigouGraphy.setConstraint("no empty:机构框图(或表)不能为空");
		jigouGraphy.getValue();

		Textbox ccc = (Textbox) this.getFellow("ccc");
		ccc.setConstraint("no empty:汽车消防车产品3C认证合同不能为空");
		ccc.getValue();

		Textbox qzxApplicationForm = (Textbox) this
				.getFellow("qzxApplicationForm");
		qzxApplicationForm.setConstraint("no empty:不能为空");
		qzxApplicationForm.getValue();

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
	
	public void onFileListUP(ForwardEvent event) throws IOException {
		String fieldName = event.getData().toString();
		Media media = null;
		String fileName;

		try {
			media = Fileupload.get();
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
				if (fieldName.equals("manual")) {
					String str = pumperDocuments.getManual();
					if (str == null)
						str = "";
					pumperDocuments.setManual(str + "|" + filrUrl);
				}

			}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		String contextPath = Executions.getCurrent().getDesktop().getWebApp()
				.getRealPath(tb.getContext());

		File file = new File(contextPath);
		if (file.isFile()) {
			file.delete();

			tb.detach();
			lb.detach();
			String filrUrl = pumperDocuments.getManual();
			filrUrl = filrUrl.replaceAll(fileName, "");

			if (fieldName.equals("qualityManual")) {
				pumperDocuments.setManual(filrUrl);
			}
		}
	}
	
	// 实现下载
	public void onFileListDownload(ForwardEvent event)
			throws FileNotFoundException, UnsupportedEncodingException {
		String fileUrl = event.getData().toString();

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

}
