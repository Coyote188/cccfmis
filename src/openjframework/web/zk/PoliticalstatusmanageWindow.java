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


public class PoliticalstatusmanageWindow extends Window {

	Long taskInstanceId;
	String processId = "0";
	String userId;
	HashMap params = new HashMap();
	public PoliticalStatusInfo politicalStatusInfo;

	public PoliticalStatusInfo getPoliticalStatusInfo() {
		return politicalStatusInfo;
	}

	public void setPoliticalStatusInfo(PoliticalStatusInfo entity) {
		this.politicalStatusInfo = entity;
	}

	public void onCreate() {
		List<PoliticalStatusInfo> list = PoliticalStatusInfoServiceUtil
				.getAll();
		if (list != null && !list.isEmpty()) {
			setPoliticalStatusInfo(list.iterator().next());
		}

	}

	public List<PoliticalStatusInfo> getObjList() {
		Listbox listbox = getListbox();
		List<PoliticalStatusInfo> list = (List<PoliticalStatusInfo>) listbox
				.getModel();
		return list;
	}

	public Listbox getListbox() {
		return (Listbox) this.getFellow("politicalStatusInfoListbox");
	}

	public void onAdd() {
		params.put("politicalStatus", null);
		params.put("cmd", "add");
		openEditWindow(params);
	}

	public void openEditWindow(Map params) {
		params.put("parentListbox", getListbox());
		Window objWindow = (Window) Executions.createComponents(
				"/SysForm/SystemAdministrator/politicalstatus-edit.zul", null,
				params);

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
				PoliticalStatusInfo delcom = PoliticalStatusInfoServiceUtil
						.getByPrimaryKey(id);
				PoliticalStatusInfoServiceUtil.delete(delcom);
			}

			if (selectItems.size() > 0) {
				refreshUsersListbox();
			}
		}
	}

	public void onEdit() {
		params.put("politicalStatus", getPoliticalStatusInfo());
		params.put("cmd", "edit");
		openEditWindow(params);
	}

	public void refreshUsersListbox() {
		List<PoliticalStatusInfo> list = getObjList();
		list.clear();
		list.addAll(PoliticalStatusInfoServiceUtil.getAll());
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
			qstr = "from PoliticalStatusInfo where " + qstr;
		} else {
			qstr = "from PoliticalStatusInfo";
		}
		List<PoliticalStatusInfo> searchResult = PoliticalStatusInfoServiceUtil
				.findByQuery(qstr);
		List<PoliticalStatusInfo> list = getObjList();
		list.clear();
		list.addAll(searchResult);
	}

}
