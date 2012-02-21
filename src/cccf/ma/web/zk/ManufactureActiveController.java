package cccf.ma.web.zk;

import java.awt.Checkbox;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Window;
import org.zkoss.zul.api.Listcell;

import cccf.ma.model.ManufactureInfo;
import cccf.ma.model.ProductionEnterpriseInfo;
import cccf.ma.service.ManufactureInfoServiceUtil;
import cccf.ma.service.ProductionEnterpriseInfoServiceUtil;

@SuppressWarnings("serial")
public class ManufactureActiveController extends GenericForwardComposer{
	
	private List<ManufactureInfo> manuList;
	private List<ProductionEnterpriseInfo> proList;
	
	private Listbox lbxProduction,lbxManufacture;
	private Checkbox ckbxProSelectAll,ckbxManuSelectAll;
	private Button btnActive;
	private Window frmProductionManufacture;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		manuList = ManufactureInfoServiceUtil.getInactive();
		proList = ProductionEnterpriseInfoServiceUtil.getInactive();
	}
	
	public void onActiveManu(ForwardEvent event){
		
		Listcell cell = (Listcell) event.getOrigin().getTarget();
		ManufactureInfo manu = (ManufactureInfo) cell.getAttribute("manu");
		try {
			int reply = Messagebox.show("是否确认激活 ：" + manu.getName() + "?","提示",Messagebox.YES|Messagebox.NO,Messagebox.QUESTION);
			if (reply == Messagebox.YES) {
				ManufactureInfoServiceUtil.active(manu);
				lbxManufacture.removeChild(cell.getParent());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}
	
	public void onActivePro(ForwardEvent event){
		Listcell cell = (Listcell) event.getOrigin().getTarget();
		ProductionEnterpriseInfo manu = (ProductionEnterpriseInfo) cell.getAttribute("pro");
		try {
			int reply = Messagebox.show("是否确认激活 ：" + manu.getName() + "?","提示",Messagebox.YES|Messagebox.NO,Messagebox.QUESTION);
			if (reply == Messagebox.YES) {
				ProductionEnterpriseInfoServiceUtil.active(manu);
				lbxProduction.removeChild(cell.getParent());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}

	public void onClick$btnActive() throws InterruptedException {
		if (lbxManufacture.getSelectedCount() == 0
				&& lbxProduction.getSelectedCount() == 0) {
			throw new WrongValueException(btnActive, "请选择要激活的记录！");
		} else {
			int reply = Messagebox.show("是否确认激活选中的记录？", "提示", Messagebox.YES
					| Messagebox.NO, Messagebox.QUESTION);
			if (reply == Messagebox.NO) {
				return;
			} else {

				Set<Listitem> manuSet = lbxManufacture.getSelectedItems();
				Set<Listitem> proSet = lbxProduction.getSelectedItems();
				for (Listitem item : manuSet) {
					ManufactureInfo manu = (ManufactureInfo) item.getValue();
					ManufactureInfoServiceUtil.active(manu);
				}
				for (Listitem item : proSet) {
					ProductionEnterpriseInfo pro = (ProductionEnterpriseInfo) item
							.getValue();
					ProductionEnterpriseInfoServiceUtil.active(pro);
				}
				manuList = ManufactureInfoServiceUtil.getInactive();
				proList = ProductionEnterpriseInfoServiceUtil.getInactive();
				ListModel manuModel = new SimpleListModel(manuList);
				ListModel proModel = new SimpleListModel(proList);
				lbxManufacture.setModel(manuModel);
				lbxProduction.setModel(proModel);
			}
		}
	}
	
	public void onCheck$ckbxProSelectAll(){
		lbxProduction.selectAll();
	}
	
	public void onCheck$ckbxManuSelectAll() {
		lbxManufacture.selectAll();
	}
	
	public List<ManufactureInfo> getManuList() {
		return manuList;
	}

	public void setManuList(List<ManufactureInfo> manuList) {
		this.manuList = manuList;
	}

	public List<ProductionEnterpriseInfo> getProList() {
		return proList;
	}

	public void setProList(List<ProductionEnterpriseInfo> proList) {
		this.proList = proList;
	}

}
