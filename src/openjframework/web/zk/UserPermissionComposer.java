package openjframework.web.zk;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import openjframework.model.ProfileInfo;
import openjframework.model.RoleInfo;
import openjframework.service.ProfileInfoServiceUtil;
import openjframework.service.RoleInfoServiceUtil;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Window;

public class UserPermissionComposer extends GenericForwardComposer{

	private static final long serialVersionUID = 1L;
	private Window uPWin;
	private ListModelList gridModel;
	private List<List<RoleInfo>> model;
	private Grid roleGrd;
	ProfileInfo profile;
	private Label msgLab;
	List<RoleInfo>list_role;
	Map params=Executions.getCurrent().getArg();
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		profile=(ProfileInfo)params.get("profile");
		drawRoleGrdRow();
	}
	public void drawRoleGrdRow()
	{
		list_role=RoleInfoServiceUtil.getAll();
		if(list_role!=null&&list_role.size()>0)
		{
			model=new ArrayList<List<RoleInfo>>();
			int rowclu=list_role.size()/6;
			int row_clu=list_role.size()%6;
			for(int i=0;i<rowclu;i++)
			{
				List<RoleInfo> rowlist=new ArrayList<RoleInfo>();
				rowlist=list_role.subList((i+5*i), ((i+5)+5*i)+1);//subList(from:0,to:4),截取list为0,1,2,3
				model.add(rowlist);
			}
			if(row_clu>0)
			{
				List<RoleInfo> rowlist=new ArrayList<RoleInfo>();
				rowlist=list_role.subList(6*rowclu,list_role.size());
				model.add(rowlist);
			}
			
			gridModel = new ListModelList(model,true);
			roleGrd.setModel(gridModel);
			roleGrd.setRowRenderer(new RowRenderer() {			
				@Override
				public void render(Row row, Object data) throws Exception {
					if (data == null) return;
					List<RoleInfo> roleRows = (List<RoleInfo>) data;
					Checkbox ckb;
					for(RoleInfo role:roleRows)
					{
						if(role!=null)
						{
							ckb=new Checkbox();
							ckb.setLabel(role.getName());
							ckb.setAttribute("role", role);
							if(RoleInfoServiceUtil.isExistByProfileInfo(profile, role))
								ckb.setChecked(true);
							row.appendChild(ckb);
						}
					}
				}
			});
		}
	}
	
	public void onClick$role_select_Btn()
	{
		if(list_role!=null&&list_role.size()>0)
		{
			GrdCheck_All(roleGrd,true);
		}
		else
			msgLab.setValue("没有记录!");
	}
	public void onClick$role_notselect_Btn()
	{
		if(list_role!=null&&list_role.size()>0)
		{
			GrdCheck_All(roleGrd,false);
		}
		else
			msgLab.setValue("没有记录!");
	}
	public void onClick$role_update_Btn() throws InterruptedException
	{
		if(list_role!=null&&list_role.size()>0)
		{
			if(profile.getRoleList().size()>0)
			{
				profile.setRoleList(null);
				ProfileInfoServiceUtil.update(profile);
			}
			Set roleList=new HashSet();
			List<Checkbox>role_ckblist=getGrdBySelectedChildren(roleGrd);
			if(role_ckblist!=null&&role_ckblist.size()>0)
			{
				for(Checkbox ckb:role_ckblist)
				{
					RoleInfo role=(RoleInfo) ckb.getAttribute("role");
					roleList.add(role);
				}
				profile.setRoleList(roleList);
				ProfileInfoServiceUtil.update(profile);
			}
			msgLab.setValue("["+profile.getName()+"]拥有的角色已更新!");
		}
		else
			msgLab.setValue("没有记录!");
	}
	public void onClick$back_Btn()
	{
		Tabpanel tp=(Tabpanel) uPWin.getParent();
		uPWin.detach();
		Window objWindow = (Window) Executions.createComponents("/SysForm/SystemAdministrator/user_permissionmanage.zul",null,null);
		objWindow.setParent(tp);
		objWindow.doEmbedded();
	}
	public void GrdCheck_All(Grid grd,boolean bool)
	{
		List<Checkbox>selectedlist=new ArrayList();
		List<Row>rowlist=grd.getRows().getChildren();
		for(Row row:rowlist)
		{
			List<Checkbox>list=row.getChildren();
			for(Checkbox ckb:list)
			{
				ckb.setChecked(bool);
			}
			
		}
	}
	public List<Checkbox> getGrdBySelectedChildren(Grid grd)
	{
		List<Checkbox>selectedlist=new ArrayList();
		List<Row>rowlist=grd.getRows().getChildren();
		for(Row row:rowlist)
		{
			List<Checkbox>list=row.getChildren();
			for(Checkbox ckb:list)
			{
				if(ckb.isChecked())selectedlist.add(ckb);
			}
		}
		return selectedlist;
	}
	
	public ProfileInfo getProfile() {
		return profile;
	}
	public void setProfile(ProfileInfo profile) {
		this.profile = profile;
	}
}
