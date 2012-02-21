package cccf.ma.web.zk;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import openjframework.model.MessageInfo;
import openjframework.myenum.MessageEnum;
import openjframework.service.MessageInfoServiceUtil;
import openjframework.service.UserInfoServiceUtil;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;
import cccf.ma.model.EnterpriseInfo;
import cccf.ma.service.EnterpriseInfoServiceUtil;

@SuppressWarnings({ "serial", "unchecked", "unused" })
public class EnterpriseactiveWindow
		extends Window
{
	public final static String		ON_ACTIVE	= "onCreate";
	public final static String		ON_REFUSED	= "onCreate";
	public final static String		ON_SENDMSG	= "onSendMsg";
	Long							taskInstanceId;
	String							processId	= "0";
	String							userId;
	@SuppressWarnings("rawtypes")
	HashMap							params		= new HashMap();
	private Grid					grdEnterpriseActive;
	private Window					enterpriseactiveWindow;
	private Button					btnAutoActive;
	private List<EnterpriseInfo>	list;
	public EnterpriseInfo			enterpriseInfo;
	public EnterpriseInfo getEnterpriseInfo()
	{
		return enterpriseInfo;
	}
	public void setEnterpriseInfo(EnterpriseInfo entity)
	{
		this.enterpriseInfo = entity;
	}
	public void onCreate()
	{
		list = EnterpriseInfoServiceUtil.findInactiveEnterpriseByPermission();
		Components.wireVariables(this, this);
		this.addEventListener(this.ON_SENDMSG, new EventListener()
		{
			@Override
			public void onEvent(Event arg0)
					throws Exception
			{
				onSendMsg(arg0);
			}
		});
		grdEnterpriseActive.setRowRenderer(new EnterpriseActiveRowRenderer());
		ListModel lModel = new SimpleListModel(list);
		grdEnterpriseActive.setModel(lModel);
		int defaultStatus = EnterpriseInfoServiceUtil.getDefaultStatus();
		if (defaultStatus == 0)
		{
			btnAutoActive.setLabel("自动激活");
		} else
		{
			btnAutoActive.setLabel("手动激活");
		}
	}
	public List<EnterpriseInfo> getObjList()
	{
		Listbox listbox = getListbox();
		List<EnterpriseInfo> list = (List<EnterpriseInfo>) listbox.getModel();
		return list;
	}
	public Listbox getListbox()
	{
		return (Listbox) this.getFellow("enterpriseInfoListbox");
	}
	private void onView(EnterpriseInfo enterprise)
			throws SuspendNotAllowedException, InterruptedException
	{
		params.put("enterprise", enterprise);
		Window frmEnterpriseInfo = (Window) Executions.createComponents("enterprise-viewer.zul", null, params);
		frmEnterpriseInfo.setParent(this);
		frmEnterpriseInfo.doModal();
	}
	private void onActive(EnterpriseInfo enterprise)
			throws InterruptedException
	{
		int reply = Messagebox.show("是否确认激活该企业", "提示", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		if (reply == Messagebox.YES)
		{
			EnterpriseInfoServiceUtil.activeEnterprise(enterprise);
			// List<ManufactureInfo> mList =
			// ManufactureInfoServiceUtil.getManufactures(enterprise);
			// List<ProductionEnterpriseInfo> pList =
			// ProductionEnterpriseInfoServiceUtil.getProductionEnterprise(enterprise);
			// if (!mList.isEmpty()&&!pList.isEmpty()) {
			// for(ManufactureInfo m : mList){
			// ManufactureInfoServiceUtil.active(m);
			// }
			// for(ProductionEnterpriseInfo m : pList){
			// ProductionEnterpriseInfoServiceUtil.active(m);
			// }
			// }
			Events.postEvent(new Event(this.ON_SENDMSG, this, enterprise));
			onCreate();
		} else
		{
			return;
		}
	}
	private void onSendMsg(Event event)
	{
		EnterpriseInfo enterprise = (EnterpriseInfo) event.getData();
		MessageInfo msg = new MessageInfo();
		msg.setSubject("企业激活成功");
		msg.setContent("企业激活已成功，您当前可以对企业相关功能进行操作。");
		msg.setType(MessageEnum.企业激活.ordinal());
		msg.setReceiveUser(enterprise.getAccount());
		msg.setSendUser(UserInfoServiceUtil.getCurrentLoginUser());
		msg.setSendDate(new Date());
		MessageInfoServiceUtil.sendMessageToDesktop(msg);
	}
	private void onRefused(Event event)
	{
		EnterpriseInfo e = (EnterpriseInfo) event.getData();
		onCreate();
	}
	// 设置企业注册后自动/手动激活
	public void onAutoActiveSwitch()
	{
		String msgStr = "企业激活当前设置为：";
		switch (EnterpriseInfoServiceUtil.getDefaultStatus())
		{
		case 1:
			try
			{
				if (Messagebox.YES == Messagebox.show("确认修改企业注册激活设置为： " + "手动激活？", "操作提示", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION))
				{
					EnterpriseInfoServiceUtil.setDefaultStatus(0);
					btnAutoActive.setLabel("自动激活");
					msgStr += "手动激活";
					Messagebox.show(msgStr, "操作提示", Messagebox.OK, msgStr);
				}
				break;
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		case 0:
			try
			{
				if (Messagebox.YES == Messagebox.show("确认修改企业注册激活设置为： " + "自动激活", "操作提示", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION))
				{
					EnterpriseInfoServiceUtil.setDefaultStatus(1);
					btnAutoActive.setLabel("手动激活");
					msgStr += "自动激活";
					Messagebox.show(msgStr, "操作提示", Messagebox.OK, msgStr);
				}
				break;
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	private class EnterpriseActiveRowRenderer
			implements RowRenderer
	{
		@Override
		public void render(Row row, Object data)
				throws Exception
		{
			final EnterpriseInfo enterprise = (EnterpriseInfo) data;
			final Toolbarbutton btnEName = new Toolbarbutton(enterprise.getName());
			btnEName.addEventListener(Events.ON_CLICK, new EventListener()
			{
				@Override
				public void onEvent(Event arg0)
						throws Exception
				{
					onView(enterprise);
				}
			});
			btnEName.setParent(row);
			new Label(enterprise.getLocation()).setParent(row);
			new Label(enterprise.getContactPerson()).setParent(row);
			new Label(enterprise.getTelephoneNum()).setParent(row);
			final Button btnActive = new Button("激活");
			btnActive.addEventListener(Events.ON_CLICK, new EventListener()
			{
				@Override
				public void onEvent(Event arg0)
						throws Exception
				{
					onActive(enterprise);
				}
			});
			btnActive.setParent(row);
		}
	}
}
