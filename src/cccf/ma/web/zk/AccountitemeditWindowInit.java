package cccf.ma.web.zk;
import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import cccf.ma.service.*;
import cccf.ma.model.*;

public class AccountitemeditWindowInit extends org.zkoss.zkplus.databind.AnnotateDataBinderInit{
 
	 Map params = Executions.getCurrent().getArg();
	 AccountItemInfo accountItem;
	 
	  public void doAfterCompose(Page page,org.zkoss.zk.ui.Component[] comps)throws Exception {
		 accountItem=(AccountItemInfo)params.get("accountItem");		 
	
		
		 if(accountItem==null){
		 	accountItem=new AccountItemInfo();
		 }
		 
		 	  					
		 List<AccountTypeInfo> accountTypeList=AccountTypeInfoServiceUtil.getAll();
		 page.setVariable("accountTypeList",accountTypeList);		 
	   
	   page.setVariable("accountItem", accountItem);
	   super.doAfterCompose(page,(org.zkoss.zk.ui.Component[]) comps); 
	     
	  }
 }
    