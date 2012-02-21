package cccfmis.bpm.zk;

import java.util.Iterator;
import java.util.Map;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Row;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import cccf.ma.model.ApplicationInfo;
import cccf.ma.model.ProductionModelInfo;

public class ApplicationInformation extends Window {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Map<?, ?> params = Executions.getCurrent().getArg();
	ApplicationInfo applicationInfo;
	Grid appinfo;

	public void onCreate() {
		applicationInfo = (ApplicationInfo) this.getPage().getAttribute("inspectionApplication");
		appinfo = (Grid) getFellow("appinfo");
		// 申请企业
		if (applicationInfo != null && applicationInfo.getEnterprise() != null) {
			Row row = new Row();
			Label appname = new Label(applicationInfo.getEnterprise().getName());
			Label appperson = new Label(applicationInfo.getEnterprise().getContactPerson());
			Label apphonenum = new Label(applicationInfo.getEnterprise().getTelephoneNum());
			Label appaddress = new Label(applicationInfo.getEnterprise().getContactAddress());
			Label apptype = new Label("申请企业");
			row.appendChild(apptype);
			row.appendChild(appname);
			row.appendChild(appperson);
			row.appendChild(apphonenum);
			row.appendChild(appaddress);
			appinfo.getRows().appendChild(row);
		}
		// 生产商
		if (applicationInfo != null && applicationInfo.getProductionEnterprise() != null) {
			Row row = new Row();
			Label appname = new Label(applicationInfo.getProductionEnterprise().getName());
			Label appperson = new Label(applicationInfo.getProductionEnterprise().getFacLinkMan());
			Label apphonenum = new Label(applicationInfo.getProductionEnterprise().getFacTel());
			Label appaddress = new Label(applicationInfo.getProductionEnterprise().getContactAddress());
			Label apptype = new Label("生产商");
			row.appendChild(apptype);
			row.appendChild(appname);
			row.appendChild(appperson);
			row.appendChild(apphonenum);
			row.appendChild(appaddress);
			appinfo.getRows().appendChild(row);
		}
		// 制造商
		if (applicationInfo != null && applicationInfo.getManufacture() != null) {
			Row row = new Row();
			Label appname = new Label(applicationInfo.getManufacture().getName());
			Label appperson = new Label(applicationInfo.getManufacture().getManuLinkMan());
			Label apphonenum = new Label(applicationInfo.getManufacture().getManuTel());
			Label appaddress = new Label(applicationInfo.getManufacture().getContactAddress());
			Label apptype = new Label("制造商");
			row.appendChild(apptype);
			row.appendChild(appname);
			row.appendChild(appperson);
			row.appendChild(apphonenum);
			row.appendChild(appaddress);
			appinfo.getRows().appendChild(row);
		}
		// 具体型号
		if (applicationInfo != null && applicationInfo.getProductionModel() != null) {
			Listbox sqxxxh = (Listbox) getFellow("sqxxxh");
			//applicationInfo.getProductionModel()
			Iterator<ProductionModelInfo> productionModelInfos = applicationInfo.getProductionModel().iterator();
			while (productionModelInfos.hasNext()) {
				ProductionModelInfo productionModelInfo = productionModelInfos.next();
				Listitem item = new Listitem();
				Listcell name = new Listcell(productionModelInfo.getName());
				Listcell modelname = new Listcell(productionModelInfo.getModel());
				Listcell fullReport = new Listcell();
				if (productionModelInfo.getFullReport() != null) {
					Toolbarbutton fullReport1 = new Toolbarbutton("查看全性能委托检验报告");
					fullReport1.addForward(Events.ON_CLICK, this, "onOpenFile", productionModelInfo.getFullReport());
					fullReport.appendChild(fullReport1);
				}
				Listcell flowChart = new Listcell();
				if (productionModelInfo.getFlowChart() != null) {
					Toolbarbutton flowChart1 = new Toolbarbutton("查看产品工艺流程图");
					flowChart1.addForward(Events.ON_CLICK, this, "onOpenFile", productionModelInfo.getFlowChart());
					flowChart.appendChild(flowChart1);
				}
				Listcell characterForm = new Listcell();
				if (productionModelInfo.getCharacterForm() != null) {
					Toolbarbutton characterForm1 = new Toolbarbutton("查看关键件或产品特性描述填报表");
					characterForm1.addForward(Events.ON_CLICK, this, "onOpenFile", productionModelInfo.getCharacterForm());
					characterForm.appendChild(characterForm1);
				}
				Listcell pumperCcc = new Listcell();
				if (productionModelInfo.getPumperCcc() != null) {
					Toolbarbutton pumperCcc1 = new Toolbarbutton("查看汽车消防车产品3C认证申请书");
					pumperCcc1.addForward(Events.ON_CLICK, this, "onOpenFile", productionModelInfo.getPumperCcc());
					pumperCcc.appendChild(pumperCcc1);
				}

				item.appendChild(name);
				item.appendChild(modelname);
				item.appendChild(fullReport);
				item.appendChild(flowChart);
				item.appendChild(characterForm);
				item.appendChild(pumperCcc);
				sqxxxh.appendChild(item);
			}
		} else {
			Groupbox xhgroupbox = (Groupbox) getFellow("xhgroupbox");
			xhgroupbox.setVisible(false);
		}

		// 基本文件
		if (applicationInfo.getBusinessLisence() != null) {
			Row row = (Row) getFellow("businessLisence");
			row.setValue(applicationInfo.getBusinessLisence());
		} else {
			Row row = (Row) getFellow("businessLisence");
			((Cell) row.getChildren().get(2)).getChildren().clear();
			Label label = new Label("无");
			((Cell) row.getChildren().get(2)).appendChild(label);
		}
		if (applicationInfo.getOrganizationCode() != null) {
			Row row = (Row) getFellow("organizationCode");
			row.setValue(applicationInfo.getOrganizationCode());
		} else {
			Row row = (Row) getFellow("organizationCode");
			((Cell) row.getChildren().get(2)).getChildren().clear();
			Label label = new Label("无");
			((Cell) row.getChildren().get(2)).appendChild(label);
		}
		if (applicationInfo.getInspectionDeviceList() != null) {
			Row row = (Row) getFellow("inspectionDeviceList");
			row.setValue(applicationInfo.getInspectionDeviceList());
		} else {
			Row row = (Row) getFellow("inspectionDeviceList");
			((Cell) row.getChildren().get(2)).getChildren().clear();
			Label label = new Label("无");
			((Cell) row.getChildren().get(2)).appendChild(label);
		}
		if (applicationInfo.getLayoutGraph() != null) {
			Row row = (Row) getFellow("layoutGraph");
			row.setValue(applicationInfo.getLayoutGraph());
		} else {
			Row row = (Row) getFellow("layoutGraph");
			((Cell) row.getChildren().get(2)).getChildren().clear();
			Label label = new Label("无");
			((Cell) row.getChildren().get(2)).appendChild(label);
		}
		// if (applicationInfo.getBrand() != null) {
		// Row row = (Row) getFellow("brand");
		// row.setValue(applicationInfo.getBrand());
		// } else {
		// Row row = (Row) getFellow("brand");
		// ((Cell) row.getChildren().get(2)).getChildren().clear();
		// Label label = new Label("无");
		// ((Cell) row.getChildren().get(2)).appendChild(label);
		// }
		if (applicationInfo.getOriginalCertificate() != null) {
			Row row = (Row) getFellow("originalCertificate");
			row.setValue(applicationInfo.getOriginalCertificate());
		} else {
			Row row = (Row) getFellow("originalCertificate");
			((Cell) row.getChildren().get(2)).getChildren().clear();
			Label label = new Label("无");
			((Cell) row.getChildren().get(2)).appendChild(label);
		}
		if (applicationInfo.getQualityManual() != null) {
			Row row = (Row) getFellow("qualityManual");
			row.setValue(applicationInfo.getQualityManual());
		} else {
			Row row = (Row) getFellow("qualityManual");
			((Cell) row.getChildren().get(2)).getChildren().clear();
			Label label = new Label("无");
			((Cell) row.getChildren().get(2)).appendChild(label);
		}
	}

	public void onOpenFile(ForwardEvent event) throws InterruptedException {
		String fielpath = event.getData().toString();
		Window seefile = (Window) Executions.createComponents("/cccfpage/seeFile.zul", null, null);
		Iframe iframe = (Iframe) seefile.getFellow("ifrme");
		iframe.setSrc(fielpath);
		seefile.doModal();
	}

	public void onOpenFiles(ForwardEvent event) throws InterruptedException {
		String param = "";
		if (event.getData() != null)
			param = event.getData().toString();
		String filepath = "";
		if (param.trim().equals("businessLisence")) {
			filepath = applicationInfo.getBusinessLisence();
		} else if (param.trim().equals("organizationCode")) {
			filepath = applicationInfo.getOrganizationCode();
		} else if (param.trim().equals("inspectionDeviceList")) {
			filepath = applicationInfo.getInspectionDeviceList();
		} else if (param.trim().equals("layoutGraph")) {
			filepath = applicationInfo.getLayoutGraph();
		}
		// else if (param.trim().equals("brand")) {
		// filepath = applicationInfo.getBrand();
		// }
		else if (param.trim().equals("originalCertificate")) {
			filepath = applicationInfo.getOriginalCertificate();
		} else if (param.trim().equals("qualityManual")) {
			filepath = applicationInfo.getQualityManual();
		}
		Window seefile = (Window) Executions.createComponents("/cccfpage/seeFile.zul", null, null);
		Iframe iframe = (Iframe) seefile.getFellow("ifrme");
		iframe.setSrc(filepath);
		seefile.doModal();
	}
}
