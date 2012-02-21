package cccf.ma.web.zk;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.zkoss.zk.ui.*;
import org.zkoss.zul.*;

import cccf.ma.model.*;
import cccf.ma.service.*;

public class CommontreatymangeWindow extends Window{

   Long taskInstanceId;
   String processId = "0";
   String userId;
   HashMap params = new HashMap();
    public CommonTreatyInfo commonTreatyInfo;
    public CommonTreatyInfo getCommonTreatyInfo(){
           return commonTreatyInfo;
    }
    public void setCommonTreatyInfo(CommonTreatyInfo entity){
           this.commonTreatyInfo=entity;
    }
    
    public void onCreate(){
    List<CommonTreatyInfo> list=CommonTreatyInfoServiceUtil.getAll();
    if (list != null &&
				!list.isEmpty())
		{
			setCommonTreatyInfo(list.iterator().next());
		}
	         
		
	}
	
	public List<CommonTreatyInfo> getObjList()
	{
		Listbox listbox = getListbox();
		List<CommonTreatyInfo> list = (List<CommonTreatyInfo>)listbox.getModel();
		return list;
	}
	
	public Listbox getListbox()
	{
		return (Listbox)this.getFellow("commonTreatyInfoListbox");
	}
	
	public void onAdd()
	{	
		params.put("commonTreaty", null);	
		params.put("cmd", "add");		
		openEditWindow(params);
	}
	
	public void openEditWindow(Map params)	{
		params.put("parentListbox", getListbox());
		Window objWindow = (Window) Executions.createComponents("commontreaty-edit.zul",null,params);
		
		try {
			
			objWindow.doModal();
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
				CommonTreatyInfo delcom=CommonTreatyInfoServiceUtil.getByPrimaryKey(id);
				CommonTreatyInfoServiceUtil.delete(delcom);
			}

			if(selectItems.size()>0){
				refreshUsersListbox();				
			}
		}
	}
	
	public void onEdit()
	{
		params.put("commonTreaty", getCommonTreatyInfo());		
		params.put("cmd", "edit");	
		openEditWindow(params);
	}
	
	public void refreshUsersListbox()
	{
		List<CommonTreatyInfo> list = getObjList();
		list.clear();
		list.addAll(CommonTreatyInfoServiceUtil.getAll());		
	}
	
	public void onSearch()
	{
		String qstr="";
		Hbox hbox=(Hbox)this.getFellow("querybox");
		 		
 		Textbox title=(Textbox)hbox.getFellow("title");
 		if(title.getText()!=""&&title.getText()!=null)
 		{
	 		
			qstr=qstr+" and title like ";
			
			qstr=qstr+"'%"+title.getText()+"%'";
			
		}
		 		
 		Textbox type=(Textbox)hbox.getFellow("type");
 		if(type.getText()!=""&&type.getText()!=null)
 		{
	 		
			qstr=qstr+" and type=";
			
			qstr=qstr+type.getText();
			
		}
		
		
		if(qstr!="")
		{
			qstr=qstr.replaceFirst("and","");
			qstr="from CommonTreatyInfo where "+qstr;
		}else
		{
			qstr="from CommonTreatyInfo";		
		}
		List<CommonTreatyInfo> searchResult=CommonTreatyInfoServiceUtil.findByQuery(qstr);
		List<CommonTreatyInfo> list = getObjList();
		list.clear();
		list.addAll(searchResult);	
	}
	
	
		
		
	

}
	
	