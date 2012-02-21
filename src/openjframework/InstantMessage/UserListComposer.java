package openjframework.InstantMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import openjframework.InstantMessage.UserListData;

import openjframework.model.UserInfo;
import openjframework.service.UserInfoServiceUtil;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

public class UserListComposer extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;
	private List<UserInfo> userList;
	private AnnotateDataBinder binder;
	private Listbox userListbox;
//	private HashMap<String, ArrayList<String>> mgsHsmap = new HashMap<String, ArrayList<String>>();

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		binder = new AnnotateDataBinder(comp);
		userList = new ListModelList(UserListData.getInstance().getAllUser());
		EventQueue que = EventQueues.lookup("userList",
				EventQueues.APPLICATION, true);
		que.subscribe(new EventListener() {
			public void onEvent(Event evt) throws Exception {
				userList = new ListModelList(UserListData.getInstance()
						.getAllUser());
				binder.loadAll();
			}
		});

//		EventQueues
//				.lookup(
//						"chatOne_" + sessionScope.get("login_name")
//								+ "_Channel_Notice", EventQueues.APPLICATION,
//						true).subscribe(new EventListener() {
//					@Override
//					public void onEvent(Event evt) throws Exception {
//						mgsHsmap = (HashMap<String, ArrayList<String>>) sessionScope
//								.get("HSMsg");
//						String talkTomeGuys = evt.getName();
//						if (mgsHsmap.containsKey(talkTomeGuys)) {
//							for (int i = 0; i < userListbox.getItemCount(); i++) {
//								if (userListbox.getItemAtIndex(i).getValue().toString()
//										.equalsIgnoreCase(talkTomeGuys)) {
//									userListbox.getItemAtIndex(i).setStyle(
//											"color:red;");
//								}
//							}
//						}
//					}
//				});
		comp.setAttribute(comp.getId(), this, true);
		binder.loadAll();
	}

	public List<UserInfo> getUserList() {
		return userList;
	}

	public void onDoubleClick$userListbox(Event evt) {
		if (userListbox.getSelectedItem() == null)
			return;
		Listitem clickedItem = userListbox.getSelectedItem();
		UserInfo userInfo = (UserInfo) clickedItem.getAttribute("userInfo");
		pageScope.put("talkObject", userInfo);
		if (!page.hasFellow("win_" + userInfo.getUsername())) {
			Window win = (Window) Executions.createComponents(
					"/SysForm/InstantMessage/chatOne.zul", null, pageScope);
			win.setId("win_" + userInfo.getUsername());
			win.setPosition("center");
			Random r = new Random();
			win.setTop(String.valueOf(r.nextInt(200)) + "px");
			win.setLeft(String.valueOf(r.nextInt(200)) + "px");
			win.setMinheight(20);
			win.setSizable(true);
			win.setStyle("background:#c5e6ef;");
			win.doOverlapped();
		} else {
			page.getFellow("win_" + userInfo.getUsername()).invalidate();
		}

	}

}
