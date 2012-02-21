package cccf.ma.web.zk;

import java.util.*;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import openjframework.util.ZkFileUtil;
import org.zkoss.zk.ui.*;
import org.zkoss.zul.*;
import org.zkoss.zk.ui.event.ForwardEvent;
import cccf.ma.model.*;

public class PumperdocumentsViewWindow extends Window {

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
		}

		if (pumperDocuments.getFactoryForm() != null
				&& !pumperDocuments.getFactoryForm().isEmpty()) {
			Toolbarbutton label_factoryForm = (Toolbarbutton) this
					.getFellow("label_factoryForm");
			label_factoryForm.setContext(pumperDocuments.getFactoryForm());
			label_factoryForm
					.setLabel(pumperDocuments.getFactoryForm()
							.substring(
									pumperDocuments.getFactoryForm()
											.lastIndexOf("/") + 1));
		}

		if (pumperDocuments.getProduceIntro() != null
				&& !pumperDocuments.getProduceIntro().isEmpty()) {
			Toolbarbutton label_produceIntro = (Toolbarbutton) this
					.getFellow("label_produceIntro");
			label_produceIntro.setContext(pumperDocuments.getProduceIntro());
			label_produceIntro
					.setLabel(pumperDocuments.getProduceIntro()
							.substring(
									pumperDocuments.getProduceIntro()
											.lastIndexOf("/") + 1));
		}

		if (pumperDocuments.getMaterialsForm() != null
				&& !pumperDocuments.getMaterialsForm().isEmpty()) {
			Toolbarbutton label_materialsForm = (Toolbarbutton) this
					.getFellow("label_materialsForm");
			label_materialsForm.setContext(pumperDocuments.getMaterialsForm());
			label_materialsForm
					.setLabel(pumperDocuments.getMaterialsForm()
							.substring(
									pumperDocuments.getMaterialsForm()
											.lastIndexOf("/") + 1));
		}

		if (pumperDocuments.getInspectStandard() != null
				&& !pumperDocuments.getInspectStandard().isEmpty()) {
			Toolbarbutton label_inspectStandard = (Toolbarbutton) this
					.getFellow("label_inspectStandard");
			label_inspectStandard.setContext(pumperDocuments
					.getInspectStandard());
			label_inspectStandard.setLabel(pumperDocuments.getInspectStandard()
					.substring(
							pumperDocuments.getInspectStandard().lastIndexOf(
									"/") + 1));
		}

		if (pumperDocuments.getBrandCertificate() != null
				&& !pumperDocuments.getBrandCertificate().isEmpty()) {
			Toolbarbutton label_qualityStandard = (Toolbarbutton) this
					.getFellow("label_qualityStandard");
			label_qualityStandard.setContext(pumperDocuments
					.getQualityStandard());
			label_qualityStandard.setLabel(pumperDocuments.getQualityStandard()
					.substring(
							pumperDocuments.getQualityStandard().lastIndexOf(
									"/") + 1));
		}

		if (pumperDocuments.getQualityResponsibilityForm() != null
				&& !pumperDocuments.getQualityResponsibilityForm().isEmpty()) {
			Toolbarbutton label_qualityResponsibilityForm = (Toolbarbutton) this
					.getFellow("label_qualityResponsibilityForm");
			label_qualityResponsibilityForm.setContext(pumperDocuments
					.getQualityResponsibilityForm());
			label_qualityResponsibilityForm.setLabel(pumperDocuments
					.getQualityResponsibilityForm().substring(
							pumperDocuments.getQualityResponsibilityForm()
									.lastIndexOf("/") + 1));
		}

		if (pumperDocuments.getJigouGraphy() != null
				&& !pumperDocuments.getJigouGraphy().isEmpty()) {
			Toolbarbutton label_jigouGraphy = (Toolbarbutton) this
					.getFellow("label_jigouGraphy");
			label_jigouGraphy.setContext(pumperDocuments.getJigouGraphy());
			label_jigouGraphy
					.setLabel(pumperDocuments.getJigouGraphy()
							.substring(
									pumperDocuments.getJigouGraphy()
											.lastIndexOf("/") + 1));
		}

		if (pumperDocuments.getBrandCertificate() != null
				&& !pumperDocuments.getBrandCertificate().isEmpty()) {
			Toolbarbutton label_chassisCertificate = (Toolbarbutton) this
					.getFellow("label_chassisCertificate");
			label_chassisCertificate.setContext(pumperDocuments
					.getChassisCertificate());
			label_chassisCertificate.setLabel(pumperDocuments
					.getChassisCertificate().substring(
							pumperDocuments.getChassisCertificate()
									.lastIndexOf("/") + 1));
		}

		if (pumperDocuments.getUnitDesc() != null
				&& !pumperDocuments.getUnitDesc().isEmpty()) {
			Toolbarbutton label_unitDesc = (Toolbarbutton) this
					.getFellow("label_unitDesc");
			label_unitDesc.setContext(pumperDocuments.getUnitDesc());
			label_unitDesc.setLabel(pumperDocuments.getUnitDesc().substring(
					pumperDocuments.getUnitDesc().lastIndexOf("/") + 1));
		}

		if (pumperDocuments.getUnitTypeForm() != null
				&& !pumperDocuments.getUnitTypeForm().isEmpty()) {
			Toolbarbutton label_unitTypeForm = (Toolbarbutton) this
					.getFellow("label_unitTypeForm");
			label_unitTypeForm.setContext(pumperDocuments.getUnitTypeForm());
			label_unitTypeForm
					.setLabel(pumperDocuments.getUnitTypeForm()
							.substring(
									pumperDocuments.getUnitTypeForm()
											.lastIndexOf("/") + 1));
		}

		if (pumperDocuments.getUnitDiffDesc() != null
				&& !pumperDocuments.getUnitDiffDesc().isEmpty()) {
			Toolbarbutton label_unitDiffDesc = (Toolbarbutton) this
					.getFellow("label_unitDiffDesc");
			label_unitDiffDesc.setContext(pumperDocuments.getUnitDiffDesc());
			label_unitDiffDesc
					.setLabel(pumperDocuments.getUnitDiffDesc()
							.substring(
									pumperDocuments.getUnitDiffDesc()
											.lastIndexOf("/") + 1));
		}

		if (pumperDocuments.getTechnologyDesc() != null
				&& !pumperDocuments.getTechnologyDesc().isEmpty()) {
			Toolbarbutton label_technologyDesc = (Toolbarbutton) this
					.getFellow("label_technologyDesc");
			label_technologyDesc
					.setContext(pumperDocuments.getTechnologyDesc());
			label_technologyDesc.setLabel(pumperDocuments.getTechnologyDesc()
					.substring(
							pumperDocuments.getTechnologyDesc()
									.lastIndexOf("/") + 1));
		}

		if (pumperDocuments.getProductionFormReport() != null
				&& !pumperDocuments.getProductionFormReport().isEmpty()) {
			Toolbarbutton label_productionFormReport = (Toolbarbutton) this
					.getFellow("label_productionFormReport");
			label_productionFormReport.setContext(pumperDocuments
					.getProductionFormReport());
			label_productionFormReport.setLabel(pumperDocuments
					.getProductionFormReport().substring(
							pumperDocuments.getProductionFormReport()
									.lastIndexOf("/") + 1));
		}

		if (pumperDocuments.getCcc() != null
				&& !pumperDocuments.getCcc().isEmpty()) {
			Toolbarbutton label_ccc = (Toolbarbutton) this
					.getFellow("label_ccc");
			label_ccc.setContext(pumperDocuments.getCcc());
			label_ccc.setLabel(pumperDocuments.getCcc().substring(
					pumperDocuments.getCcc().lastIndexOf("/") + 1));
		}

		if (pumperDocuments.getQzxApplicationForm() != null
				&& !pumperDocuments.getQzxApplicationForm().isEmpty()) {
			Toolbarbutton label_qzxApplicationForm = (Toolbarbutton) this
					.getFellow("label_qzxApplicationForm");
			label_qzxApplicationForm.setContext(pumperDocuments
					.getQzxApplicationForm());
			label_qzxApplicationForm.setLabel(pumperDocuments
					.getQzxApplicationForm().substring(
							pumperDocuments.getQzxApplicationForm()
									.lastIndexOf("/") + 1));
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

	// 实现下载
	public void onFiledown(ForwardEvent event) throws FileNotFoundException,
			UnsupportedEncodingException {
		String fieldName = event.getData().toString();

		Textbox tb = (Textbox) this.getFellow(fieldName);

		// 文件的真实url
		String fileUrl = tb.getValue();

		String downUrl = ZkFileUtil.getDownUrl(fileUrl) + "&online=true";
		openFileOnlineWindow(downUrl);

	}

	public void onFileListDownload(ForwardEvent event)
			throws FileNotFoundException, UnsupportedEncodingException {
		String fileUrl =event.getData().toString();
		String downUrl = ZkFileUtil.getDownUrl(fileUrl) + "&online=true";
		openFileOnlineWindow(downUrl);

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

}
