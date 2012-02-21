package openjframework.web.zk;

import java.math.*;
import java.util.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import openjframework.service.*;
import openjframework.model.*;

import org.zkoss.zk.ui.*;
import org.zkoss.zul.*;


public class PositionmanageWindow extends Window {

	Long taskInstanceId;
	String processId = "0";
	String userId;
	HashMap params = new HashMap();
	public PositionInfo positionInfo;

	public PositionInfo getPositionInfo() {
		return positionInfo;
	}

	public void setPositionInfo(PositionInfo entity) {
		this.positionInfo = entity;
	}

	public void onCreate() {
		List<PositionInfo> list = PositionInfoServiceUtil.getAll();
		if (list != null && !list.isEmpty()) {
			setPositionInfo(list.iterator().next());
		}

	}

	public List<PositionInfo> getObjList() {
		Listbox listbox = getListbox();
		List<PositionInfo> list = (List<PositionInfo>) listbox.getModel();
		return list;
	}

	public Listbox getListbox() {
		return (Listbox) this.getFellow("positionInfoListbox");
	}

	public void onAdd() {
		params.put("position", null);
		params.put("cmd", "add");
		openEditWindow(params);
	}

	public void openEditWindow(Map params) {
		params.put("parentListbox", getListbox());
		Window objWindow = (Window) Executions
				.createComponents(
						"/SysForm/SystemAdministrator/position-edit.zul",
						null, params);

		try {

			objWindow.doModal();
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
				PositionInfo delcom = PositionInfoServiceUtil
						.getByPrimaryKey(id);
				PositionInfoServiceUtil.delete(delcom);
			}

			if (selectItems.size() > 0) {
				refreshUsersListbox();
			}
		}
	}

	public void onEdit() {
		params.put("position", getPositionInfo());
		params.put("cmd", "edit");
		openEditWindow(params);
	}

	public void refreshUsersListbox() {
		List<PositionInfo> list = getObjList();
		list.clear();
		list.addAll(PositionInfoServiceUtil.getAll());
	}

	public void onSearch() {
		String qstr = "";
		Hbox hbox = (Hbox) this.getFellow("querybox");

		Textbox no = (Textbox) hbox.getFellow("no");
		if (no.getText() != "" && no.getText() != null) {

			qstr = qstr + " and no like ";

			qstr = qstr + "'%" + no.getText() + "%'";

		}

		Textbox name = (Textbox) hbox.getFellow("name");
		if (name.getText() != "" && name.getText() != null) {

			qstr = qstr + " and name like ";

			qstr = qstr + "'%" + name.getText() + "%'";

		}

		if (qstr != "") {
			qstr = qstr.replaceFirst("and", "");
			qstr = "from PositionInfo where " + qstr;
		} else {
			qstr = "from PositionInfo";
		}
		List<PositionInfo> searchResult = PositionInfoServiceUtil
				.findByQuery(qstr);
		List<PositionInfo> list = getObjList();
		list.clear();
		list.addAll(searchResult);
	}

}
