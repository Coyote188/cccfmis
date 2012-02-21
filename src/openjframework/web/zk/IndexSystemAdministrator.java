package openjframework.web.zk;

import java.text.SimpleDateFormat;
import java.util.List;

import openjframework.InstantMessage.UserListData;
import openjframework.model.MessageInfo;
import openjframework.model.UserInfo;
import openjframework.model.UserLogInfo;
import openjframework.service.MessageInfoServiceUtil;
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
import org.zkoss.zul.Label;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;

public class IndexSystemAdministrator extends GenericForwardComposer {
	private static final long serialVersionUID = -3801962286418473844L;
	private Tree tempCataTre;
	private Tabbox mainTab;
	private Label lastlogtime;
	private Label lastlogip;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		tempCataTre.addEventListener(Events.ON_SELECT, new EventListener() {

			@Override
			public void onEvent(Event arg0) throws Exception {
				Treeitem item = tempCataTre.getSelectedItem();
				if (item == null || item.getValue() == null) {
					return;
				} else {
					Tabs ts = mainTab.getTabs();
					List<Tab> tab = ts.getChildren();
					int i = -1;
					for (Tab t : tab) {
						if (item.getLabel().equals(t.getLabel())) {
							i = t.getIndex();
							break;
						}
					}
					if (i != -1) {
						mainTab.setSelectedIndex(i);
						Tabpanel tp = mainTab.getSelectedPanel();
						if (tp != null)
							tp.removeChild(tp.getFirstChild());
						Window objWindow = (Window) Executions
								.createComponents(item.getValue().toString(),
										null, null);
						objWindow.setParent(tp);
						// objWindow.setContentStyle("overflow:auto;");
						objWindow.doEmbedded();
					} else {
						final Tab t = new Tab();
						t.setLabel(item.getLabel());
						t.setClosable(true);
						ts.appendChild(t);
						Tabpanels tps = mainTab.getTabpanels();
						Tabpanel tp = new Tabpanel();
						tp.setStyle("overflow:auto");
						tps.appendChild(tp);
						Window objWindow = (Window) Executions
								.createComponents(item.getValue().toString(),
										null, null);
						objWindow.setContentStyle("overflow:auto;");
						objWindow.setParent(tp);
						objWindow.doEmbedded();
						mainTab.setSelectedTab(t);

					}
					tempCataTre.selectItem(null);

				}
			}

		});

		// 上次登录信息
		UserInfo user = UserInfoServiceUtil.getCurrentLoginUser();
		UserLogInfo lastLog = UserLogInfoServiceUtil.getUserLastLoginLog(user
				.getId());
		if (lastLog != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String str1 = sdf.format(lastLog.getLogDate());
			lastlogtime.setValue(str1);
			lastlogip.setValue(lastLog.getIpAddr());
		}
	}

	/**
	 * 修改密码
	 * 
	 * @throws SuspendNotAllowedException
	 * @throws InterruptedException
	 */
	public void onClick$modifyBtn(Event event)
			throws SuspendNotAllowedException, InterruptedException {
		Window objWindow = (Window) Executions.createComponents(
				"modifypassword.zul", null, null);
		objWindow.doModal();
	}

	public void onViewHome(ForwardEvent event) {

	}

	/**
	 * 个人设置
	 * 
	 * @param event
	 */
	public void onSetting(Event event) {
	}

	/**
	 *帮助
	 * 
	 * @param event
	 */
	public void onHelp(Event event) {

	}

	/**
	 * 退出登录
	 * 
	 * @param event
	 */
	public void onClick$logoutBtn(Event event) {

		UserInfo user = UserInfoServiceUtil.getCurrentLoginUser();
		if (sessionScope.containsKey("login_name")) {
			String name = (String) sessionScope.get("login_name");
			// 设置当前用户为离线
			UserListData.getInstance().updateUser(user,0);
			sessionScope.remove("login_name");
			EventQueues.lookup("userList", EventQueues.APPLICATION, false)
					.publish(new Event("loginOut"));
			EventQueues.lookup("chatRoomChannel", EventQueues.APPLICATION,
					false).publish(new Event("loginOut"));
		}

		((SessionCtrl) Sessions.getCurrent()).invalidateNow();
		Sessions.getCurrent(true);// 必须重建session

		Executions.sendRedirect("/login.zul");
	}
}