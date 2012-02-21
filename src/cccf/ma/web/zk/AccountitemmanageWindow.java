package cccf.ma.web.zk;

import java.math.*;
import java.util.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import openjframework.service.*;
import openjframework.model.*;
import com.aidi.bpm.zk.BpmZkUtil;
import com.aidi.bpm.service.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.zk.ui.*;
import org.zkoss.zul.*;

import com.aidi.core.zk.*;
import cccf.ma.model.*;
import cccf.ma.service.*;

public class AccountitemmanageWindow extends Window {

	Long taskInstanceId;
	String processId = "0";
	String userId;
	HashMap params = new HashMap();
	public AccountItemInfo accountItemInfo;

	public AccountItemInfo getAccountItemInfo() {
		return accountItemInfo;
	}

	public void setAccountItemInfo(AccountItemInfo entity) {
		this.accountItemInfo = entity;
	}

	public void onCreate() {
		List<AccountItemInfo> list = AccountItemInfoServiceUtil.getAll();
		if (list != null && !list.isEmpty()) {
			setAccountItemInfo(list.iterator().next());
		}

	}

	public List<AccountItemInfo> getObjList() {
		Listbox listbox = getListbox();
		List<AccountItemInfo> list = (List<AccountItemInfo>) listbox.getModel();
		return list;
	}

	public Listbox getListbox() {
		return (Listbox) this.getFellow("accountItemInfoListbox");
	}

	public void onAdd() {
		params.put("accountItem", null);
		params.put("cmd", "add");
		openEditWindow(params);
	}

	public void openEditWindow(Map params) {
		params.put("parentListbox", getListbox());
		Window objWindow = (Window) Executions.createComponents(
				"accountitem-edit.zul", null, params);

		try {

			objWindow.doModal();
			refreshUsersListbox();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void onDelete() throws InterruptedException {
		if (Messagebox.show("是否要删除记录?", "删除?", Messagebox.OK | Messagebox.NO,
				Messagebox.QUESTION) == Messagebox.OK) {
			Listbox listbox = getListbox();
			Set selectItems = listbox.getSelectedItems();
			Iterator iterator = selectItems.iterator();
			// String value = "";
			while (iterator.hasNext()) {
				Listitem listitem = (Listitem) iterator.next();
				String id = listitem.getValue().toString();
				AccountItemInfo delcom = AccountItemInfoServiceUtil
						.getByPrimaryKey(id);
				AccountItemInfoServiceUtil.delete(delcom);
			}

			if (selectItems.size() > 0) {
				refreshUsersListbox();
			}
		}
	}

	public void onEdit() {
		params.put("accountItem", getAccountItemInfo());
		params.put("cmd", "edit");
		openEditWindow(params);
	}

	public void refreshUsersListbox() {
		List<AccountItemInfo> list = getObjList();
		list.clear();
		list.addAll(AccountItemInfoServiceUtil.getAll());
	}

	public void onSearch() {
		String qstr = "";
		Hbox hbox = (Hbox) this.getFellow("querybox");

		if (qstr != "") {
			qstr = qstr.replaceFirst("and", "");
			qstr = "from AccountItemInfo where " + qstr;
		} else {
			qstr = "from AccountItemInfo";
		}
		List<AccountItemInfo> searchResult = AccountItemInfoServiceUtil
				.findByQuery(qstr);
		List<AccountItemInfo> list = getObjList();
		list.clear();
		list.addAll(searchResult);
	}

	public void onListitemSelect() {
		// 可编辑状态
		boolean editStatus =accountItemInfo.getAccountType().getIsCustom();
		Button btDel = (Button) this.getFellow("btDel");
		btDel.setDisabled(!editStatus);

	}

}
