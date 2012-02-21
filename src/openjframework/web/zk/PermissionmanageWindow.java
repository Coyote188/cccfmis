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


public class PermissionmanageWindow extends Window{

   Long taskInstanceId;
   String processId = "0";
   String userId;
   HashMap params = new HashMap();
    public PermissionInfo permissionInfo;
    public PermissionInfo getPermissionInfo(){
           return permissionInfo;
    }
    public void setPermissionInfo(PermissionInfo entity){
           this.permissionInfo=entity;
    }
    
    public void onCreate(){
    List<PermissionInfo> list=PermissionInfoServiceUtil.getAll();
    if (list != null &&
				!list.isEmpty())
		{
			setPermissionInfo(list.iterator().next());
		}
	         
		
	}
	
	public List<PermissionInfo> getObjList()
	{
		Listbox listbox = getListbox();
		List<PermissionInfo> list = (List<PermissionInfo>)listbox.getModel();
		return list;
	}
	
	public Listbox getListbox()
	{
		return (Listbox)this.getFellow("permissionInfoListbox");
	}
	
	public void onAdd()
	{	
		params.put("permission", null);	
		params.put("cmd", "add");		
		openEditWindow(params);
	}
	
	public void openEditWindow(Map params)	{
		 
		Window objWindow = (Window) Executions.createComponents("permission-edit.zul",null,params);
		
		try {
			
			objWindow.doModal();
			refreshUsersListbox();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
				PermissionInfo delcom=PermissionInfoServiceUtil.getByPrimaryKey(id);
				PermissionInfoServiceUtil.delete(delcom);
			}

			if(selectItems.size()>0){
				refreshUsersListbox();				
			}
		}
	}
	
	public void onEdit()
	{
		params.put("permission", getPermissionInfo());		
		params.put("cmd", "edit");	
		openEditWindow(params);
	}
	
	public void refreshUsersListbox()
	{
		List<PermissionInfo> list = getObjList();
		list.clear();
		list.addAll(PermissionInfoServiceUtil.getAll());		
	}
	
	public void onSearch()
	{
		String qstr="";
		Hbox hbox=(Hbox)this.getFellow("querybox");
		
		
		if(qstr!="")
		{
			qstr=qstr.replaceFirst("and","");
			qstr="from PermissionInfo where "+qstr;
		}else
		{
			qstr="from PermissionInfo";		
		}
		List<PermissionInfo> searchResult=PermissionInfoServiceUtil.findByQuery(qstr);
		List<PermissionInfo> list = getObjList();
		list.clear();
		list.addAll(searchResult);	
	}
	
	
		
		
	

}
	
	