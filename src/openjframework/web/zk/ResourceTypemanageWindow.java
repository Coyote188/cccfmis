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


public class ResourceTypemanageWindow extends Window{

   Long taskInstanceId;
   String processId = "0";
   String userId;
   HashMap params = new HashMap();
    public ResourceTypeInfo resourceTypeInfo;
    public ResourceTypeInfo getResourceTypeInfo(){
           return resourceTypeInfo;
    }
    public void setResourceTypeInfo(ResourceTypeInfo entity){
           this.resourceTypeInfo=entity;
    }
    
    public void onCreate(){
    List<ResourceTypeInfo> list=ResourceTypeInfoServiceUtil.getAll();
    if (list != null &&
				!list.isEmpty())
		{
			setResourceTypeInfo(list.iterator().next());
		}
	         
		
	}
	
	public List<ResourceTypeInfo> getObjList()
	{
		Listbox listbox = getListbox();
		List<ResourceTypeInfo> list = (List<ResourceTypeInfo>)listbox.getModel();
		return list;
	}
	
	public Listbox getListbox()
	{
		return (Listbox)this.getFellow("resourceTypeInfoListbox");
	}
	
	public void onAdd()
	{	
		params.put("resourceType", null);	
		params.put("cmd", "add");		
		openEditWindow(params);
	}
	
	public void openEditWindow(Map params)	{
		 
		Window objWindow = (Window) Executions.createComponents("resourceType-edit.zul",null,params);
		
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
				ResourceTypeInfo delcom=ResourceTypeInfoServiceUtil.getByPrimaryKey(id);
				ResourceTypeInfoServiceUtil.delete(delcom);
			}

			if(selectItems.size()>0){
				refreshUsersListbox();				
			}
		}
	}
	
	public void onEdit()
	{
		params.put("resourceType", getResourceTypeInfo());		
		params.put("cmd", "edit");	
		openEditWindow(params);
	}
	
	public void refreshUsersListbox()
	{
		List<ResourceTypeInfo> list = getObjList();
		list.clear();
		list.addAll(ResourceTypeInfoServiceUtil.getAll());		
	}
	
	public void onSearch()
	{
		String qstr="";
		Hbox hbox=(Hbox)this.getFellow("querybox");
		
		
		if(qstr!="")
		{
			qstr=qstr.replaceFirst("and","");
			qstr="from ResourceTypeInfo where "+qstr;
		}else
		{
			qstr="from ResourceTypeInfo";		
		}
		List<ResourceTypeInfo> searchResult=ResourceTypeInfoServiceUtil.findByQuery(qstr);
		List<ResourceTypeInfo> list = getObjList();
		list.clear();
		list.addAll(searchResult);	
	}
	
	
		
		
	

}
	
	