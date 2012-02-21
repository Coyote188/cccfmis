package openjframework.web.zk;

import java.util.*;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import openjframework.service.*;
import openjframework.model.*;

import org.zkoss.zk.ui.*;
import org.zkoss.zul.*;
import org.zkoss.zk.ui.event.InputEvent;

import com.aidi.bpm.service.*;
import com.aidi.core.spring.*;
import com.aidi.core.zk.*;
import openjframework.service.*;
import openjframework.model.*;

public class PermissioneditWindow extends Window {

	public PermissionInfo permission;
	Map params = Executions.getCurrent().getArg();
	String userId;

	List roleList;
	Bandbox bdRoleList;
	Checkbox ckVisible;
	Checkbox ckEnable;
	Checkbox ckWriteble;

	public void onCreate() {
		
		bdRoleList=(Bandbox)this.getFellow("bdRoleList");
		permission = (PermissionInfo) this.getPage().getVariable("permission");
		if (permission.getRoleList() == null) {
			permission.setRoleList(new HashSet());
		} else {

			bdRoleList = (Bandbox) this.getFellow("bdRoleList");
			String rname = "";
			for (Object o : permission.getRoleList()) {
				RoleInfo role=(RoleInfo)o;
				rname += "[" + role.getName() + "]";
			}
			bdRoleList.setText(rname);
		}
		
		ckVisible=(Checkbox)this.getFellow("ckVisible");
	}

	public void onSave() {
		validateData();
		int pint=0;
		if(ckVisible.isChecked()){
			pint=100;
		}
		permission.setPermissionType(pint);
		PermissionInfoServiceUtil.saveOrUpdate(permission);
		this.detach();
	}

	/**
	 * Cancel this screen
	 * 
	 */
	public void onCancel() {
		this.detach();
	}

	public void onRoleListSelect() {
		Listbox lbxRoleList = (Listbox) this.getFellow("lbxRoleList");

		Set<Listitem> list = lbxRoleList.getSelectedItems();
		String rName = "";
		roleList = new ArrayList();
		if (list.size() > 0) {
			int i = 0;
			for (Listitem item : list) {

				RoleInfo role = (RoleInfo) item.getValue();
				rName += "[" + role.getName() + "]";
				roleList.add(role);

			}
			bdRoleList.setText(rName);
			permission.getRoleList().clear();
			permission.getRoleList().addAll(roleList);
		}

	}

	void validateData() {

		bdRoleList.getValue();

	}

}
