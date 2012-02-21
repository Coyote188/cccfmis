package openjframework.web.zk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import openjframework.model.ProfileInfo;
import openjframework.service.ProfileInfoServiceUtil;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;


public class UserPermissionManageComposer extends GenericForwardComposer{

	private static final long serialVersionUID = 1L;
	Window uPMWin;
    List<ProfileInfo>userlist;
    Textbox nameTbx,depTbx;
    Listbox userLbx;
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		userlist=ProfileInfoServiceUtil.getAll();
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
		ListModel list=new SimpleListModel(resultlist);
		userLbx.setModel(list);
	}
	//分配
	public void onClick$assignTlb(ForwardEvent event)
	{
		Toolbarbutton tool = (Toolbarbutton) event.getOrigin().getTarget();
		Tabpanel tp=(Tabpanel) uPMWin.getParent();
		uPMWin.detach();
		ProfileInfo user=(ProfileInfo) tool.getAttribute("user");
		Map params = new HashMap();
		params.put("profile",user);
		Window objWindow = (Window) Executions.createComponents("/SysForm/SystemAdministrator/user_permission.zul",null,params);
		objWindow.setParent(tp);
		objWindow.doEmbedded();
	}
	public List<ProfileInfo> getUserlist() {
		return userlist;
	}
	public void setUserlist(List<ProfileInfo> userlist) {
		this.userlist = userlist;
	}
	
}
