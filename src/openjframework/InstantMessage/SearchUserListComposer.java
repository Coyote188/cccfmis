package openjframework.InstantMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import openjframework.model.UserInfo;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

public class SearchUserListComposer extends GenericForwardComposer {
	
	public List<UserInfo> getSearchUserList() {
		return searchUserList;
	}
	public void setSearchUserList(List<UserInfo> searchUserList) {
		this.searchUserList = searchUserList;
	}
	private static final long serialVersionUID = 1L;
	private List<UserInfo> searchUserList;

	private AnnotateDataBinder binder;
	private Listbox searchUserListbox;
	private Bandbox searchBbx;
	private List<UserInfo>userListData;
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		binder = new AnnotateDataBinder(comp);
		searchUserList = new ListModelList();
		comp.setAttribute(comp.getId(), this, true);
		binder.loadAll();
		
	}
	public void onFocus$searchBbx()
	{
		userListData=UserListData.getInstance().getAllUser();
	}

	public void onChanging$searchBbx(org.zkoss.zk.ui.event.InputEvent event)
	{
		List<UserInfo> filterUserList=new ArrayList();
		if(userListData.size()>0)
		{
			for(UserInfo user:userListData)
			{
				if(user.getName().contains(event.getValue()))
					filterUserList.add(user);
			}
		}
		System.out.print("eg"+event.getValue());
		searchUserList = new ListModelList(filterUserList);
		binder.loadAll();
	}
	public void onClick$searchUserListbox(Event evt) {
		if (searchUserListbox.getSelectedItem() == null)
			return;
		Listitem clickedItem = searchUserListbox.getSelectedItem();
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
