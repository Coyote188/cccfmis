package cccf.ma.web.zk;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import openjframework.service.*;
import openjframework.model.*;
import com.aidi.bpm.zk.BpmZkUtil;
import com.aidi.bpm.service.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.zk.ui.*;
import org.zkoss.zul.*;

import com.aidi.core.zk.*;
import cccf.ma.model.*;
import cccf.ma.service.*;
import cccf.myenum.ApplicationStatusUtil;

public class AccounttypemanageWindow extends Window{

   Long taskInstanceId;
   String processId = "0";
   String userId;
   HashMap params = new HashMap();
    public AccountTypeInfo accountTypeInfo;
    public AccountTypeInfo getAccountTypeInfo(){
           return accountTypeInfo;
    }
    public void setAccountTypeInfo(AccountTypeInfo entity){
           this.accountTypeInfo=entity;
    }
    
    public void onCreate(){
    List<AccountTypeInfo> list=AccountTypeInfoServiceUtil.getAll();
    if (list != null &&
				!list.isEmpty())
		{
			setAccountTypeInfo(list.iterator().next());
		}
	         
		
	}
	
	public List<AccountTypeInfo> getObjList()
	{
		Listbox listbox = getListbox();
		List<AccountTypeInfo> list = (List<AccountTypeInfo>)listbox.getModel();
		return list;
	}
	
	public Listbox getListbox()
	{
		return (Listbox)this.getFellow("accountTypeInfoListbox");
	}
	
	public void onAdd()
	{	
		params.put("accountType", null);	
		params.put("cmd", "add");		
		openEditWindow(params);
	}
	
	public void openEditWindow(Map params)	{
		params.put("parentListbox", getListbox());
		Window objWindow = (Window) Executions.createComponents("accounttype-edit.zul",null,params);
		
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
				AccountTypeInfo delcom=AccountTypeInfoServiceUtil.getByPrimaryKey(id);
				AccountTypeInfoServiceUtil.delete(delcom);
			}

			if(selectItems.size()>0){
				refreshUsersListbox();				
			}
		}
	}
	
	public void onEdit()
	{
		params.put("accountType", getAccountTypeInfo());		
		params.put("cmd", "edit");	
		openEditWindow(params);
	}
	
	public void refreshUsersListbox()
	{
		List<AccountTypeInfo> list = getObjList();
		list.clear();
		list.addAll(AccountTypeInfoServiceUtil.getAll());		
	}
	
	public void onSearch()
	{
		String qstr="";
		Hbox hbox=(Hbox)this.getFellow("querybox");
		 		
 		Textbox name=(Textbox)hbox.getFellow("name");
 		if(name.getText()!=""&&name.getText()!=null)
 		{
	 		
			qstr=qstr+" and name like ";
			
			qstr=qstr+"'%"+name.getText()+"%'";
			
		}
		 		
 		Textbox desc=(Textbox)hbox.getFellow("desc");
 		if(desc.getText()!=""&&desc.getText()!=null)
 		{
	 		
			qstr=qstr+" and desc like ";
			
			qstr=qstr+"'%"+desc.getText()+"%'";
			
		}
		 		
 		Textbox isCustom=(Textbox)hbox.getFellow("isCustom");
 		if(isCustom.getText()!=""&&isCustom.getText()!=null)
 		{
	 		
			qstr=qstr+" and isCustom=";
			
			qstr=qstr+isCustom.getText();
			
		}
		
		
		if(qstr!="")
		{
			qstr=qstr.replaceFirst("and","");
			qstr="from AccountTypeInfo where "+qstr;
		}else
		{
			qstr="from AccountTypeInfo";		
		}
		List<AccountTypeInfo> searchResult=AccountTypeInfoServiceUtil.findByQuery(qstr);
		List<AccountTypeInfo> list = getObjList();
		list.clear();
		list.addAll(searchResult);	
	}
	
	public void onListitemSelect() {
		// 可编辑状态
		boolean editStatus =accountTypeInfo.getIsCustom();
		Button btModify = (Button) this.getFellow("btModify");
		Button btDel = (Button) this.getFellow("btDel");
		btModify.setDisabled(!editStatus);
		btDel.setDisabled(!editStatus);

	}
		
		
	

}
	
	