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
import openjframework.model.*;
import openjframework.service.*;

public class ResourcemanageWindow extends Window {

	Long taskInstanceId;
	String processId = "0";
	String userId;
	HashMap params = new HashMap();
	public ResourceInfo resourceInfo;

	public ResourceInfo getResourceInfo() {
		return resourceInfo;
	}

	public void setResourceInfo(ResourceInfo entity) {
		this.resourceInfo = entity;
	}

	public void onCreate() {
		List<ResourceInfo> list = ResourceInfoServiceUtil.getAll();
		if (list != null && !list.isEmpty()) {
			setResourceInfo(list.iterator().next());
		}

	}

	public List<ResourceInfo> getObjList() {
		Listbox listbox = getListbox();
		List<ResourceInfo> list = (List<ResourceInfo>) listbox.getModel();
		return list;
	}

	public Listbox getListbox() {
		return (Listbox) this.getFellow("resourceInfoListbox");
	}

	public void onAdd() {
		params.put("resource", null);
		params.put("cmd", "add");
		openEditWindow(params);
	}

	public void openEditWindow(Map params) {
		params.put("parentListbox", getListbox());
		Window objWindow = (Window) Executions.createComponents(
				"SystemAdministrator/resource-edit.zul", null, params);

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
				ResourceInfo delcom = ResourceInfoServiceUtil
						.getByPrimaryKey(id);
				ResourceInfoServiceUtil.delete(delcom);
			}

			if (selectItems.size() > 0) {
				refreshUsersListbox();
			}
		}
	}

	public void onEdit() {
		params.put("resource", getResourceInfo());
		params.put("cmd", "edit");
		openEditWindow(params);
	}

	public void refreshUsersListbox() {
		List<ResourceInfo> list = getObjList();
		list.clear();
		list.addAll(ResourceInfoServiceUtil.getAll());
	}

	public void onSearch() {
		String qstr = "";
		Hbox hbox = (Hbox) this.getFellow("querybox");

		if (qstr != "") {
			qstr = qstr.replaceFirst("and", "");
			qstr = "from ResourceInfo where " + qstr;
		} else {
			qstr = "from ResourceInfo";
		}
		List<ResourceInfo> searchResult = ResourceInfoServiceUtil
				.findByQuery(qstr);
		List<ResourceInfo> list = getObjList();
		list.clear();
		list.addAll(searchResult);
	}

	public void onPermission() {
		HashMap rparams=new HashMap();
		rparams.put("resource", getResourceInfo());
		rparams.put("cmd", "edit");

		Window objWindow = (Window) Executions.createComponents(
				"SystemAdministrator/permission-edit.zul", null, rparams);

		try {

			objWindow.doModal();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
