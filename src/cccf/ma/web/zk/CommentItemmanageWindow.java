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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.zk.ui.*;
import org.zkoss.zul.*;
import com.aidi.core.zk.*;


import cccf.ma.model.*;
import cccf.ma.service.*;

public class CommentItemmanageWindow extends Window{

   Long taskInstanceId;
   String processId = "0";
   String userId;
   HashMap params = new HashMap();
    public CommentItemInfo commentItemInfo;
    UserInfo user = UserInfoServiceUtil.getCurrentLoginUser();
    public CommentItemInfo getCommentItemInfo(){
           return commentItemInfo;
    }
    public void setCommentItemInfo(CommentItemInfo entity){
           this.commentItemInfo=entity;
    }
    
    public void onCreate(){
    List<CommentItemInfo> list=CommentItemInfoServiceUtil.findByUserInfo(user);
    if (list != null &&
				!list.isEmpty())
		{
			setCommentItemInfo(list.iterator().next());
		}
	         
		
	}
	
	public List<CommentItemInfo> getObjList()
	{
		Listbox listbox = getListbox();
		List<CommentItemInfo> list = (List<CommentItemInfo>)listbox.getModel();
		return list;
	}
	
	public Listbox getListbox()
	{
		return (Listbox)this.getFellow("commentItemInfoListbox");
	}
	public void onBrowse()
	{	
		params.put("commentItem",  getCommentItemInfo());	
		params.put("cmd", "browse");		
		openBrowseWindow(params);
	}
	public void openBrowseWindow(Map params)	{
		params.put("parentListbox", getListbox());
		Window objWindow = (Window) Executions.createComponents("commentItem-browse.zul",null,params);
		
		try {
			
			objWindow.doModal();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void onAdd()
	{	
		params.put("commentItem", null);	
		params.put("cmd", "add");		
		openEditWindow(params);
	}
	
	public void openEditWindow(Map params)	{
		params.put("parentListbox", getListbox());
		Window objWindow = (Window) Executions.createComponents("commentItem-eidt.zul",null,params);
		
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
				CommentItemInfo delcom=CommentItemInfoServiceUtil.getByPrimaryKey(id);
				CommentItemInfoServiceUtil.delete(delcom);
			}

			if(selectItems.size()>0){
				refreshUsersListbox();				
			}
		}
	}
	public void onWord() throws InterruptedException
	{
		if(openjframework.util.ReportToWord.wordReport()){
			Messagebox.show("导出word成功!");
					
		}
	}
	public void onEdit()
	{
		params.put("commentItem", getCommentItemInfo());		
		params.put("cmd", "edit");	
		openEditWindow(params);
	}
	
	public void refreshUsersListbox()
	{
		List<CommentItemInfo> list = getObjList();
		list.clear();
		list.addAll(CommentItemInfoServiceUtil.getAll());		
	}
	
	public void onSearch()
	{
		String qstr="";
		Hbox hbox=(Hbox)this.getFellow("querybox");
		
		
		if(qstr!="")
		{
			qstr=qstr.replaceFirst("and","");
			qstr="from CommentItemInfo where "+qstr;
		}else
		{
			qstr="from CommentItemInfo";		
		}
		List<CommentItemInfo> searchResult=CommentItemInfoServiceUtil.findByQuery(qstr);
		List<CommentItemInfo> list = getObjList();
		list.clear();
		list.addAll(searchResult);	
	}
	
	
		
		
	

}
	
	