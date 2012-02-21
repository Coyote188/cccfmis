package cccf.ma.web.zk;
import java.util.*;

import org.zkoss.zk.ui.*;
import org.zkoss.zul.*;
import cccf.ma.service.*;
import cccf.ma.model.*;
import cccf.myenum.TreatyType;

public class CommontreatyeditWindow extends Window{

     public CommonTreatyInfo commonTreaty; 
     Map params = Executions.getCurrent().getArg();
     String userId;
	 String processId = "0";
	 String rowId;
     
     public void onCreate()
     {
    	 commonTreaty=(CommonTreatyInfo)this.getPage().getVariable("commonTreaty");
    	 //初始化产品类别combobox
      	Combobox typeCbx=(Combobox)this.getFellow("type");
      	for(int i=0;i<TreatyType.values().length;i++){
      		Comboitem item=new Comboitem();
      		item.setValue(i);
      		item.setLabel(TreatyType.values()[i].toString());
      		typeCbx.appendChild(item);
      	}          
    	       
     }
     
     public void onSave(){	
        validateData();	
		String cmd="add";
		String pcmd=(String) params.get("cmd");
		if(pcmd!=null) cmd=pcmd;
		//修改一条协议之后即更新为同类协议最新
		commonTreaty.setDate(new Date());
		if(cmd.equals("add"))
			rowId =CommonTreatyInfoServiceUtil.create(commonTreaty).toString();
		else
		{
			CommonTreatyInfoServiceUtil.update(commonTreaty);
			rowId = commonTreaty.getId();
		}
		refreshParentListbox();
		this.detach();
	}			
	public void refreshParentListbox()
	{
		Listbox listbox = (Listbox) params.get("parentListbox");
		if (listbox != null) {
			List<CommonTreatyInfo> list = (List<CommonTreatyInfo>)listbox.getModel();
			if (list != null) {
				list.clear();
				list.addAll(CommonTreatyInfoServiceUtil.getAll());	
			}
		}
	}

	/**
	 * Cancel this screen
	 * 
	 */
	public void onCancel() {
		this.detach();
	}
	public void onTreatyTypeSelect(org.zkoss.zk.ui.event.SelectEvent evt)
	{
		Iterator items = evt.getSelectedItems().iterator();
		while (items.hasNext()) {
			Comboitem item = (Comboitem) items.next();
			int id = (Integer)item.getValue();
			commonTreaty.setType(id);
		}
	}

	
		
	
    
		
	
	void validateData()
	{
	  	Textbox title=(Textbox)this.getFellow("title");
	  	title.getValue();
	  	Textbox treaty=(Textbox)this.getFellow("treaty");
	  	treaty.getValue();
	  	Combobox type=(Combobox)this.getFellow("type");
	  	type.getValue();
	}
		

}
		
		