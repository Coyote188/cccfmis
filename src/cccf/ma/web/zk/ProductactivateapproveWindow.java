package cccf.ma.web.zk;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import openjframework.model.MessageInfo;
import openjframework.model.UserInfo;
import openjframework.service.MessageInfoServiceUtil;
import openjframework.service.UserInfoServiceUtil;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;
import org.zkoss.zul.api.Listcell;
import cccf.ma.model.EnterpriseInfo;
import cccf.ma.model.EnterpriseOwnActivatedProductListInfo;
import cccf.ma.service.EnterpriseOwnActivatedProductListInfoServiceUtil;

public class ProductactivateapproveWindow
		extends Window
{
	Long											taskInstanceId;
	String											processId	= "0";
	String											userId;
	HashMap											params		= new HashMap();
	Textbox											description;
	UserInfo										user;
	MessageInfo										activateMsg;
	public EnterpriseOwnActivatedProductListInfo	enterpriseOwnActivatedProductListInfo;
	public EnterpriseOwnActivatedProductListInfo getEnterpriseOwnActivatedProductListInfo()
	{
		return enterpriseOwnActivatedProductListInfo;
	}
	public void setEnterpriseOwnActivatedProductListInfo(EnterpriseOwnActivatedProductListInfo entity)
	{
		this.enterpriseOwnActivatedProductListInfo = entity;
	}
	public void onCreate()
	{
		description = (Textbox) getFellow("description");
		user = UserInfoServiceUtil.getCurrentLoginUser();
		List<EnterpriseOwnActivatedProductListInfo> list = EnterpriseOwnActivatedProductListInfoServiceUtil.findNotActivateAll();
		if (list != null && !list.isEmpty())
		{
			setEnterpriseOwnActivatedProductListInfo(list.iterator().next());
		}
	}
	public List<EnterpriseOwnActivatedProductListInfo> getObjList()
	{
		Listbox listbox = getListbox();
		@SuppressWarnings("unchecked")
		List<EnterpriseOwnActivatedProductListInfo> list = (List<EnterpriseOwnActivatedProductListInfo>) listbox.getModel();
		return list;
	}
	public Listbox getListbox()
	{
		return (Listbox) this.getFellow("enterpriseOwnActivatedProductListInfoListbox");
	}
	public void refreshUsersListbox()
	{
		List<EnterpriseOwnActivatedProductListInfo> list = getObjList();
		list.clear();
		list.addAll(EnterpriseOwnActivatedProductListInfoServiceUtil.findNotActivateAll());
	}
	// 选中多个激活
	public void onActivate()
			throws InterruptedException
	{
		validateData();
		int count = getListbox().getSelectedCount();
		if (count == 0)
			Messagebox.show("请至少选择一个条目!", "提示", Messagebox.OK, Messagebox.INFORMATION);
		else
		{
			if (Messagebox.show("您选中了" + count + "条产品,是否要全部激活?", "激活产品", Messagebox.OK | Messagebox.NO, Messagebox.QUESTION) == Messagebox.OK)
			{
				Listbox listbox = getListbox();
				Set selectItems = listbox.getSelectedItems();
				Iterator iterator = selectItems.iterator();
				while (iterator.hasNext())
				{
					Listitem selectdItem = (Listitem) iterator.next();
					EnterpriseOwnActivatedProductListInfo eoap = (EnterpriseOwnActivatedProductListInfo) selectdItem.getValue();
					eoap.setActivatedate(new Date());
					eoap.setActivateStatus(1);
					eoap.setActivateuser(user);
					eoap.setDescription(description.getValue());
					EnterpriseOwnActivatedProductListInfoServiceUtil.update(eoap);
					activateMsg = new MessageInfo();
					activateMsg.setSubject("产品激活");
					activateMsg.setContent("您申请的产品:" + eoap.getProduct().getProductName() + "已激活!");
					activateMsg.setReceiveUser(eoap.getEnterprise().getAccount());
					activateMsg.setType(0);
					MessageInfoServiceUtil.sendMessageToDesktop(activateMsg);
				}
				Messagebox.show(count + "条产品产品已成功激活", "提示", Messagebox.OK, Messagebox.INFORMATION);
				if (selectItems.size() > 0)
				{
					refreshUsersListbox();
				}
			}
		}
	}
	// 单个激活
	public void onSingleActivate(ForwardEvent event)
			throws InterruptedException
	{
		validateData();
		if (Messagebox.show("您选中了1条产品,是否要激活?", "激活产品", Messagebox.OK | Messagebox.NO, Messagebox.QUESTION) == Messagebox.OK)
		{
			Listcell cell = (Listcell) event.getOrigin().getTarget();
			EnterpriseOwnActivatedProductListInfo eoap = (EnterpriseOwnActivatedProductListInfo) cell.getAttribute("eoap");
			eoap.setActivatedate(new Date());
			eoap.setActivateStatus(1);
			eoap.setActivateuser(user);
			eoap.setDescription(description.getValue());
			EnterpriseOwnActivatedProductListInfoServiceUtil.update(eoap);
			Messagebox.show("产品已成功激活", "提示", Messagebox.OK, Messagebox.INFORMATION);
			refreshUsersListbox();
			activateMsg = new MessageInfo();
			activateMsg.setSubject("产品激活");
			activateMsg.setContent("您申请的产品:" + eoap.getProduct().getProductName() + "已激活!");
			activateMsg.setReceiveUser(eoap.getEnterprise().getAccount());
			activateMsg.setType(0);
			MessageInfoServiceUtil.sendMessageToDesktop(activateMsg);
		}
	}
	void validateData()
	{
		description.getValue();
	}
	public void onEnterpriseInfoDetail(ForwardEvent event)
			throws SuspendNotAllowedException, InterruptedException
	{
		Toolbarbutton tool = (Toolbarbutton) event.getOrigin().getTarget();
		EnterpriseOwnActivatedProductListInfo eoap = (EnterpriseOwnActivatedProductListInfo) tool.getAttribute("eoap");
		EnterpriseInfo enterprise = eoap.getEnterprise();
		Map params = new HashMap();
		params.put("enterprise", enterprise);
		Window frmManufature = (Window) Executions.createComponents("/views/EnterpriseAdmin/productdetailenterpriseInfo.zul", null, params);
		frmManufature.doModal();
	}
}
