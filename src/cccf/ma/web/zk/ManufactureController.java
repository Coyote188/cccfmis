package cccf.ma.web.zk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;
import cccf.ma.model.EnterpriseInfo;
import cccf.ma.model.LocationInfo;
import cccf.ma.model.ManufactureInfo;
import cccf.ma.model.StateInfo;
import cccf.ma.service.EnterpriseInfoServiceUtil;
import cccf.ma.service.LocationInfoServiceUtil;
import cccf.ma.service.ManufactureInfoServiceUtil;
import cccf.ma.service.StateInfoServiceUtil;

@SuppressWarnings({ "serial", "unchecked" })
public class ManufactureController
		extends GenericForwardComposer
{
	private List<ManufactureInfo>	manufactureList;
	private EnterpriseInfo			enterprise;
	public ManufactureInfo			manufacture;
	public List<StateInfo>			sList;
	public List<LocationInfo>		pList;
	public List<LocationInfo>		cList;
	Map								param	= new HashMap();
	private Grid					grdPro;
	private Textbox					tbxLocation;
	private Textbox					tbxName;
	@Override
	public void doAfterCompose(Component comp)
			throws Exception
	{
		super.doAfterCompose(comp);
		// load();
		enterprise = EnterpriseInfoServiceUtil.getCurrentEnterprise();
		manufactureList = ManufactureInfoServiceUtil.getManufactures(enterprise);
		sList = StateInfoServiceUtil.getAll();
		pList = LocationInfoServiceUtil.getProvinceList();
	}
	private void load()
	{
		manufactureList = ManufactureInfoServiceUtil.getManufactures(enterprise);
		ListModel model = new SimpleListModel(manufactureList);
		grdPro.setModel(model);
	}
	Vlayout	editbox;
	public void onClick$btnAdd()
			throws SuspendNotAllowedException, InterruptedException
	{
		editbox.getChildren().clear();
		Executions.createComponents("/SysForm/manu-edit.zul", editbox, null);
	}
	public boolean onSave(ManufactureInfo manufacture2)
			throws InterruptedException
	{
		int reply = Messagebox.show("是否确认保存修改?", "警告", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		if (reply == Messagebox.YES)
		{
			manufacture2.setStatus(0);
			manufacture2.setEnterprise(enterprise);
			ManufactureInfoServiceUtil.saveOrUpdate(manufacture2);
			return true;
		} else
		{
			return false;
		}
	}
	public void onClick$btnOnSave()
	{
		validateData();
		manufacture = new ManufactureInfo();
		manufacture.setStatus(0);
		manufacture.setContactAddress(tbxLocation.getText());
		manufacture.setName(tbxName.getText());
		try
		{
			if (ManufactureInfoServiceUtil.isManufactureExist(manufacture.getName()))
				throw new WrongValueException(tbxName, "制造商名称已经存在，请核实后重新输入!");
			onSave(manufacture);
			load();
			tbxLocation.setConstraint("");
			tbxName.setConstraint("");
			tbxLocation.setText("");
			tbxName.setText("");
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	public void onClick$btnDetial(ForwardEvent event)
	{
		ManufactureInfo manu = (ManufactureInfo) event.getOrigin().getTarget().getAttribute("manu");
		param.put("manu", manu);
		editbox.getChildren().clear();
		Executions.createComponents("/SysForm/manu-edit.zul", editbox, param);
	}
	public void onClick$btnRemove(ForwardEvent event)
			throws InterruptedException
	{
		ManufactureInfo manu = (ManufactureInfo) event.getOrigin().getTarget().getAttribute("manu");
		int reply = Messagebox.show("是否确认删除:" + manu.getName() + "?", "操作提示", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		if (reply == Messagebox.YES)
		{
			ManufactureInfoServiceUtil.delete(manu);
			load();
		} else
		{
			return;
		}
	}
	public List<StateInfo> getSList()
	{
		return sList;
	}
	public void setSList(List<StateInfo> list)
	{
		sList = list;
	}
	public ManufactureInfo getPro()
	{
		return manufacture;
	}
	public void setPro(ManufactureInfo manufacture)
	{
		this.manufacture = manufacture;
	}
	void validateData()
	{
		tbxLocation.setConstraint("no empty:通讯地址不能为空");
		tbxLocation.getValue();
		InputValidateUtil.validate(InputValidateUtil.LENGTH_ENTERPRISENAME, tbxLocation);
		tbxName.setConstraint("no empty:名称不能为空");
		tbxName.getValue();
		InputValidateUtil.validate(InputValidateUtil.LENGTH_VARCHAR, tbxName);
	}
	public List<ManufactureInfo> getManufactureList()
	{
		return manufactureList;
	}
	public void setManufactureList(List<ManufactureInfo> manufactureList)
	{
		this.manufactureList = manufactureList;
	}
}
