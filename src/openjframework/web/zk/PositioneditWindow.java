package openjframework.web.zk;
import java.util.*;

import openjframework.service.*;
import openjframework.model.*;

import org.zkoss.zk.ui.*;
import org.zkoss.zul.*;


public class PositioneditWindow extends Window{

     public PositionInfo position; 
     Map params = Executions.getCurrent().getArg();
     String userId;
	 String processId = "0";
	 String rowId;
     
     public void onCreate()
     {
    	 position=(PositionInfo)this.getPage().getVariable("position");
    	 
    	       
     }
     
     public void onSave(){	
        validateData();	
		String cmd="add";
		String pcmd=(String) params.get("cmd");
		if(pcmd!=null) cmd=pcmd;
		if(cmd.equals("add"))
			rowId =PositionInfoServiceUtil.create(position).toString();
		else
		{
			PositionInfoServiceUtil.update(position);
			rowId = position.getNo();
		}
		this.detach();
		
		Listbox listbox = (Listbox) params.get("parentListbox");
		if (listbox != null) {
			List<PositionInfo> list = (List<PositionInfo>)listbox.getModel();
			if (list != null) {
				list.clear();
				list.addAll(PositionInfoServiceUtil.getAll());	
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
	

	
		
	
    
		
	
	void validateData()
	{
	  	
	}
		

}
		
		