package openjframework.web.zk;

import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import openjframework.service.*;
import openjframework.model.*;

import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.*;

public class OrganizationmanageWindow extends Window{

   Long taskInstanceId;
   String processId = "0";
   String userId;
   HashMap params = new HashMap();
    public OrganizationInfo organizationInfo;
    public OrganizationInfo getOrganizationInfo(){
           return organizationInfo;
    }
    public void setOrganizationInfo(OrganizationInfo entity){
           this.organizationInfo=entity;
    }
    
    public void onCreate(){
    List<OrganizationInfo> list=OrganizationInfoServiceUtil.getAll();
    if (list != null &&
				!list.isEmpty())
		{
			setOrganizationInfo(list.iterator().next());
		}
	         
		
	}
	
	public List<OrganizationInfo> getObjList()
	{
		Listbox listbox = getListbox();
		List<OrganizationInfo> list = (List<OrganizationInfo>)listbox.getModel();
		return list;
	}
	
	public Listbox getListbox()
	{
		return (Listbox)this.getFellow("organizationInfoListbox");
	}
	
	
	public void onDelete() throws InterruptedException
	{
		if (Messagebox.show("是否要删除记录?", "删除?", Messagebox.OK | Messagebox.NO,
				Messagebox.QUESTION) == Messagebox.OK) {			
			Listbox listbox = getListbox();
			Set selectItems=listbox.getSelectedItems();
			Iterator iterator = selectItems.iterator();
			//String value = "";
			while(iterator.hasNext()) {
				Listitem listitem=(Listitem)iterator.next();
				String id=listitem.getValue().toString();
				OrganizationInfo delcom=OrganizationInfoServiceUtil.getByPrimaryKey(id);
				OrganizationInfoServiceUtil.delete(delcom);
			}

			if(selectItems.size()>0){
				refreshUsersListbox();				
			}
		}
	}
	public void refreshUsersListbox()
	{
		List<OrganizationInfo> list = getObjList();
		list.clear();
		list.addAll(OrganizationInfoServiceUtil.getAll());		
	}
	
	public void onSearch()
	{
		String qstr="";
		Hbox hbox=(Hbox)this.getFellow("querybox");
		 		
 		Textbox organizationNo=(Textbox)hbox.getFellow("organizationNo");
 		if(organizationNo.getText()!=""&&organizationNo.getText()!=null)
 		{
	 		
			qstr=qstr+" and organizationNo like ";
			
			qstr=qstr+"'%"+organizationNo.getText()+"%'";
			
		}
		 		
 		Textbox organizationName=(Textbox)hbox.getFellow("organizationName");
 		if(organizationName.getText()!=""&&organizationName.getText()!=null)
 		{
	 		
			qstr=qstr+" and organizationName like ";
			
			qstr=qstr+"'%"+organizationName.getText()+"%'";
			
		}	
		
		if(qstr!="")
		{
			qstr=qstr.replaceFirst("and","");
			qstr="from OrganizationInfo where "+qstr;
		}else
		{
			qstr="from OrganizationInfo";		
		}
		List<OrganizationInfo> searchResult=OrganizationInfoServiceUtil.findByQuery(qstr);
		List<OrganizationInfo> list = getObjList();
		list.clear();
		list.addAll(searchResult);	
	}
	
	
	public void onAssignPerson(ForwardEvent event)
	{
		Toolbarbutton tool = (Toolbarbutton) event.getOrigin().getTarget();
		Tabpanel tp=(Tabpanel) this.getParent();
		Tabs tbs=tp.getTabbox().getTabs();
		List<Tab> tab = tbs.getChildren();
		OrganizationInfo org=(OrganizationInfo) tool.getAttribute("org");
		Map params = new HashMap();
		params.put("organization",org);
		int i = -1;
		for (Tab t : tab) {
			if (t.getLabel().equals("分配单位人员")) {
				i = t.getIndex();
				break;
			}
		}
		
		if(i==-1)
		{
			this.detach();
			tp.getLinkedTab().setLabel("分配单位人员");
			Window objWindow = (Window) Executions.createComponents("/SysForm/SystemAdministrator/organization-person-assign.zul",null,params);
			objWindow.setParent(tp);
			objWindow.doEmbedded();
		}
		else{
			tp.getTabbox().setSelectedIndex(i);
			Tabpanel tpl=tp.getTabbox().getSelectedPanel();
			if (tpl != null)
				tpl.removeChild(tpl.getFirstChild());
			Window objWindow = (Window) Executions.createComponents("/SysForm/SystemAdministrator/organization-person-assign.zul",null,params);
			objWindow.setParent(tpl);
			objWindow.doEmbedded();
		}
		
		
	}
	
		
		
	

}
	
	