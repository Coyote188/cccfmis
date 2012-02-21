package openjframework.web.zk;
import java.util.*;

import openjframework.service.*;
import openjframework.model.*;

import org.zkoss.zk.ui.*;
import org.zkoss.zul.*;


public class ResourceTypeeditWindow extends Window{

     public ResourceTypeInfo resourceType; 
     Map params = Executions.getCurrent().getArg();
     String userId;
	 String processId = "0";
	 String rowId;
     
     public void onCreate()
     {
    	 resourceType=(ResourceTypeInfo)this.getPage().getVariable("resourceType");
    	 
    	       
     }
     
     public void onSave(){	
        validateData();	
		String cmd="add";
		String pcmd=(String) params.get("cmd");
		if(pcmd!=null) cmd=pcmd;
		if(cmd.equals("add"))
			rowId =ResourceTypeInfoServiceUtil.create(resourceType).toString();
		else
		{
			ResourceTypeInfoServiceUtil.update(resourceType);
			rowId = resourceType.getNo();
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
	  	
	}
		

}
		
		