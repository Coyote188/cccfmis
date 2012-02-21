package cccf.ma.web.zk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.jhlabs.math.RidgedFBM;

import cccf.ma.model.EnterpriseInfo;
import cccf.ma.model.LocationInfo;
import cccf.ma.model.ProductionEnterpriseInfo;
import cccf.ma.model.StateInfo;
import cccf.ma.service.EnterpriseInfoServiceUtil;
import cccf.ma.service.LocationInfoServiceUtil;
import cccf.ma.service.ProductionEnterpriseInfoServiceUtil;
import cccf.ma.service.StateInfoServiceUtil;
import cccf.myenum.OrganizationNature;

public class PEEditController extends GenericForwardComposer{

	public static final String ON_SETSAVETOENTERPRISE = "onSetProperty";
	
	private ProductionEnterpriseInfo fac;
	public List<StateInfo> sList;
	public List<LocationInfo> pList;
	public List<LocationInfo> cList;
	private boolean isNewObj;
	private Window frmProductionEnter,frmEnterpriseOuter2,enterpriseCreateWindow,frmERF;
	
	private List<String> organizationNatureArray;
	private Combobox cbxProvince,cbxCity,cbxState,cbxOrganizationNature;
	private Textbox tbxLocation;
	private Textbox tbx000,tbx001,tbx002,tbx003,tbx004,tbx005,tbx006,tbx007,tbx008,tbx009,tbx010
		,tbx011,tbx012,tbx013,tbx014,tbx015,tbx016,tbx017,tbx018,tbx019,tbx020,tbx021,tbx022
		,tbx023,tbx024,tbx025,tbx026,tbx027,tbx028,tbx029,tbx030,tbx031,tbx032,tbx033;
	private Hbox hLocation;
	private Radiogroup rgpBusinessScope;
	
	Map param = Executions.getCurrent().getArg();
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		if (null != param.get("manu")) {
			setFac((ProductionEnterpriseInfo) param.get("manu"));
			isNewObj = false;
		} else {
			setFac(new ProductionEnterpriseInfo());
			isNewObj = true;
		}
		init();
		sList = StateInfoServiceUtil.getAll();
		pList = LocationInfoServiceUtil.getProvinceList();
	}
	
	Checkbox btnSetSameOfEnterprise;
	public void onCheck$btnSetSameOfEnterprise() {
		Events.sendEvent(EnterpriseinfoWindow.ON_SENDDATA2,
				enterpriseCreateWindow, null);
	}

	public void onSetProperty(Event ent) {
		System.out.println(ent.getData());
		if(btnSetSameOfEnterprise.isChecked()){
		EnterpriseInfo e = (EnterpriseInfo) ent.getData();
		tbx000.setText(e.getName());
		fac.setName(e.getName());
		tbx001.setText(e.getNameEn());
		fac.setFacNameEn(e.getNameEn());
		tbx002.setText(e.getOrganizationCode());
		fac.setFacCode(e.getOrganizationCode());
		cbxOrganizationNature.setText(e.getOrganizationNature());
		fac.setOrganizationNature(e.getOrganizationNature());
		rgpBusinessScope.setSelectedIndex(e.getScale());
		fac.setScale(e.getScale());
		tbx003.setText(e.getLegalPerson());
		fac.setFacLegalPerson(e.getLegalPerson());
		tbx004.setText(e.getLegalPersonEn());
		fac.setLegalPersonEn(e.getLegalPersonEn());
		tbx005.setText(e.getContactAddForLegal());
		fac.setContactAddForLegal(e.getContactAddForLegal());
		tbx006.setText(e.getContactAddForLegalEn());
		fac.setContactAddForLegalEn(e.getContactAddForLegalEn());
		tbx007.setText(e.getTelForLegal());
		fac.setTelForLegal(e.getTelForLegal());
		tbx008.setText(e.getMobileForLegal());
		fac.setMobileForLegal(e.getMobileForLegal());
		tbx014.setText(e.getRegisteredAddress());
		fac.setRegisteredAddress(e.getRegisteredAddress());
		tbx015.setText(e.getRegisteredAddressEn());
		fac.setRegisteredAddressEn(e.getRegisteredAddressEn());
		tbx016.setText(e.getRegisteredCapital());
		fac.setRegisteredCapital(e.getRegisteredCapital());
		tbx017.setText(e.getBusinessScope());
		fac.setFacWorkRange(e.getBusinessScope());
		cbxState.setText(e.getState());
		fac.setFacCountry(e.getState());
		hLocation.setVisible(false);
		tbxLocation.setConstraint("");
		tbxLocation.setText(e.getLocation());
		tbxLocation.setVisible(true);
		fac.setFacDistrict(e.getLocation());
		tbx019.setText(e.getContactAddress());
		fac.setContactAddress(e.getContactAddress());
		tbx020.setText(e.getContactAddressEn());
		fac.setContactAddressEn(e.getContactAddressEn());
		tbx025.setText(e.getPostalcode());
		fac.setFacZip(e.getPostalcode());
		tbx026.setText(e.getEmailAddress());
		fac.setFacEmail(e.getEmailAddress());
		tbx027.setText(e.getContactPerson());
		fac.setFacLinkMan(e.getContactPerson());
		tbx028.setText(e.getContactPersonEn());
		fac.setFacLinkManEn(e.getContactPersonEn());
		tbx029.setText(e.getTelephoneNum());
		fac.setFacTel(e.getTelephoneNum());
		tbx030.setText(e.getMobileNum());
		fac.setFacMob(e.getMobileNum());
		tbx031.setText(e.getFaxNum());
		fac.setFacFax(e.getFaxNum());
		tbx032.setText(e.getWebAddress());
		fac.setWebAddress(e.getWebAddress());
		}else{
			this.fac = new ProductionEnterpriseInfo();
			initComponents();
		}
	}
	
	public void onCheck$rgpBusinessScope(){
//		System.out.println("on bookmark changed " + rgpBusinessScope.getSelectedItem().getValue() + "--" + rgpBusinessScope.getSelectedIndex());
		fac.setScale(rgpBusinessScope.getSelectedIndex());
	}
	
	public void init(){
		organizationNatureArray = new ArrayList<String>();
		OrganizationNature[] org = OrganizationNature.values();
		for(OrganizationNature str : org){
			organizationNatureArray.add(str.toString());
		}
	}
	
	public void onClick$btnSubmit(){
		validateData();
		setState();
		setDistrict();
		if("".equals(fac.getId()) || null == fac.getId())
			if(ProductionEnterpriseInfoServiceUtil.isProExist(fac.getName()))
				throw new WrongValueException(tbx000,"生产厂已经存在，请检查输入信息!");
		
		try{
			if(Messagebox.YES == Messagebox.show("确认添加?","操作提示",Messagebox.YES|Messagebox.NO,Messagebox.QUESTION)){
				fac.setStatus(0);
				fac.setEnterprise(EnterpriseInfoServiceUtil.getCurrentEnterprise());
				ProductionEnterpriseInfoServiceUtil.saveOrUpdate(fac);
				Messagebox.show("修改成功!","提示",Messagebox.OK,Messagebox.INFORMATION);
			}
			
			frmProductionEnter.detach();
			//Window objWindow = (Window) Executions.createComponents("productionEnterprise-manage.zul", frmEnterpriseOuter2, null);
			//objWindow.doEmbedded();
				
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void onSelect$cbxState() {
		StateInfo state = (StateInfo) cbxState.getSelectedItem().getAttribute(
				"stateInfo");
		if (!state.getName().equals("中国")) {
			hLocation.setVisible(false);
			tbxLocation.setVisible(true);
		} else {
			tbxLocation.setVisible(false);
			hLocation.setVisible(true);
			pList = LocationInfoServiceUtil.getProvinceList();
			ListModelList pListModel = new ListModelList(pList);
			cbxProvince.setModel(pListModel);
		}
	}
	
	public void onSelect$cbxProvince() {
		LocationInfo province = (LocationInfo) cbxProvince.getSelectedItem()
				.getAttribute("province");
		List<LocationInfo> cList = LocationInfoServiceUtil.getCityList(province);
		ListModelList cityListModel = new ListModelList(cList);
		cbxCity.setModel(cityListModel);
	}
	
	public void onClick$btnSendEvent(){
		validateData();
		setState();
		setDistrict();
		Events.postEvent(EnterpriseinfoWindow.onAddObject, enterpriseCreateWindow, fac);
//		frmERF.detach();
		onCleanComponent();
	}
	
	private void onCleanComponent() {
		setFac(new ProductionEnterpriseInfo());
		initComponents();
		cbxProvince.setText("—选择省—");
		cbxCity.setText("—选择市—");
		cbxState.setText("—选择国家—");
		tbxLocation.setVisible(false);
		hLocation.setVisible(true);
	}
	
	private void initComponents() {
		List<Textbox> tbxs = new ArrayList<Textbox>();
		tbxs.add(tbx000);
		tbxs.add(tbx001);
		tbxs.add(tbx002);
		tbxs.add(tbx003);
		tbxs.add(tbx004);
		tbxs.add(tbx005);
		tbxs.add(tbx006);
		tbxs.add(tbx007);
		tbxs.add(tbx008);
		tbxs.add(tbx009);
		tbxs.add(tbx010);
		tbxs.add(tbx011);
		tbxs.add(tbx012);
		tbxs.add(tbx013);
		tbxs.add(tbx014);
		tbxs.add(tbx015);
		tbxs.add(tbx016);
		tbxs.add(tbx017);
		tbxs.add(tbx018);
		tbxs.add(tbx019);
		tbxs.add(tbx020);
		tbxs.add(tbx021);
		tbxs.add(tbx022);
		tbxs.add(tbx023);
		tbxs.add(tbx024);
		tbxs.add(tbx025);
		tbxs.add(tbx026);
		tbxs.add(tbx027);
		tbxs.add(tbx028);
		tbxs.add(tbx029);
		tbxs.add(tbx030);
		tbxs.add(tbx031);
		tbxs.add(tbx032);
		tbxs.add(tbx033);
		tbxs.add(tbxLocation);
		
		for(Textbox tbx:tbxs){
			Constraint constraint = tbx.getConstraint();
			tbx.setConstraint("");
			tbx.setValue("");
			tbx.setConstraint(constraint);
		}
	}
	
	public void onClick$btnGoback(){
		self.detach();		
	}


	public void setFac(ProductionEnterpriseInfo fac) {
		this.fac = fac;
	}


	public ProductionEnterpriseInfo getFac() {
		return fac;
	}
	
	public void setState(){
		fac.setFacCountry(cbxState.getSelectedItem().getLabel().toString());
	}
	
	public void setDistrict(){
		String location = "";
		if(tbxLocation.isVisible())
			location = tbxLocation.getText().toString();
		else
			location = cbxProvince.getSelectedItem().getLabel().toString() + "" + cbxCity.getSelectedItem().getLabel().toString(); 
		fac.setFacDistrict(location);
	}
	
	void validateData() {
		tbx000.getValue();
		tbx001.getValue();
		tbx002.getValue();
		cbxOrganizationNature.getValue();
		if(null == rgpBusinessScope.getSelectedItem())
			throw new WrongValueException(rgpBusinessScope,"请选择企业规模!");
		tbx003.getValue();
		tbx004.getValue();
		tbx005.getValue();
		tbx006.getValue();
		tbx007.getValue();
		tbx008.getValue();
		tbx009.getValue();
		tbx010.getValue();
		tbx011.getValue();
		tbx012.getValue();
		tbx013.getValue();
		tbx014.getValue();
		tbx015.getValue();
		tbx016.getValue();
		tbx017.getValue();
		tbx018.getValue();
		if (cbxState.getValue().contains("—")) {
			throw new WrongValueException(cbxState, "请选择国家！");
		}
		if (!tbxLocation.isVisible()) {
			if (cbxProvince.getValue().contains("—")
					|| cbxCity.getValue().contains("—")) {
				throw new WrongValueException(cbxCity, "请选择省和市！");
			}
		} else {
			tbxLocation.getValue();
			InputValidateUtil.validate(InputValidateUtil.LENGTH_VARCHAR,
					tbxLocation);
		}		
		tbx019.getValue();
		tbx020.getValue();
		tbx021.getValue();
		tbx022.getValue();
		tbx023.getValue();
		tbx024.getValue();
		tbx025.getValue();
		tbx026.getValue();
		tbx027.getValue();
		tbx028.getValue();
		tbx029.getValue();
		tbx030.getValue();
		tbx031.getValue();
		tbx032.getValue();
		tbx033.getValue();
	}

	public List<String> getOrganizationNatureArray() {
		return organizationNatureArray;
	}

	public void setOrganizationNatureArray(List<String> organizationNatureArray) {
		this.organizationNatureArray = organizationNatureArray;
	}

}
