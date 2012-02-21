package openjframework.web.zk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import openjframework.model.ProfileInfo;
import openjframework.service.ProfileInfoServiceUtil;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class UserManageComposer extends GenericForwardComposer{

	
	private static final long serialVersionUID = 1L;
	Window uMWin;
	Textbox nameTbx,depTbx;
	List<ProfileInfo>userlist;
	Listbox userLbx;
	ProfileInfo profile;
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		userlist=ProfileInfoServiceUtil.getAll();
	}
	public void onDetail(ForwardEvent event) throws SuspendNotAllowedException, InterruptedException
	{
		Toolbarbutton tlb=(Toolbarbutton)event.getOrigin().getTarget();
		ProfileInfo profile=(ProfileInfo)tlb.getAttribute("profile");
		Map params = new HashMap();
		params.put("profile",profile);
		Window objWindow = (Window) Executions.createComponents("/SysForm/SystemAdministrator/ProfileInfo.zul",null,params);
		objWindow.setParent(uMWin);
		objWindow.doModal();
	}
	public void onClick$addBtn() throws SuspendNotAllowedException, InterruptedException
	{
		Map params = new HashMap();
		params.put("cmd","add");
		Window objWindow = (Window) Executions.createComponents("/SysForm/SystemAdministrator/registerUser.zul",null,params);
		objWindow.setParent(uMWin);
		objWindow.setWidth("720px");
		objWindow.setTitle("新注册用户");
		objWindow.setClosable(true);
		objWindow.setBorder("normal");
		objWindow.doModal();
	}
	public void onClick$updateBtn() throws SuspendNotAllowedException, InterruptedException
	{
		if(profile==null)
		{
			Messagebox.show("请选择一个用户进行修改!", "提示", Messagebox.OK, Messagebox.INFORMATION);
			return;
		}
		Map params = new HashMap();
		params.put("cmd","edit");
		params.put("profile",profile);
		Window objWindow = (Window) Executions.createComponents("/SysForm/SystemAdministrator/registerUser.zul",null,params);
		objWindow.setParent(uMWin);
		objWindow.setWidth("720px");
		objWindow.setTitle("修改用户基本资料");
		objWindow.setClosable(true);
		objWindow.setBorder("normal");
		objWindow.doModal();
	}
	public void onClick$queryBtn()
	{
		String qstr="";
		if(nameTbx.getValue()!=""&&nameTbx.getValue()!=null)
 		{
			qstr+=" and name like "
				+"'%"+nameTbx.getValue()+"%'";
		}
		if(depTbx.getValue()!=""&&depTbx.getValue()!=null)
 		{
	 		
			qstr+=" and organization.organizationName like "
				+"'%"+depTbx.getValue()+"%'";
			
		}
		if(qstr!="")
		{
			qstr=qstr.replaceFirst("and","");
			qstr="from ProfileInfo where "+qstr;
		}else
		{
			qstr="from ProfileInfo";		
		}
		List<ProfileInfo>resultlist=ProfileInfoServiceUtil.findByQuery(qstr);
		BindingListModelList list=new BindingListModelList(resultlist,true);
		userLbx.setModel(list);
	}
	public List<ProfileInfo> getUserlist() {
		return userlist;
	}
	public void setUserlist(List<ProfileInfo> userlist) {
		this.userlist = userlist;
	}
	public ProfileInfo getProfile() {
		return profile;
	}
	public void setProfile(ProfileInfo profile) {
		this.profile = profile;
	}
}
