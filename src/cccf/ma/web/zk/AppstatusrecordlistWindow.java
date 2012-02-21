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

public class AppstatusrecordlistWindow extends Window{

   Long taskInstanceId;
   String processId = "0";
   String userId;
   HashMap params = new HashMap();
    public AppStatusRecordInfo appStatusRecordInfo;
    public AppStatusRecordInfo getAppStatusRecordInfo(){
           return appStatusRecordInfo;
    }
    public void setAppStatusRecordInfo(AppStatusRecordInfo entity){
           this.appStatusRecordInfo=entity;
    }
    
    public void onCreate(){
    List<AppStatusRecordInfo> list=AppStatusRecordInfoServiceUtil.getAll();
    if (list != null &&
				!list.isEmpty())
		{
			setAppStatusRecordInfo(list.iterator().next());
		}
	         
		
	}
	
	public List<AppStatusRecordInfo> getObjList()
	{
		Listbox listbox = getListbox();
		List<AppStatusRecordInfo> list = (List<AppStatusRecordInfo>)listbox.getModel();
		return list;
	}
	
	public Listbox getListbox()
	{
		return (Listbox)this.getFellow("appStatusRecordInfoListbox");
	}
	
	public void onAdd()
	{	
		params.put("appStatusRecord", null);	
		params.put("cmd", "add");		
		openEditWindow(params);
	}
	
	public void openEditWindow(Map params)	{
		params.put("parentListbox", getListbox());
		Window objWindow = (Window) Executions.createComponents(".zul",null,params);
		
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
				AppStatusRecordInfo delcom=AppStatusRecordInfoServiceUtil.getByPrimaryKey(id);
				AppStatusRecordInfoServiceUtil.delete(delcom);
			}

			if(selectItems.size()>0){
				refreshUsersListbox();				
			}
		}
	}
	
	public void onEdit()
	{
		params.put("appStatusRecord", getAppStatusRecordInfo());		
		params.put("cmd", "edit");	
		openEditWindow(params);
	}
	
	public void refreshUsersListbox()
	{
		List<AppStatusRecordInfo> list = getObjList();
		list.clear();
		list.addAll(AppStatusRecordInfoServiceUtil.getAll());		
	}
	
	public void onSearch()
	{
		String qstr="";
		Hbox hbox=(Hbox)this.getFellow("querybox");
		
		
		if(qstr!="")
		{
			qstr=qstr.replaceFirst("and","");
			qstr="from AppStatusRecordInfo where "+qstr;
		}else
		{
			qstr="from AppStatusRecordInfo";		
		}
		List<AppStatusRecordInfo> searchResult=AppStatusRecordInfoServiceUtil.findByQuery(qstr);
		List<AppStatusRecordInfo> list = getObjList();
		list.clear();
		list.addAll(searchResult);	
	}
	
	public  void onClose(){
		this.detach();
	}
	
	
		
		
	

}
	
	