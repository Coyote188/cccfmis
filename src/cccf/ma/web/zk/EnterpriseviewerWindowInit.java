package cccf.ma.web.zk;
import java.util.List;
import java.util.Map;

import openjframework.model.UserInfo;
import openjframework.service.UserInfoServiceUtil;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import cccf.ma.service.*;
import cccf.ma.model.*;

public class EnterpriseviewerWindowInit extends org.zkoss.zkplus.databind.AnnotateDataBinderInit{
 
	 Map params = Executions.getCurrent().getArg();
	 EnterpriseInfo enterprise;
	 
	  public void doAfterCompose(Page page,org.zkoss.zk.ui.Component[] comps)throws Exception {
		 enterprise=(EnterpriseInfo)params.get("enterprise");		 
	
		
		 if(enterprise==null){
		 	enterprise=new EnterpriseInfo();
		 }
		 
		 	  					
		 List<ManufactureInfo> manufactureList=ManufactureInfoServiceUtil.getAll();
		 page.setVariable("manufactureList",manufactureList);		 
	   	  					
		 List<UserInfo> accountList=UserInfoServiceUtil.getAll();
		 page.setVariable("accountList",accountList);		 
	   
	   page.setVariable("enterprise", enterprise);
	   super.doAfterCompose(page,(org.zkoss.zk.ui.Component[]) comps); 
	     
	  }
 }
    