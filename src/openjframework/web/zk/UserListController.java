package openjframework.web.zk;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import openjframework.model.OrganizationInfo;
import openjframework.model.ProfileInfo;
import openjframework.model.RoleInfo;
import openjframework.model.UserInfo;
import openjframework.service.ProfileInfoServiceUtil;
import openjframework.service.RoleInfoServiceUtil;
import openjframework.service.UserInfoServiceUtil;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Window;


@SuppressWarnings({"serial","unchecked","unused"})
public class UserListController extends GenericForwardComposer {
//	private static final long serialVersionUID = 1L;	
	
	private ListModel gridModel;
	private List<List<ProfileInfo>> model;
	List<ProfileInfo> list_user;
	private Grid userGrd;
	private RoleInfo role;
	Map params = Executions.getCurrent().getArg();

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		role = (RoleInfo) params.get("role");
		drawuserGrdRow();
	}

	public void drawuserGrdRow() {
		list_user = ProfileInfoServiceUtil.getAll();
		if (list_user != null && list_user.size() > 0) {
			model = new ArrayList<List<ProfileInfo>>();
			int rowclu = list_user.size() / 6;
			int row_clu = list_user.size() % 6;
			for (int i = 0; i < rowclu; i++) {
				List<ProfileInfo> rowlist = new ArrayList<ProfileInfo>();
				rowlist = list_user.subList((i + 5 * i), ((i + 5) + 5 * i) + 1);// subList(from:0,to:4),截取list为0,1,2,3
				model.add(rowlist);
			}
			if (row_clu > 0) {
				List<ProfileInfo> rowlist = new ArrayList<ProfileInfo>();
				rowlist = list_user.subList(6 * rowclu, list_user.size());
				model.add(rowlist);
			}

			gridModel = new ListModelList(model,true);
			System.out.println(model.size()+"list model list here");
			userGrd.setModel(gridModel);
			userGrd.setRowRenderer(new RowRenderer() {
				@Override
				public void render(Row row, Object data) throws Exception {
					if (data == null)
						return;
					List<ProfileInfo> userRows = (List<ProfileInfo>) data;
					
					for (final ProfileInfo user : userRows) {
						if (user != null) {
							final Checkbox ckb = new Checkbox();
							ckb.setLabel(user.getName());
							ckb.setAttribute("user", user);
							ckb.setTooltiptext("姓名："+user.getName()+"，单位："+user.getOrganization());
							if (isUserHasRole(user)) {
								ckb.setChecked(true);
							}
							
							ckb.addEventListener(Events.ON_CHECK, new EventListener(){

								@Override
								public void onEvent(Event arg0) throws Exception {
									if (ckb.isChecked() == true) {
										modifyRole(user, role, Type.add);
									}else {
										modifyRole(user, role, Type.min);
									}
								}
							});
							
							row.appendChild(ckb);
						}
					}
				}
			});
		}
	}
	
	private void modifyRole(ProfileInfo user , RoleInfo roleInfo , Type type){
		Set<ProfileInfo> users = role.getUserList();
		if(type.equals(Type.add)){
			users.add(user);
		}else {
			users.remove(user);
			Iterator it = (Iterator) users.iterator();
			while (it.hasNext()) {
				ProfileInfo u = (ProfileInfo) it.next();
				if (u.getId().equals(user.getId())) {
					it.remove();
					break;
				}
			}
		}
	}
	
	public void onClick$btnUpdate(){
		try {
			int reply = Messagebox.show("是否确认修改角色信息？","操作提示",Messagebox.YES|Messagebox.NO,Messagebox.QUESTION);
			if(reply == Messagebox.YES)
				RoleInfoServiceUtil.update(role);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean isUserHasRole(ProfileInfo user) {
		boolean isEqual = false;
		Set<RoleInfo> roles = user.getRoleList();
		for (RoleInfo role : roles) {
			if (role.getName().toString()
					.equals(this.role.getName().toString())) {
				isEqual = true;
				break;
			}
		}
		return isEqual;
	}

	public void onClick$user_select_Btn() {
		GrdCheck_All(userGrd, true);
	}

	public void onClick$user_notselect_Btn() {
		GrdCheck_All(userGrd, false);
	}

	public void GrdCheck_All(Grid grd, boolean bool) {
		List<Checkbox> selectedlist = new ArrayList();
		List<Row> rowlist = grd.getRows().getChildren();
		Set<ProfileInfo> users = role.getUserList();
		for (Row row : rowlist) {
			List<Checkbox> list = row.getChildren();
			users.clear();
			for (Checkbox ckb : list) {
				ckb.setChecked(bool);
				if (bool) {
					for (ProfileInfo user : list_user) {
						users.add(user);
					}
				}
			}
		}
		role.setUserList(users);
		System.out.println(bool+""+users.size());
	}

	public List<Checkbox> getGrdBySelectedChildren(Grid grd) {
		List<Checkbox> selectedlist = new ArrayList();
		List<Row> rowlist = grd.getRows().getChildren();
		for (Row row : rowlist) {
			List<Checkbox> list = row.getChildren();
			for (Checkbox ckb : list) {
				if (ckb.isChecked())
					selectedlist.add(ckb);
			}
		}
		return selectedlist;
	}

	public RoleInfo getRole() {
		return role;
	}

	public void setRole(RoleInfo role) {
		this.role = role;
	}
	
	private enum Type{
		add,min //操作类型，添加/删除
	}
}
