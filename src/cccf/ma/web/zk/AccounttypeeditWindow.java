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

public class AccounttypeeditWindow extends Window{

     public AccountTypeInfo accountType; 
     Map params = Executions.getCurrent().getArg();
     String userId;
	 String processId = "0";
	 String rowId;
     
     public void onCreate()
     {
    	 accountType=(AccountTypeInfo)this.getPage().getVariable("accountType");
    	 
    	Checkbox checkboxisCustom=(Checkbox)this.getFellow("isCustom");
    	checkboxisCustom.setChecked(accountType.getIsCustom()); 
    	       
     }
     
     public void onSave(){	
        validateData();	
		String cmd="add";
		String pcmd=(String) params.get("cmd");
		if(pcmd!=null) cmd=pcmd;
		if(cmd.equals("add"))
			rowId =AccountTypeInfoServiceUtil.create(accountType).toString();
		else
		{
			AccountTypeInfoServiceUtil.update(accountType);
			rowId = accountType.getAtypeId();
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
	

	
		
	
    
		
	
	void validateData()
	{
	  			
				Textbox name=(Textbox)this.getFellow("name");
				name.getValue(); 
	  	
	}
		

}
		
		