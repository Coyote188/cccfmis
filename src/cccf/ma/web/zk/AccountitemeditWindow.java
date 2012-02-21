package cccf.ma.web.zk;
import java.util.*;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import openjframework.service.*;
import openjframework.model.*;

import org.zkoss.zk.ui.*;
import org.zkoss.zul.*;
import org.zkoss.zk.ui.event.InputEvent;

import com.aidi.bpm.service.*;
import com.aidi.core.spring.*;
import com.aidi.core.zk.*;
import cccf.ma.service.*;
import cccf.ma.model.*;

public class AccountitemeditWindow extends Window{

     public AccountItemInfo accountItem; 
     Map params = Executions.getCurrent().getArg();
     String userId;
	 String processId = "0";
	 String rowId;
     
     public void onCreate()
     {
    	 accountItem=(AccountItemInfo)this.getPage().getVariable("accountItem");
    	 System.out.println(accountItem.getAccountType());
    	 if(accountItem.getAccountType()!=null)
    	 {
        	 boolean editStatus =accountItem.getAccountType().getIsCustom();
        	 Combobox accountType=(Combobox)getFellow("accountType");
        	 accountType.setButtonVisible(editStatus);
        	 Textbox name=(Textbox)getFellow("name");
        	 name.setReadonly(!editStatus);
    	 }
    	 
    	       
     }
     
     public void onSave(){	
        validateData();	
		String cmd="add";
		String pcmd=(String) params.get("cmd");
		if(pcmd!=null) cmd=pcmd;
		if(cmd.equals("add"))
			rowId =AccountItemInfoServiceUtil.create(accountItem).toString();
		else
		{
			AccountItemInfoServiceUtil.update(accountItem);
			rowId = accountItem.getAccountItemId();
		}
		this.detach();
	}			


	/**
	 * Cancel this screen
	 * 
	 */
	public void onCancel() {
		this.detach();
	}
	


	public void onaccountTypeSelect(org.zkoss.zk.ui.event.SelectEvent evt) {
		Iterator items = evt.getSelectedItems().iterator();
		while (items.hasNext()) {
			Comboitem item = (Comboitem) items.next();
			String id = item.getValue().toString();
			String qstr="from AccountTypeInfo where atypeId='"+id+"'";
			List list=AccountTypeInfoServiceUtil.findByQuery(qstr);			
			AccountTypeInfo accountType = new AccountTypeInfo();
			if(list.size()>0)
			    accountType=(AccountTypeInfo)list.get(0);
			accountItem.setAccountType(accountType);
		}
	}
			
		
	
    
		
	
	void validateData()
	{
	  			
				Combobox accountType=(Combobox)this.getFellow("accountType");
				accountType.getValue(); 
	  			
				Textbox name=(Textbox)this.getFellow("name");
				name.getValue(); 
	  	
	}
		

}
		
		