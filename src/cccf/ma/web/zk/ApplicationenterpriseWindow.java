package cccf.ma.web.zk;

import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.zkoss.zk.ui.*;
import org.zkoss.zul.*;
import cccf.ma.model.*;
import cccf.ma.service.*;
import cccf.myenum.ApplicationStatusUtil;

@SuppressWarnings({ "unchecked", "serial" })
public class ApplicationenterpriseWindow
		extends Window
{
	Long					taskInstanceId;
	String					processId	= "0";
	String					userId;
	HashMap					params		= new HashMap();
	EnterpriseInfo			enterprise;
	public ApplicationInfo	applicationInfo;
	public ApplicationInfo getApplicationInfo()
	{
		return applicationInfo;
	}
	public void setApplicationInfo(ApplicationInfo entity)
	{
		this.applicationInfo = entity;
	}
	public void onCreate()
	{
		enterprise = EnterpriseInfoServiceUtil.getCurrentEnterprise();
		// 企业未激活
		if (enterprise.getStatus() == 0)
		{
			Button btModify = (Button) getFellow("btModify");
			btModify.setDisabled(true);
			Button btDetail = (Button) getFellow("btDetail");
			btDetail.setDisabled(true);
			Button btAdd = (Button) getFellow("btAdd");
			btAdd.setDisabled(true);
			Button btDel = (Button) getFellow("btDel");
			btDel.setDisabled(true);
		}
	}
	public List<ApplicationInfo> getObjList()
	{
		Listbox listbox = getListbox();
		List<ApplicationInfo> list = (List<ApplicationInfo>) listbox.getModel();
		return list;
	}
	public Listbox getListbox()
	{
		return (Listbox) this.getFellow("applicationInfoListbox");
	}
	// public void onAdd() {
	// params.put("application", null);
	// params.put("cmd", "add");
	// openEditWindow(params);
	// }
	// ////////////////////////修改的代码///////////////////////////////////////////////////////////////////////////////////////////
	public void onAdd()
			throws InterruptedException
	{
		Window objWindow = (Window) Executions.createComponents("/SysForm/application/product_accreditation_apply.zul", null, params);
		objWindow.doModal();
	}
	public void openEmbeddedWindow(Map params)
			throws InterruptedException
	{
		Tabpanel tp = (Tabpanel) this.getParent().getParent().getParent();// 得到装载窗口的Tabpannel
		Tabs tbs = tp.getTabbox().getTabs();
		List<Tab> tab = tbs.getChildren();
		int i = -1;
		for (Tab t : tab)
		{// 判断窗口是否已经打开
			if (t.getLabel().equals("产品认证申请"))
			{
				i = t.getIndex();// 获得已打开的窗口所在的位置
				break;
			}
		}
		if (i == -1)// 如果该窗口还没有打开
		{
			Tab t = new Tab();
			t.setLabel("产品认证申请");
			t.setClosable(true);
			tbs.appendChild(t);
			Tabpanels tps = tp.getTabbox().getTabpanels();
			Tabpanel newtp = new Tabpanel();
			tps.appendChild(newtp);
			Window objWindow = (Window) Executions.createComponents("/SysForm/application/product_accreditation_apply.zul", newtp, params);
			objWindow.setVflex("true");
			objWindow.setHeight("true");
			tp.getTabbox().setSelectedTab(t);
		} else
		// 否则,窗口已经打开的情况下,
		{
			if (Messagebox.show("该窗口已经打开,是否覆盖已打开的窗口", "系统提示?", Messagebox.OK | Messagebox.NO, Messagebox.QUESTION) == Messagebox.OK)
			{
				tp.getTabbox().setSelectedIndex(i);// 选中该窗口
				Tabpanel seltp = tp.getTabbox().getSelectedPanel();
				if (seltp != null)
					seltp.removeChild(seltp.getFirstChild());
				seltp.removeChild(seltp.getFirstChild());// 为tp移除原来的窗口
				Window objWindow = (Window) Executions.createComponents("/SysForm/application/product_accreditation_apply.zul", seltp, params);
				objWindow.setParent(seltp);
				objWindow.setVflex("true");
				objWindow.setHeight("true");
			} else
				// 否,直接选择已打开的窗口
				tp.getTabbox().setSelectedIndex(i);
		}
	}
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void openEditWindow(Map params0)
	{
		params.put("parentListbox", getListbox());
		Window objWindow = (Window) Executions.createComponents("application-apply.zul", null, params);
		try
		{
			objWindow.doModal();
			refreshUsersListbox();
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void onDelete()
			throws InterruptedException
	{
		if (Messagebox.show("是否要删除记录?", "删除?", Messagebox.OK | Messagebox.NO, Messagebox.QUESTION) == Messagebox.OK)
		{
			Listbox listbox = getListbox();
			Set selectItems = listbox.getSelectedItems();
			Iterator iterator = selectItems.iterator();
			// String value = "";
			while (iterator.hasNext())
			{
				Listitem listitem = (Listitem) iterator.next();
				String id = listitem.getValue().toString();
				ApplicationInfo delcom = ApplicationInfoServiceUtil.getByPrimaryKey(id);
				ApplicationInfoServiceUtil.delete(delcom);
			}
			if (selectItems.size() > 0)
			{
				refreshUsersListbox();
			}
		}
	}
	public void onEdit()
	{
		if (applicationInfo == null)
		{
			try
			{
				Messagebox.show("请选择一条记录!", "操作提示", Messagebox.OK, Messagebox.ERROR);
				return;
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		params.put("application", getApplicationInfo());
		params.put("cmd", "edit");
		openEditWindow(params);
	}
	public void refreshUsersListbox()
	{
		List<ApplicationInfo> list = getObjList();
		list.clear();
		System.out.print(enterprise.getId());
		list.addAll(ApplicationInfoServiceUtil.getApplicationByEnterprise(enterprise.getId()));
	}
	public void onSearch()
	{
		String qstr = "";
		Hbox hbox = (Hbox) this.getFellow("querybox");
		Textbox production = (Textbox) hbox.getFellow("production");
		if (production.getText() != "" && production.getText() != null)
		{
			qstr = qstr + " and production.null like ";
			qstr = qstr + "'%" + production.getText() + "%'";
		}
		Textbox stauts = (Textbox) hbox.getFellow("stauts");
		if (stauts.getText() != "" && stauts.getText() != null)
		{
			qstr = qstr + " and stauts=";
			qstr = qstr + stauts.getText();
		}
		Textbox applyDate = (Textbox) hbox.getFellow("applyDate");
		if (applyDate.getText() != "" && applyDate.getText() != null)
		{
			qstr = qstr + " and applyDate like ";
			qstr = qstr + "'%" + applyDate.getText() + "%'";
		}
		if (qstr != "")
		{
			qstr = qstr.replaceFirst("and", "");
			qstr = "from ApplicationInfo where " + qstr;
		} else
		{
			qstr = "from ApplicationInfo";
		}
		List<ApplicationInfo> searchResult = ApplicationInfoServiceUtil.findByQuery(qstr);
		List<ApplicationInfo> list = getObjList();
		list.clear();
		list.addAll(searchResult);
	}
	public void onListitemSelect()
	{
		// 可编辑状态
		boolean editStatus = ApplicationStatusUtil.isEditEnabled(applicationInfo.getStauts(), applicationInfo.getStauts0());
		Button btModify = (Button) this.getFellow("btModify");
		Button btDel = (Button) this.getFellow("btDel");
		btModify.setDisabled(!editStatus);
		btDel.setDisabled(!editStatus);
	}
}
