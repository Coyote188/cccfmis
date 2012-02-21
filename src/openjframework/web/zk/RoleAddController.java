package openjframework.web.zk;

import java.util.Map;

import openjframework.model.RoleInfo;
import openjframework.service.RoleInfoServiceUtil;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class RoleAddController extends GenericForwardComposer{
	
	public static final String ON_TYPE="onModity";
	
	private String msgStr ="";
	
	private RoleInfo role;
	private Div divAddRole;
	private Textbox tbxRoleName;
	private Window frmRoleDistribute;
	Map param = Executions.getCurrent().getArg();
	private Combobox cbxIsReadonly;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		role = (RoleInfo) param.get("role");
		msgStr = (String)param.get("str");
		cbxIsReadonly.setSelectedIndex(0);
	}
	
	public void onClick$btnAddRole() {
		validata();
		setReadType();
		try {
			if (Messagebox.show(msgStr + role.getName(), "操作提示", Messagebox.YES
					| Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES)
				if (role.getId() != null) {
					save();
				} else if (!RoleInfoServiceUtil.isRoleExist(role.getName())) {
					save();
				} else {
					try {
						Messagebox.show("您要添加的角色已经存在！", "操作提示", Messagebox.YES,
								"");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void onSelect$cbxIsReadonly(){
		if (cbxIsReadonly.getSelectedIndex() == 1) {
			try {
				if(Messagebox.NO == Messagebox.show("将角色设置为只读状态后，将无法再对角色进行修改，是否确认将："+tbxRoleName.getText()+"设置为只读?","警告",Messagebox.YES|Messagebox.NO,Messagebox.QUESTION))
					cbxIsReadonly.setSelectedIndex(0);
			} catch (WrongValueException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void setReadType(){
		String isReadonly = cbxIsReadonly.getSelectedItem().getValue().toString();
		if (isReadonly.equals("true")) {
			role.setReadonly();
		}else {
			return;
		}
	}
	
	private void save(){
		RoleInfoServiceUtil.saveOrUpdate(role);
		Events.postEvent(RoleDistributeController.ON_RELOAD, frmRoleDistribute, null);
		try {
			Messagebox.show("操作成功!");
			onClick$btnClose();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void onClick$btnClose(){
		divAddRole.detach();
	}

	public void setRole(RoleInfo role) {
		this.role = role;
	}

	public RoleInfo getRole() {
		return role;
	}
	
	void validata(){
		tbxRoleName.getValue();
	}
}
