package openjframework.web.zk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.annotations.ParamDef;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import com.opensymphony.xwork2.util.location.Location;
import com.sun.org.apache.bcel.internal.generic.NEW;

import cccf.ma.model.EnterpriseInfo;
import cccf.ma.model.LocationInfo;
import cccf.ma.model.StateInfo;
import cccf.ma.service.EnterpriseInfoServiceUtil;
import cccf.ma.service.LocationInfoServiceUtil;
import cccf.ma.service.StateInfoServiceUtil;

@SuppressWarnings( { "serial", "unchecked" })
public class MsgLocationSelectController extends GenericForwardComposer {

	private List<StateInfo> sList;
	private List<LocationInfo> pList;
	private List<LocationInfo> cList;
	private List<EnterpriseInfo> eList;

	private Listbox lbxState, lbxPro, lbxCity;
	private Window objWindow;
	private Div divEpList;
	private Checkbox cbxSelectAll;
	private Toolbarbutton btnCheckAll;
	
	Map param = Executions.getCurrent().getArg();

	@Override
	public void doAfterCompose(Component comp) throws Exception {

		String str = (String) param.get("data");
		super.doAfterCompose(comp);

		if (str.equals("state")) {
			lbxCity.detach();
			lbxPro.detach();
			sList = StateInfoServiceUtil.getAll();
			eList = EnterpriseInfoServiceUtil.findByState(sList.get(0));
			param.put("enterpriseList", eList);
		} else {
			lbxState.detach();
			pList = LocationInfoServiceUtil.getProvinceList();
			cList = LocationInfoServiceUtil.getCityList(pList.get(0));

			eList = EnterpriseInfoServiceUtil.findByLocation(pList.get(0)
					.getName());
			param.put("enterpriseList", eList);

		}
		{
			objWindow = (Window) Executions.createComponents(
					"/SysForm/EnterpriseNotice/msg-enterprise-list.zul", divEpList, param);
			objWindow.doEmbedded();
			Events.postEvent(MsgEnterpriseListController.ON_CHECKALL, divEpList.getFellow("frmEnterpriseList"), null);
		}
	}
	
	public void onClick$btnOkState(){
		Set<Listitem> set = lbxState.getSelectedItems();
		eList = new ArrayList<EnterpriseInfo>();
		for(Listitem it : set){
			StateInfo s = (StateInfo) it.getValue();
			List<EnterpriseInfo> eList = EnterpriseInfoServiceUtil.findByState(s);
			for(EnterpriseInfo e : eList){
				this.eList.add(e);
			}
		}
		Events.postEvent(MsgEnterpriseListController.ON_RELOAD, divEpList.getFellow("frmEnterpriseList"), eList);
	}
	
	public void onClick$btnCheckAll(){
		List<Listitem> items = lbxState.getItems();
		if (btnCheckAll.getLabel().equals("全选")) {
			btnCheckAll.setLabel("取消");
			for (Listitem it : items) {
				it.setSelected(true);
			}
		}else {
			btnCheckAll.setLabel("全选");
			for (Listitem it : items) {
				it.setSelected(false);
			}
		}
	}

	public void onSelect$lbxPro() {
		if (!lbxPro.getSelectedItems().isEmpty()) {
			cList.clear();
			Set<Listitem> items = lbxPro.getSelectedItems();
			for (Listitem item : items) {
				LocationInfo p = (LocationInfo) item.getValue();
				List<LocationInfo> temp = LocationInfoServiceUtil.getCityList(p);
				for(LocationInfo city : temp){
					cList.add(city);
//					= LocationInfoServiceUtil.getCityList(p);
				}
			}			

			ListModelList model = new ListModelList(cList);

			lbxCity.setModel(model);
		}
	}
	
	public void onCheck$cbxSelectAll() {
		List<Listitem> items = lbxCity.getItems();
		if (cbxSelectAll.isChecked()) {
			for (Listitem it : items) {
				it.setSelected(true);
			}
		} else {
			for (Listitem it : items) {
				it.setSelected(false);
			}
		}
	}
	
	public void onClick$btnOkLoc(){
		Set<Listitem> set = lbxCity.getSelectedItems();
		eList = new ArrayList<EnterpriseInfo>();
		for(Listitem it : set){
			LocationInfo city = (LocationInfo) it.getValue();
			List<EnterpriseInfo> eList = EnterpriseInfoServiceUtil.findByLocation(city.getName());
			for(EnterpriseInfo e : eList){
				this.eList.add(e);
			}
		}
		Events.postEvent(MsgEnterpriseListController.ON_RELOAD, divEpList.getFellow("frmEnterpriseList"), eList);
	}

	public void setSList(List<StateInfo> sList) {
		this.sList = sList;
	}

	public List<StateInfo> getSList() {
		return sList;
	}

	public void setCList(List<LocationInfo> cList) {
		this.cList = cList;
	}

	public List<LocationInfo> getCList() {
		return cList;
	}

	public void setPList(List<LocationInfo> pList) {
		this.pList = pList;
	}

	public List<LocationInfo> getPList() {
		return pList;
	}

}
