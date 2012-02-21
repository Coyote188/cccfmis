package cccf.mis.web.index;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import openjframework.model.UserInfo;
import openjframework.service.UserInfoServiceUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.sys.SessionCtrl;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Box;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;
import cccf.ma.model.CommonTreatyInfo;
import cccf.ma.service.CommonTreatyInfoServiceUtil;
import cccf.mis.web.index.PersonIndexController.WorkingThread;
import cccf.mis.web.index.enterprise.ApplyQueryUtils;

public class EnterpriseIndexController
		extends GenericForwardComposer
{
	private Box					mianbox, homebox, editbox;
	private Grid				running_apply;
	private Tabbox				maintabbox;
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -5166154108153270513L;
	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo)
	{
		// TODO Auto-generated method stub
		try
		{
			UserInfo userInfo = UserInfoServiceUtil.getCurrentLoginUser();
			List<Map> kk = ApplyQueryUtils.getRunningApplys(userInfo.getId());
			if (kk != null)
			{
				page.setAttribute("running_apply", kk);
			}
			List<Map> kk3 = ApplyQueryUtils.getTodoTask(userInfo.getId());
			if (kk3 != null)
			{
				page.setAttribute("task_apply", kk3);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return super.doBeforeCompose(page, parent, compInfo);
	}
	public void doAfterCompose(Component comp)
			throws Exception
	{
		UserInfo userInfo = UserInfoServiceUtil.getCurrentLoginUser();
		if (userInfo == null)
		{
			Executions.sendRedirect("/login.zul");
			return;
		}
		super.doAfterCompose(comp);
	}
	/**
	 * 退出
	 * 
	 * @param event
	 */
	public void onLogout(ForwardEvent event)
	{
		((SessionCtrl) Sessions.getCurrent()).invalidateNow();
		Sessions.getCurrent(true);
		Executions.sendRedirect("/login.zul");
	}
	/**
	 * 发起新审请
	 * 
	 * @param event
	 */
	public void onNewApply(ForwardEvent event)
	{
		homebox.setVisible(false);
		editbox.getChildren().clear();
		editbox.setVisible(true);
		Window win = (Window) Executions.createComponents("/views/Index_enterprise/item/Acc_apply.zul", editbox, null);
		win.addEventListener("onClose", new applywinListener());
	}
	public void onRest(ForwardEvent event)
	{
		homebox.setVisible(true);
		editbox.setVisible(false);
	}
	class applywinListener
			implements EventListener
	{
		@Override
		public void onEvent(Event event)
				throws Exception
		{
			EnterpriseIndexController.this.onRest(null);
		}
	}
	public void onAction(ForwardEvent event)
	{
		Component comp = event.getOrigin().getTarget();
		String lab;
		if (comp instanceof Toolbarbutton)
		{
			lab = ((Toolbarbutton) comp).getLabel();
		} else if (comp instanceof Treerow)
		{
			lab = ((Treecell) comp.getFirstChild()).getLabel();
		} else if (comp instanceof Treecell)
		{
			lab = ((Treecell) comp).getLabel();
		} else
		{
			return;
		}
		String url = (String) event.getData();
		Tab tab = getTaskTab(maintabbox.getTabs(), lab);
		if (tab == null)
		{
			tab = new Tab();
			tab.setLabel(lab);
			tab.setClosable(true);
			maintabbox.getTabs().appendChild(tab);
			Tabpanel tabpanel = new Tabpanel();
			tabpanel.setVflex("true");
			tabpanel.setHflex("true");
			maintabbox.getTabpanels().appendChild(tabpanel);
			// URL
			Window win = (Window) Executions.createComponents(url, null, arg);
			win.setHflex("true");
			win.setVflex("true");
			win.setTitle("");
			win.setBorder("none");
			win.setClosable(false);
			win.setParent(tabpanel);
		}
		tab.setSelected(true);
	}
	private Tab getTaskTab(Tabs tabs, String lab)
	{
		if (tabs == null)
			return null;
		@SuppressWarnings("unchecked")
		List<Tab> list = tabs.getChildren();
		for (Tab tab : list)
		{
			if (lab.equals(tab.getLabel()))
				return tab;
		}
		return null;
	}
	/**
	 * @param event
	 * @throws SuspendNotAllowedException
	 * @throws InterruptedException
	 */
	public void onCommontreaty(ForwardEvent event)
			throws SuspendNotAllowedException, InterruptedException
	{
		// URL
		CommonTreatyInfo productTreaty = CommonTreatyInfoServiceUtil.getNewestofProtocol(1);
		param.put("cott", productTreaty);
		Window win = (Window) Executions.createComponents("/SysForm/commontreaty.zul", null, param);
		win.doModal();
	}
}
