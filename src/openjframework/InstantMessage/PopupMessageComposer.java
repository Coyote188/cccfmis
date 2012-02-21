package openjframework.InstantMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import openjframework.model.InstantMessageInfo;
import openjframework.model.UserInfo;
import openjframework.service.InstantMessageInfoServiceUtil;
import openjframework.service.UserInfoServiceUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Window;

public class PopupMessageComposer
		extends GenericForwardComposer
{
	public List<UserInfo> getUserlist()
	{
		return userlist;
	}
	private static final long					serialVersionUID	= 1L;
	private List<UserInfo>						userlist;
	private HashMap<String, List<UserInfo>>		ulHsmap				= new HashMap<String, List<UserInfo>>();
	private HashMap<String, ArrayList<String>>	mgsHsmap			= new HashMap<String, ArrayList<String>>();
	private AnnotateDataBinder					binder;
	private Div									_mini_window;
	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo)
	{
		return super.doBeforeCompose(page, parent, compInfo);
	}
	@Override
	public void doAfterCompose(Component comp)
			throws Exception
	{
		super.doAfterCompose(comp);
		userlist = new ArrayList<UserInfo>();
		binder = new AnnotateDataBinder(comp);
		// _mini_window.setVisible(false);
		Clients.evalJavaScript("hideMini()");
		UserInfo user = UserInfoServiceUtil.getCurrentLoginUser();
		if (user == null)
		{
			Executions.getCurrent().sendRedirect("/login.zul");
			return;
		}
		List<InstantMessageInfo> im_list = InstantMessageInfoServiceUtil.getMyUnlineMessage(user.getId());
		if (im_list != null && im_list.size() > 0)
		{
			for (InstantMessageInfo imi : im_list)
			{
				UserInfo us = imi.getSendUser();
				us.setTemp(1);
				if (!isExistSelf(us, userlist))
					userlist.add(us);
				// 将离线消息读取处理
				String talkTomeGuys = us.getUsername();
				if (!mgsHsmap.containsKey(talkTomeGuys))
				{
					ArrayList<String> msgList = new ArrayList<String>();
					msgList.add(imi.getContent());
					mgsHsmap.put(talkTomeGuys, msgList);
				} else
				{
					mgsHsmap.get(talkTomeGuys).add(imi.getContent());
				}
			}
			Clients.evalJavaScript("showMini()");
			// _mini_window.setVisible(true);
		}
		EventQueues.lookup("chatOne_" + sessionScope.get("login_name") + "_Channel_Notice", EventQueues.APPLICATION, true).subscribe(new EventListener()
		{
			@Override
			public void onEvent(Event event)
					throws Exception
			{
				String talkTomeGuys = event.getName();
				UserInfo senderUser = UserInfoServiceUtil.getByUserName(talkTomeGuys);
				if (!page.hasFellow("win_" + senderUser.getUsername()))
				{
					if (!ulHsmap.containsKey(sessionScope.get("login_name")))
					{
						List<UserInfo> ulList = new ArrayList<UserInfo>();
						ulList.add(senderUser);
						ulHsmap.put((String) sessionScope.get("login_name"), ulList);
					} else
					{
						if (!isExistSelf(senderUser, userlist))
							ulHsmap.get(sessionScope.get("login_name")).add(senderUser);
					}
					userlist.clear();
					userlist.addAll(ulHsmap.get(sessionScope.get("login_name")));
					Clients.evalJavaScript("showMini()");
					// _mini_window.setVisible(true);
					binder.loadAll();
				}
			}
		});
		comp.setAttribute(comp.getId(), this, true);
		binder.loadAll();
	}
	public boolean isExistSelf(UserInfo user, List<UserInfo> userlist)
	{
		if (userlist == null)
			return false;
		else if (userlist.size() == 0)
			return false;
		for (Iterator<UserInfo> it = userlist.iterator(); it.hasNext();)
		{
			UserInfo _user = (UserInfo) it.next();
			if (user.getId().equals(_user.getId()))
				return true;
		}
		return false;
	}
	public void delUser(UserInfo user)
	{
		for (Iterator<UserInfo> it = userlist.iterator(); it.hasNext();)
		{
			UserInfo _user = (UserInfo) it.next();
			if (user.getId().equals(_user.getId()))
				it.remove();
		}
	}
	public void onVisible()
	{
		_mini_window.setVisible(false);
	}
	@SuppressWarnings("unchecked")
	public void onOpenItem(ForwardEvent event)
	{
		Listcell cell = (Listcell) event.getOrigin().getTarget();
		UserInfo userInfo = (UserInfo) cell.getAttribute("user");
		String talkTomeGuys = userInfo.getUsername();
		pageScope.put("talkObject", userInfo);
		if (userInfo.getTemp() == 1)
			sessionScope.put("HSMsg", mgsHsmap);
		Window win = (Window) Executions.createComponents("/SysForm/InstantMessage/chatOne.zul", null, pageScope);
		win.setId("win_" + userInfo.getUsername());
		win.setPosition("center");
		Random r = new Random();
		win.setTop(String.valueOf(r.nextInt(200)) + "px");
		win.setLeft(String.valueOf(r.nextInt(200)) + "px");
		win.setMinheight(20);
		win.setSizable(true);
		win.setStyle("background:#c5e6ef;");
		win.doOverlapped();
		delUser(userInfo);
		if (userlist.size() == 0)
		{
			Clients.evalJavaScript("hideMini()");
			ulHsmap.remove(sessionScope.get("login_name"));
		}
		// 更新消息已读状态
		//
		if (userInfo.getTemp() == 1)
			InstantMessageInfoServiceUtil.updateMyUnlineMessageBySendUser(userInfo);
		binder.loadAll();
	}
}
