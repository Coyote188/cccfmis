package openjframework.web.zk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import openjframework.model.RoleInfo;
import openjframework.service.RoleInfoServiceUtil;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import com.sun.org.apache.bcel.internal.generic.NEW;

@SuppressWarnings({"serial","unchecked"})
public class RoleDistributeController extends GenericForwardComposer{
	
	public static final String ON_RELOAD = "onReload";
	
	private List<RoleInfo> roles;
	Map param;
	
	private Div divUserList;
	private Grid grdRoleList;
	
	public void onClick$btnDistribute(ForwardEvent event){
		RoleInfo role = (RoleInfo) event.getOrigin().getTarget().getAttribute("role");
		param.put("role", role);
		Window frmUserList = (Window) Executions.createComponents("/SysForm/SystemAdministrator/user-role-distribute.zul", null, param);
		if (divUserList.hasFellow("frmUserList")||divUserList.hasFellow("divAddRole")) {
			divUserList.removeChild(divUserList.getLastChild());
			frmUserList.doEmbedded();
			
		}else {
			frmUserList.doEmbedded();
		}
		frmUserList.setParent(divUserList);
	}
	
	public void onClick$btnAdd(){
		param.put("role", new RoleInfo());
		param.put("str","是否确认保存 ：");
		Div divAddRole = (Div) Executions.createComponents("/SysForm/SystemAdministrator/role-edit.zul", null, param);
		if (divUserList.hasFellow("frmUserList")||divUserList.hasFellow("divAddRole")) {
			divUserList.removeChild(divUserList.getLastChild());
		}
		divAddRole.setParent(divUserList);
	}
	
	public void onClick$btnModify(ForwardEvent event){
		RoleInfo role = (RoleInfo) event.getOrigin().getTarget().getAttribute("role");
		if (role.isReadonly()) {
			try {
				Messagebox.show("该角色不允许修改!");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			param.put("role", role);
			param.put("str","是否确认将 ："+role.getName()+"修改为：");
			Div divAddRole = (Div) Executions.createComponents("/SysForm/SystemAdministrator/role-edit.zul", null, param);
			if (divUserList.hasFellow("frmUserList")||divUserList.hasFellow("divAddRole")) {
				divUserList.removeChild(divUserList.getLastChild());
			}
			divAddRole.setParent(divUserList);
		}
	}
	
	public void onReload(){
		roles = RoleInfoServiceUtil.getAll();
		ListModelList model = new ListModelList(roles);
		grdRoleList.setModel(model);
		
	}
	
	public void onClick$btnRemove(ForwardEvent event){
		RoleInfo role = (RoleInfo) event.getOrigin().getTarget().getAttribute("role");
		if (role.isReadonly()) {
			try {
				Messagebox.show("该角色不允许修改!");
				return;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			if(Messagebox.YES == Messagebox.show("是否确认删除："+role.getName()+"?","操作提示",Messagebox.YES|Messagebox.NO,Messagebox.QUESTION))
				RoleInfoServiceUtil.delete(role);
			onReload();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		param = new HashMap();
		roles = RoleInfoServiceUtil.getAll();
	}

	public List<RoleInfo> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleInfo> roles) {
		this.roles = roles;
	}

}
