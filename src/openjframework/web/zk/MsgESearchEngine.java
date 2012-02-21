package openjframework.web.zk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.zul.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;

import cccf.ma.model.EnterpriseInfo;
import cccf.ma.model.EnterpriseOwnActivatedProductListInfo;
import cccf.ma.model.LocationInfo;
import cccf.ma.model.ProductCatalogueInfo;
import cccf.ma.model.StateInfo;
import cccf.ma.service.EnterpriseInfoServiceUtil;
import cccf.ma.service.EnterpriseOwnActivatedProductListInfoServiceUtil;
import cccf.ma.service.LocationInfoServiceUtil;
import cccf.ma.service.ProductCatalogueInfoServiceUtil;
import cccf.ma.service.StateInfoServiceUtil;

@SuppressWarnings({"serial","unchecked"})
public class MsgESearchEngine extends GenericForwardComposer {

	private String queryStr;
	private List<String> searchType;
	private List<EnterpriseInfo> eList;
	private List<StateInfo> sList;
	private List<LocationInfo> pList;
	private List<LocationInfo> cList;
	private List<ProductCatalogueInfo> productList;

	private Combobox cbxSearchType;
	private Row rwContent;
	private Bandbox bdState,bdProvince,bdCity,bdProduct;
	private Listbox lbxState,lbxPro,lbxCity,lbxProduct;
	private Window frmMsgSearchEngine;
	private Textbox tbxSearchStr;
	private Div divLocation;
	private Toolbarbutton btnSearch;

	Map param = new HashMap();
	
	public void onSelect$cbxSearchType() {
		String type = cbxSearchType.getSelectedItem().getLabel();
		if (type.equals("按企业名称")) {
			tbxSearchStr.setDisabled(false);
			tbxSearchStr.setVisible(true);
			bdState.setVisible(false);
			bdProvince.setVisible(false);
			bdCity.setVisible(false);
			bdProduct.setVisible(false);
			
		}else if (type.equals("按国家")){
			tbxSearchStr.setVisible(false);
			bdProvince.setVisible(false);
			bdProduct.setVisible(false);
			bdCity.setVisible(false);
			sList = StateInfoServiceUtil.getAll();
			ListModelList model = new ListModelList(sList);
			lbxState.setModel(model);
			bdState.setVisible(true);
		}else if (type.equals("按地区")) {
			tbxSearchStr.setVisible(false);
			bdProduct.setVisible(false);
			bdState.setVisible(false);
			pList = LocationInfoServiceUtil.getProvinceList();
			cList = new ArrayList<LocationInfo>();
			ListModelList model = new ListModelList(pList);
			lbxPro.setModel(model);
			bdProvince.setVisible(true);
			bdCity.setVisible(true);
		}else {
			tbxSearchStr.setVisible(false);
			bdState.setVisible(false);
			bdProvince.setVisible(false);
			bdCity.setVisible(false);
			bdProduct.setVisible(true);
			productList = ProductCatalogueInfoServiceUtil.getLeafData();
			ListModelList model = new ListModelList(productList);
			lbxProduct.setModel(model);
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
		Events.postEvent(MsgEnterpriseListController.ON_RELOAD, rwContent.getFellow("frmEnterpriseList"), eList);
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
				}
			}			

			ListModelList model = new ListModelList(cList);

			lbxCity.setModel(model);
		}
	}
	
	public void onClick$btnSearch(){
		System.out.println("onclicked");
		searchStr();
		Events.postEvent(MsgEnterpriseListController.ON_RELOAD, rwContent.getFirstChild(), eList);
	}
	
	private void searchStr(){
		String queryStr = "";
		if (tbxSearchStr.isVisible()) {
			queryStr = tbxSearchStr.getText();
			eList = EnterpriseInfoServiceUtil.findByName(queryStr);
		}else if(bdProvince.isVisible()){
			Set<Listitem> set = lbxCity.getSelectedItems();
			eList = new ArrayList<EnterpriseInfo>();
			for(Listitem it : set){
				LocationInfo city = (LocationInfo) it.getValue();
				if(null != city.getName()){
					List<EnterpriseInfo> eList = EnterpriseInfoServiceUtil.findByLocation(city.getName());
					for(EnterpriseInfo e : eList){
						this.eList.add(e);
					}
				}
			}
		}else if(bdState.isVisible()){
			Set<Listitem> set = lbxState.getSelectedItems();
			eList = new ArrayList<EnterpriseInfo>();
			for(Listitem it : set){
				StateInfo s = (StateInfo) it.getValue();
				List<EnterpriseInfo> eList = EnterpriseInfoServiceUtil.findByState(s);
				for(EnterpriseInfo e : eList){
					this.eList.add(e);
				}
			}
		}else {
			Set<Listitem> set = lbxProduct.getSelectedItems();
			eList = new ArrayList<EnterpriseInfo>();
			for(Listitem it : set){
				ProductCatalogueInfo product = (ProductCatalogueInfo) it.getValue();
				List<EnterpriseInfo> eList = EnterpriseOwnActivatedProductListInfoServiceUtil.findEByProduct(product);
				for(EnterpriseInfo e : eList){
					this.eList.add(e);
				}
			}
		}
		
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
//		btnSearch.setDisabled(true);
		Window objWindow = (Window) Executions.createComponents(
				"/SysForm/EnterpriseNotice/msg-enterprise-list.zul", rwContent, null);
		objWindow.doEmbedded();
//		Window objWindow = (Window) Executions.createComponents(
//				"/SysForm/EnterpriseNotice/msg-enterprise-list.zul", rwContent, null);
//		eList = EnterpriseInfoServiceUtil.getAll();
//		Events.postEvent(MsgEnterpriseListController.ON_RELOAD, rwContent.getFellow("frmEnterpriseList"), eList);
//		objWindow.doEmbedded();
		init();
	}

	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}

	public String getQueryStr() {
		return queryStr;
	}

	public void setSearchType(List<String> searchType) {
		this.searchType = searchType;
	}

	public List<String> getSearchType() {
		return searchType;
	}

	private void init() {
		searchType = new ArrayList<String>();
		searchType.add("按企业名称");
		searchType.add("按相关产品");
		searchType.add("按地区");
		searchType.add("按国家");
	}

	public void setEList(List<EnterpriseInfo> eList) {
		this.eList = eList;
	}

	public List<EnterpriseInfo> getEList() {
		return eList;
	}

	public void setSList(List<StateInfo> sList) {
		this.sList = sList;
	}

	public List<StateInfo> getSList() {
		return sList;
	}

	public void setPList(List<LocationInfo> pList) {
		this.pList = pList;
	}

	public List<LocationInfo> getPList() {
		return pList;
	}

	public void setCList(List<LocationInfo> cList) {
		this.cList = cList;
	}

	public List<LocationInfo> getCList() {
		return cList;
	}
}
