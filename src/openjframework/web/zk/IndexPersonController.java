package openjframework.web.zk;

import java.text.SimpleDateFormat;
import java.util.List;
import openjframework.InstantMessage.UserListData;
import openjframework.model.MessageInfo;
import openjframework.model.UserInfo;
import openjframework.model.UserLogInfo;
import openjframework.service.MessageInfoServiceUtil;
import openjframework.service.PermissionInfoServiceUtil;
import openjframework.service.UserInfoServiceUtil;
import openjframework.service.UserLogInfoServiceUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.sys.SessionCtrl;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.*;

@SuppressWarnings("unused")
public class IndexPersonController
		extends GenericForwardComposer
{
	private static final long	serialVersionUID	= -3801962286418473844L;
	public static final String	ON_DETACHTAB		= "onDetachTab";
	private Tree				tempCataTre;
	private Treecell			tclEnterpriseActive;
	private Tabbox				mainTab;
	private Toolbarbutton		msgimg;
	private Popup				msgpop, ppNewEAcitveInfo;
	private Label				usermsg;
	private Label				subject;
	private Textbox				content;
	private Panel				jhPanel;
	private Label				lastlogtime;
	private Label				lastlogip;
	private Window				Indexwindow;
	// 打开菜单
	public void onMenuActive(ForwardEvent event)
	{
		String uri = (String) event.getData();
		if (uri == null || uri.isEmpty())
			return;
		Menuitem menuitem = (Menuitem) event.getOrigin().getTarget();
		String lab = menuitem.getLabel();
		for (Object comp : mainTab.getTabs().getChildren())
		{
			if (!(comp instanceof Tab))
				continue;
			Tab tab = Tab.class.cast(comp);
			if (tab.getLabel().equals(lab))
			{
				mainTab.setSelectedTab(tab);
				return;
			}
		}
		Tab tab = new Tab(lab);
		tab.setClosable(true);
		Tabpanel tabpanel = new Tabpanel();
		tabpanel.setVflex("true");
		tabpanel.setHflex("true");
	 
		Component comp = Executions.createComponents(uri, tabpanel, null);
		if (comp instanceof Window)
		{
			Window win = (Window) comp;
			win.setSizable(false);
			win.setBorder("none");
			win.setHflex("true");
			win.setVflex("true");
			win.setContentStyle("overflow: auto;");
			win.setTitle("");
			win.doEmbedded();
		}
		mainTab.getTabs().appendChild(tab);
		mainTab.getTabpanels().appendChild(tabpanel);
		mainTab.setSelectedTab(tab);
	}
	@Override
	public void doAfterCompose(Component comp)
			throws Exception
	{
		UserInfo user = UserInfoServiceUtil.getCurrentLoginUser();
		if(user==null){
			Executions.getCurrent().sendRedirect("/login.zul");
			return;
		}
		super.doAfterCompose(comp);
		tempCataTre.addEventListener(Events.ON_SELECT, new EventListener()
		{
			@Override
			public void onEvent(Event arg0)
					throws Exception
			{
				Treeitem item = tempCataTre.getSelectedItem();
				if (item == null || item.getValue() == null)
				{
					return;
				} else
				{
					// 权限验证
					if (!PermissionInfoServiceUtil.getEmabledByResoueNameOfLoginUser(item.getId()))
					{
						try
						{
							Messagebox.show("您无使用此功能的权限!", "操作提示", Messagebox.OK, Messagebox.INFORMATION);
							return;
						} catch (InterruptedException e1)
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					Tabs ts = mainTab.getTabs();
					List<Tab> tab = ts.getChildren();
					int i = 0;
					for (Tab t : tab)
					{
						if (item.getLabel().equals(t.getLabel()))
						{
							i = t.getIndex();
							break;
						}
					}
					if (i != 0)
					{
						mainTab.setSelectedIndex(i);
						Tabpanel tp = mainTab.getSelectedPanel();
						if (tp != null)
							tp.removeChild(tp.getFirstChild());
						Window objWindow = (Window) Executions.createComponents(item.getValue().toString(), null, null);
						objWindow.setParent(tp);
						objWindow.setContentStyle("overflow:auto;");
						objWindow.doEmbedded();
					} else
					{
						final Tab t = new Tab();
						t.setLabel(item.getLabel());
						t.setClosable(true);
						ts.appendChild(t);
						Tabpanels tps = mainTab.getTabpanels();
						Tabpanel tp = new Tabpanel();
						tp.setStyle("overflow:auto");
						tp.setStyle("overflow:auto");
						tp.setVflex("true");
						tp.setHflex("true");
						tps.appendChild(tp);
						Window objWindow = (Window) Executions.createComponents(item.getValue().toString(), null, null);
					
						objWindow.setContentStyle("overflow:auto;");
						objWindow.setParent(tp);
						objWindow.setSizable(false);
						objWindow.setBorder("none");
						objWindow.setHflex("true");
						objWindow.setVflex("true");
						objWindow.setContentStyle("overflow: auto;");
						objWindow.setTitle("");
						objWindow.doEmbedded();
						objWindow.doEmbedded();
						mainTab.setSelectedTab(t);
					}
					tempCataTre.selectItem(null);
				}
			}
		});
		
		// 有新Message消息监听
		List<MessageInfo> msgList = MessageInfoServiceUtil.findMessage(0, user);
		if (!msgList.isEmpty())
		{
			msgimg.setVisible(true);
			MessageInfo msg = msgList.get(0);
			MessageInfoServiceUtil.readMsg(msg);
			page.setVariable("msgInfo", msg);
		}
		// Listener for incoming messages ( scope=APPLICATION )
		EventQueues.lookup(user.getId() + "msgEventQueue", EventQueues.APPLICATION, true).subscribe(new EventListener()
		{
			@Override
			public void onEvent(Event event)
					throws Exception
			{
				MessageInfo msg = (MessageInfo) event.getData();
				msgimg.setVisible(true);
				if (2 == msg.getType())
				{
					// tclEnterpriseActive.setStyle("color:red;text-Decoration:underline;");
					ppNewEAcitveInfo.open(tclEnterpriseActive, "after_end");
				}
				page.setVariable("msgInfo", msg);
				// 弹出显示消息的Popup窗口
				if (2 == msg.getType())
					usermsg.setValue("系统消息");
				else
					usermsg.setValue("来自[" + msg.getSendUser().getName() + "]的消息");
				subject.setValue(msg.getSubject());
				content.setValue(msg.getContent());
				// msgpop.open(100, 250);
				msg.setReadStatus(1);
				MessageInfoServiceUtil.update(msg);
				// if (msg.getType() == 1) {
				// // 刷新mytask_list
				// Window win = (Window) Indexwindow.getFellow("person_centered_Win");
				// Tabpanel mytaskPanel= (Tabpanel) win.getFellow("mytaskPanel");
				// Component taskwin = (Component) mytaskPanel.getFirstChild();
				// mytaskPanel.removeChild(taskwin);
				// Window objWindow = (Window) Executions.createComponents(
				// "mytask_list.zul", null, null);
				// objWindow.setParent(mytaskPanel);
				// objWindow.doEmbedded();
				// }
			}
		});
		// 监听刷新已办事务消息
		// EventQueues.lookup(user.getId() + "refreshEndTaskListEvent",
		// EventQueues.APPLICATION, true).subscribe(new EventListener() {
		// @Override
		// public void onEvent(Event event) throws Exception {
		//
		// // 刷新mytask_list
		// Window win = (Window) Indexwindow.getFellow("person_centered_Win");
		// Tabpanel mytaskPanel= (Tabpanel) win.getFellow("myEndTaskPanel");
		// Window taskwin = (Window) mytaskPanel.getFirstChild();
		// mytaskPanel.removeChild(taskwin);
		// Window objWindow = (Window) Executions.createComponents(
		// "myendtask_list.zul", null, null);
		// objWindow.setParent(mytaskPanel);
		// objWindow.doEmbedded();
		//
		//
		// }
		// });
		// 上次登录信息
		UserLogInfo lastLog = UserLogInfoServiceUtil.getUserLastLoginLog(user.getId());
		if (lastLog != null)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String str1 = sdf.format(lastLog.getLogDate());
			lastlogtime.setValue(str1);
			lastlogip.setValue(lastLog.getIpAddr());
		}
	}
	public void onDetachTab()
	{
		mainTab.getSelectedTab().close();
	}
	/**
	 * 修改密码
	 * 
	 * @throws SuspendNotAllowedException
	 * @throws InterruptedException
	 */
	public void onClick$modifyBtn(Event event)
			throws SuspendNotAllowedException, InterruptedException
	{
		Window objWindow = (Window) Executions.createComponents("modifypassword.zul", null, null);
		objWindow.doModal();
	}
	public void onViewHome(ForwardEvent event)
	{}
	/**
	 * 个人设置
	 * 
	 * @param event
	 */
	public void onSetting(Event event)
	{}
	/**
	 * 帮助
	 * 
	 * @param event
	 */
	public void onHelp(Event event)
	{}
	/**
	 * 退出登录
	 * 
	 * @param event
	 */
	public void onClick$logoutBtn(Event event)
	{
		UserInfo user = UserInfoServiceUtil.getCurrentLoginUser();
		if (sessionScope.containsKey("login_name"))
		{
			String name = (String) sessionScope.get("login_name");
			// 设置当前用户为离线
			UserListData.getInstance().updateUser(user, 0);
			sessionScope.remove("login_name");
			EventQueues.lookup("userList", EventQueues.APPLICATION, false).publish(new Event("loginOut"));
			EventQueues.lookup("chatRoomChannel", EventQueues.APPLICATION, false).publish(new Event("loginOut"));
		}
		((SessionCtrl) Sessions.getCurrent()).invalidateNow();
		Sessions.getCurrent(true);// 必须重建session
		Executions.sendRedirect("/login.zul");
	}
	public void onClick$Pmsg()
	{
		try
		{
			Window objWindow = (Window) Executions.createComponents("/SysForm/message-form.zul", null, null);
			objWindow.doModal();
		} catch (SuspendNotAllowedException e)
		{
			e.printStackTrace();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
