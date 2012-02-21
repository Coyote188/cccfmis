package cccf.ma.web.zk;

import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.RowRendererExt;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;
import cccf.ma.model.EnterpriseInfo;
import cccf.ma.model.LocationInfo;
import cccf.ma.model.ManufactureInfo;
import cccf.ma.model.ProductionEnterpriseInfo;
import cccf.ma.model.StateInfo;
import cccf.ma.service.EnterpriseInfoServiceUtil;
import cccf.ma.service.LocationInfoServiceUtil;
import cccf.ma.service.ProductionEnterpriseInfoServiceUtil;
import cccf.ma.service.StateInfoServiceUtil;
import cccf.ma.web.zk.override.EditableDiv;
import cccf.ma.web.zk.override.EditableRow;

@SuppressWarnings({ "serial", "unchecked" })
public class ProductionEnterpriseController
		extends GenericForwardComposer
{
	private List<ProductionEnterpriseInfo>	proList;
	private EnterpriseInfo					enterprise;
	public ProductionEnterpriseInfo			pro;
	private Combobox						cbxProvince, cbxState, cbxCity;
	private Grid							grdPro, grdAddItem;
	private Textbox							tbxLocation;
	private Textbox							tbxName;
	private Button							btnOnCreate;
	private Window							frmEnterpriseOuter2;
	@Override
	public void doAfterCompose(Component comp)
			throws Exception
	{
		super.doAfterCompose(comp);
		enterprise = EnterpriseInfoServiceUtil.getCurrentEnterprise();
		proList = ProductionEnterpriseInfoServiceUtil.getProductionEnterprise(enterprise);
	}
	private void load()
	{
		proList = ProductionEnterpriseInfoServiceUtil.getProductionEnterprise(enterprise);
		ListModel model = new SimpleListModel(proList);
		grdPro.setModel(model);
	}
	public boolean onSave(ProductionEnterpriseInfo pro2)
			throws InterruptedException
	{
		int reply = Messagebox.show("是否确认保存修改?", "警告", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		if (reply == Messagebox.YES)
		{
			pro2.setStatus(0);
			pro2.setEnterprise(enterprise);
			ProductionEnterpriseInfoServiceUtil.saveOrUpdate(pro2);
			return true;
		} else
		{
			return false;
		}
	}
	public void onClick$btnOnSave()
	{
		validateData();
		pro = new ProductionEnterpriseInfo();
		pro.setStatus(0);
		pro.setContactAddress(tbxLocation.getText());
		pro.setName(tbxName.getText());
		try
		{
			if (ProductionEnterpriseInfoServiceUtil.isProExist(pro.getName()))
				throw new WrongValueException(tbxName, "生产企业名称已经存在，请核实后重新输入!");
			onSave(pro);
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
	Vlayout	editbox, grdProbox;
	public void onClick$btnAdd()
			throws SuspendNotAllowedException, InterruptedException
	{
		grdProbox.setVisible(false);
		editbox.getChildren().clear();
		Executions.createComponents("/SysForm/pe-edit.zul", editbox, null);
		// objWindow.doModal();
	}
	public void onClick$btnDetial(ForwardEvent event)
			throws SuspendNotAllowedException, InterruptedException
	{
		ProductionEnterpriseInfo manu = (ProductionEnterpriseInfo) event.getOrigin().getTarget().getAttribute("manu");
		param.put("manu", manu);
		editbox.getChildren().clear();
		Executions.createComponents("/SysForm/pe-edit.zul", editbox, param);
	}
	public void onClick$btnRemove(ForwardEvent event)
			throws InterruptedException
	{
		ProductionEnterpriseInfo pro = (ProductionEnterpriseInfo) event.getOrigin().getTarget().getAttribute("manu");
		int reply = Messagebox.show("是否确认删除:" + pro.getName() + "?", "警告", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		if (reply == Messagebox.YES)
		{
			ProductionEnterpriseInfoServiceUtil.delete(pro);
			load();
		} else
		{
			return;
		}
	}
	public ProductionEnterpriseInfo getPro()
	{
		return pro;
	}
	public void setPro(ProductionEnterpriseInfo pro)
	{
		this.pro = pro;
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
	public List<ProductionEnterpriseInfo> getProList()
	{
		return proList;
	}
	public void setProList(List<ProductionEnterpriseInfo> proList)
	{
		this.proList = proList;
	}
}
