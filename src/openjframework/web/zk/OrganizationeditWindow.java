package openjframework.web.zk;
import java.util.*;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import openjframework.service.*;
import openjframework.model.*;

import org.zkoss.zk.ui.*;
import org.zkoss.zul.*;


public class OrganizationeditWindow extends Window{

     public OrganizationInfo organization; 
     Map params = Executions.getCurrent().getArg();
     String userId;
	 String processId = "0";
	 String rowId;
	 Radiogroup  checkCenter;
     public void onCreate()
     {
    	 organization=(OrganizationInfo)this.getPage().getVariable("organization");
    	// Components.wireVariables(this, this);将页面的所有组件加载到类
    	 checkCenter=(Radiogroup)this.getFellow("checkCenter");
    	 checkCenter.setSelectedIndex(organization.isCheckCenter()?0:1);
    	       
     }
     
     public void onSave(){	
        validateData();	
		String cmd="add";
		String pcmd=(String) params.get("cmd");
		if(pcmd!=null) cmd=pcmd;
		organization.setCheckCenter(checkCenter.getSelectedIndex()==0?true:false);
		if(cmd.equals("tree_add"))
		{
			OrganizationInfo _parent=(OrganizationInfo)params.get("organization_parent");
			if(_parent==null)
			{
				organization.setOrganizationParent(null);
				organization.setOrganizationLevel(1);
			}
			else{
				_parent.addChild(organization);
				organization.setOrganizationLevel(_parent.getOrganizationLevel()+1);
			}
			rowId =OrganizationInfoServiceUtil.create(organization).toString();
		}
		else
		{
			OrganizationInfoServiceUtil.update(organization);
			rowId = organization.getId();
		}
		Center c=(Center)this.getParent();
		Tree departTre=(Tree)c.getFellow("orgTre");
		departTre.setModel(null);
		departTre.setTreeitemRenderer(null);
		departTre.setModel(new OrganizationTreeModel());   
		departTre.setTreeitemRenderer(new OrganizationTreeitemRenderer());
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
		
		