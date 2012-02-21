package cccf.ma.web.zk;

import java.awt.Checkbox;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import cccf.ma.model.EnterpriseInfo;
import cccf.ma.model.LocationInfo;
import cccf.ma.model.ManufactureInfo;
import cccf.ma.model.StateInfo;
import cccf.ma.service.EnterpriseInfoServiceUtil;
import cccf.ma.service.LocationInfoServiceUtil;
import cccf.ma.service.ManufactureInfoServiceUtil;
import cccf.ma.service.StateInfoServiceUtil;
import cccf.myenum.OrganizationNature;

public class ManuEditController
		extends GenericForwardComposer
{
	public static final String	ON_SETSAVETOENTERPRISE	= "onSetProperty";
	private ManufactureInfo		manu;
	public List<StateInfo>		sList;
	public List<LocationInfo>	pList;
	public List<LocationInfo>	cList;
	private boolean				isNewObj;
	private Combobox			cbxProvince, cbxCity, cbxState, cbxOrganizationNature;
	private Textbox				tbxLocation;
	private Textbox				tbx001, tbx002, tbx003, tbx004, tbx005, tbx006, tbx007, tbx008, tbx009, tbx010, tbx011, tbx012, tbx013, tbx014, tbx015, tbx016, tbx017, tbx018, tbx019, tbx020, tbx021, tbx022, tbx023, tbx024;
	private Hbox				hLocation;
	private Window				frmManufature, enterpriseCreateWindow;
	private Radiogroup			rgpBusinessScope;
	private List<String>		organizationNatureArray;
	Map							param					= Executions.getCurrent().getArg();
	@Override
	public void doAfterCompose(Component comp)
			throws Exception
	{
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		if (null != param.get("manu"))
		{
			setManu((ManufactureInfo) param.get("manu"));
			isNewObj = false;
		} else
		{
			setManu(new ManufactureInfo());
			isNewObj = true;
		}
		init();
		sList = StateInfoServiceUtil.getAll();
		pList = LocationInfoServiceUtil.getProvinceList();
	}
	public void onCheck$rgpBusinessScope()
	{
		// System.out.println("on bookmark changed " +
		// rgpBusinessScope.getSelectedItem().getValue() + "--" +
		// rgpBusinessScope.getSelectedIndex());
		manu.setScale(rgpBusinessScope.getSelectedIndex());
	}
	org.zkoss.zul.Checkbox	btnSetSameOfEnterprise;
	public void onCheck$btnSetSameOfEnterprise()
	{
		Events.sendEvent(EnterpriseinfoWindow.ON_SENDDATA, enterpriseCreateWindow, null);
	}
	public void onSetProperty(Event ent)
	{
		if (btnSetSameOfEnterprise.isChecked())
		{
			EnterpriseInfo e = (EnterpriseInfo) ent.getData();
			tbx001.setText(e.getName());
			manu.setName(e.getName());
			tbx002.setText(e.getNameEn());
			manu.setManuNameEn(e.getNameEn());
			tbx003.setText(e.getOrganizationCode());
			manu.setManucode(e.getOrganizationCode());
			cbxOrganizationNature.setText(e.getOrganizationNature());
			manu.setOrganizationNature(e.getOrganizationNature());
			rgpBusinessScope.setSelectedIndex(e.getScale());
			manu.setScale(e.getScale());
			tbx004.setText(e.getLegalPerson());
			manu.setManuLegalPerson(e.getLegalPerson());
			tbx005.setText(e.getLegalPersonEn());
			manu.setLegalPersonEn(e.getLegalPersonEn());
			tbx006.setText(e.getContactAddForLegal());
			manu.setContactAddForLegal(e.getContactAddForLegal());
			tbx007.setText(e.getContactAddForLegalEn());
			manu.setContactAddForLegalEn(e.getContactAddForLegalEn());
			tbx008.setText(e.getTelForLegal());
			manu.setTelForLegal(e.getTelForLegal());
			tbx009.setText(e.getMobileForLegal());
			manu.setMobileForLegal(e.getMobileForLegal());
			tbx010.setText(e.getRegisteredAddress());
			manu.setRegisteredAddress(e.getRegisteredAddress());
			tbx011.setText(e.getRegisteredAddressEn());
			manu.setRegisteredAddressEn(e.getRegisteredAddressEn());
			tbx012.setText(e.getRegisteredCapital());
			manu.setRegisteredCapital(e.getRegisteredCapital());
			tbx013.setText(e.getBusinessScope());
			manu.setManuWorkRange(e.getBusinessScope());
			cbxState.setText(e.getState());
			manu.setManuCountry(e.getState());
			hLocation.setVisible(false);
			tbxLocation.setConstraint("");
			tbxLocation.setText(e.getLocation());
			tbxLocation.setVisible(true);
			manu.setManuDistrict(e.getLocation());
			tbx014.setText(e.getContactAddress());
			manu.setContactAddress(e.getContactAddress());
			tbx015.setText(e.getContactAddressEn());
			manu.setContactAddressEn(e.getContactAddressEn());
			tbx016.setText(e.getPostalcode());
			manu.setManuZip(e.getPostalcode());
			tbx017.setText(e.getEmailAddress());
			manu.setManuEmail(e.getEmailAddress());
			tbx018.setText(e.getContactPerson());
			manu.setManuLinkMan(e.getContactPerson());
			tbx019.setText(e.getContactPersonEn());
			manu.setManuLinkManEn(e.getContactPersonEn());
			tbx020.setText(e.getTelephoneNum());
			manu.setManuTel(e.getTelephoneNum());
			tbx021.setText(e.getMobileNum());
			manu.setManuMob(e.getMobileNum());
			tbx022.setText(e.getFaxNum());
			manu.setManuFax(e.getFaxNum());
			tbx023.setText(e.getWebAddress());
			manu.setWebAddress(e.getWebAddress());
		} else
		{
			this.manu = new ManufactureInfo();
			initComponents();
		}
	}
	public void init()
	{
		organizationNatureArray = new ArrayList<String>();
		OrganizationNature[] org = OrganizationNature.values();
		for (OrganizationNature str : org)
		{
			organizationNatureArray.add(str.toString());
		}
	}
	public void onClick$btnSubmit()
			throws InterruptedException
	{
		validateData();
		setState();
		setDistrict();
		if ("".equals(manu.getId()) || null == manu.getId())
			if (ManufactureInfoServiceUtil.isManufactureExist(manu.getName()))
				throw new WrongValueException(tbx001, "制造商已经存在，请检查输入信息!");
		if (Messagebox.YES == Messagebox.show("确认添加?", "操作提示", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION))
		{
			manu.setStatus(0);
			manu.setEnterprise(EnterpriseInfoServiceUtil.getCurrentEnterprise());
			ManufactureInfoServiceUtil.saveOrUpdate(manu);
			Messagebox.show("修改成功!", "提示", Messagebox.OK, Messagebox.INFORMATION);
		}
		self.detach();
	}
	public void onClick$btnGoback()
	{
		self.detach();
	}
	public void onClick$btnSendEvent()
	{
		validateData();// add event to close the form
		setState();
		setDistrict();
		Events.postEvent(EnterpriseinfoWindow.onAddObject, enterpriseCreateWindow, manu);
		// frmERM.detach();
		onCleanComponent();
	}
	private void onCleanComponent()
	{
		setManu(new ManufactureInfo());
		initComponents();
		cbxProvince.setText("");
		cbxCity.setText("");
		cbxState.setText("");
		tbxLocation.setVisible(false);
		hLocation.setVisible(true);
	}
	private void initComponents()
	{
		List<Textbox> tbxs = new ArrayList<Textbox>();
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
		tbxs.add(tbxLocation);
		for (Textbox tbx : tbxs)
		{
			Constraint constraint = tbx.getConstraint();
			tbx.setConstraint("");
			tbx.setValue("");
			tbx.setConstraint(constraint);
		}
		// onCleanComponent();
	}
	public void onSelect$cbxState()
	{
		StateInfo state = (StateInfo) cbxState.getSelectedItem().getAttribute("stateInfo");
		if (!state.getName().equals("中国"))
		{
			hLocation.setVisible(false);
			tbxLocation.setVisible(true);
		} else
		{
			tbxLocation.setVisible(false);
			hLocation.setVisible(true);
			pList = LocationInfoServiceUtil.getProvinceList();
			ListModelList pListModel = new ListModelList(pList);
			cbxProvince.setModel(pListModel);
		}
	}
	public void onSelect$cbxProvince()
	{
		LocationInfo province = (LocationInfo) cbxProvince.getSelectedItem().getAttribute("province");
		List<LocationInfo> cList = LocationInfoServiceUtil.getCityList(province);
		ListModelList cityListModel = new ListModelList(cList);
		cbxCity.setModel(cityListModel);
	}
	public void setManu(ManufactureInfo manu)
	{
		this.manu = manu;
	}
	public ManufactureInfo getManu()
	{
		return manu;
	}
	public List<StateInfo> getSList()
	{
		return sList;
	}
	public void setSList(List<StateInfo> list)
	{
		sList = list;
	}
	public List<LocationInfo> getPList()
	{
		return pList;
	}
	public void setPList(List<LocationInfo> list)
	{
		pList = list;
	}
	public List<LocationInfo> getCList()
	{
		return cList;
	}
	public void setCList(List<LocationInfo> list)
	{
		cList = list;
	}
	public void setState()
	{
		manu.setManuCountry(cbxState.getSelectedItem().getLabel().toString());
	}
	public void setDistrict()
	{
		String location = "";
		if (tbxLocation.isVisible())
			location = tbxLocation.getText().toString();
		else
			location = cbxProvince.getValue() + "" + cbxCity.getValue();
		manu.setManuDistrict(location);
	}
	void validateData()
	{
		tbx001.getValue();
		tbx002.getValue();
		tbx003.getValue();
		cbxOrganizationNature.getValue();
		if (null == rgpBusinessScope.getSelectedItem())
			throw new WrongValueException(rgpBusinessScope, "必选项,不允许为空!");
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
		if (cbxState.getValue() == "")
		{
			throw new WrongValueException(cbxState, "请选择国家！");
		}
		if (!tbxLocation.isVisible())
		{
			if (cbxCity.getValue() == null || cbxCity.getValue() == "")
			{
				throw new WrongValueException(cbxCity, "请选择省和市！");
			}
		} else
		{
			tbxLocation.getValue();
			InputValidateUtil.validate(InputValidateUtil.LENGTH_VARCHAR, tbxLocation);
		}
		tbx014.getValue();
		tbx015.getValue();
		tbx016.getValue();
		tbx017.getValue();
		tbx018.getValue();
		tbx019.getValue();
		tbx020.getValue();
		tbx021.getValue();
		tbx022.getValue();
		tbx023.getValue();
		tbx024.getValue();
	}
	public List<String> getOrganizationNatureArray()
	{
		return organizationNatureArray;
	}
	public void setOrganizationNatureArray(List<String> organizationNatureArray)
	{
		this.organizationNatureArray = organizationNatureArray;
	}
}
