package openjframework.web.zk;
import java.util.*;
import openjframework.service.*;
import openjframework.model.*;
import org.zkoss.zk.ui.*;
import org.zkoss.zul.*;

public class PoliticalstatuseditWindow extends Window{

     public PoliticalStatusInfo politicalStatus; 
     Map params = Executions.getCurrent().getArg();
     String userId;
	 String processId = "0";
	 String rowId;
     
     public void onCreate()
     {
    	 politicalStatus=(PoliticalStatusInfo)this.getPage().getVariable("politicalStatus");
    	 
    	       
     }
     
     public void onSave(){	
        validateData();	
		String cmd="add";
		String pcmd=(String) params.get("cmd");
		if(pcmd!=null) cmd=pcmd;
		if(cmd.equals("add"))
			rowId =PoliticalStatusInfoServiceUtil.create(politicalStatus).toString();
		else
		{
			PoliticalStatusInfoServiceUtil.update(politicalStatus);
			rowId = politicalStatus.getNo();
		}
		this.detach();
		Listbox listbox = (Listbox) params.get("parentListbox");
		if (listbox != null) {
			List<PoliticalStatusInfo> list = (List<PoliticalStatusInfo>)listbox.getModel();
			if (list != null) {
				list.clear();
				list.addAll(PoliticalStatusInfoServiceUtil.getAll());	
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
		
		