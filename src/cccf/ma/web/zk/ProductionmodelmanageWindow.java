package cccf.ma.web.zk;

import java.util.List;

import org.zkoss.zk.ui.Components;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import cccf.ma.model.ApplicationInfo;
import cccf.ma.model.EnterpriseProductModel;
import cccf.ma.model.ProductionModelInfo;

;

public class ProductionmodelmanageWindow extends Window {

	Long taskInstanceId;
	String processId = "0";
	String userId;

	Vbox vMutipleFiles;

	public ProductionModelInfo productionModelInfo;
	private EnterpriseProductModel emp;

	public ProductionModelInfo getProductionModelInfo() {
		return productionModelInfo;
	}

	public void setProductionModelInfo(ProductionModelInfo entity) {
		this.productionModelInfo = entity;
	}

	private ApplicationInfo application;

	public void onCreate() {
		Components.wireVariables(this, this);
	}

	public List getObjList() {
		Listbox listbox = getListbox();
		List list = (List) listbox.getModel();
		return list;
	}

	public Listbox getListbox() {
		return (Listbox) this.getFellow("productionModelInfoListbox");
	}

}
