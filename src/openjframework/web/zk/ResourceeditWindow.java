package openjframework.web.zk;
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
import openjframework.service.*;
import openjframework.model.*;

public class ResourceeditWindow extends Window{

     public ResourceInfo resource; 
     Map params = Executions.getCurrent().getArg();
     String userId;
	 String processId = "0";
	 String rowId;
     
     public void onCreate()
     {
    	 resource=(ResourceInfo)this.getPage().getVariable("resource");
    	 
    	       
     }
     
     public void onSave(){	
        validateData();	
		String cmd="add";
		String pcmd=(String) params.get("cmd");
		if(pcmd!=null) cmd=pcmd;
		if(cmd.equals("add"))
			rowId =ResourceInfoServiceUtil.create(resource).toString();
		else
		{
			ResourceInfoServiceUtil.update(resource);
			rowId = resource.getId();
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
	


	public void ontypeSelect(org.zkoss.zk.ui.event.SelectEvent evt) {
		Iterator items = evt.getSelectedItems().iterator();
		while (items.hasNext()) {
			Comboitem item = (Comboitem) items.next();
			String id = item.getValue().toString();
			String qstr="from ResourceTypeInfo where no='"+id+"'";
			List list=ResourceTypeInfoServiceUtil.findByQuery(qstr);			
			ResourceTypeInfo type = new ResourceTypeInfo();
			if(list.size()>0)
			    type=(ResourceTypeInfo)list.get(0);
			resource.setType(type);
		}
	}
			
		
	
    
		
	
	void validateData()
	{
	  			
				Textbox name=(Textbox)this.getFellow("name");
				name.getValue(); 
	  			
				Combobox type=(Combobox)this.getFellow("type");
				type.getValue(); 
	  	
	}
		

}
		
		