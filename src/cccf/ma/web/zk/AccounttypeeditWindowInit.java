package cccf.ma.web.zk;
import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import cccf.ma.service.*;
import cccf.ma.model.*;

public class AccounttypeeditWindowInit extends org.zkoss.zkplus.databind.AnnotateDataBinderInit{
 
	 Map params = Executions.getCurrent().getArg();
	 AccountTypeInfo accountType;
	 
	  public void doAfterCompose(Page page,org.zkoss.zk.ui.Component[] comps)throws Exception {
		 accountType=(AccountTypeInfo)params.get("accountType");		 
	
		
		 if(accountType==null){
		 	accountType=new AccountTypeInfo();
		 }
		 
		 
	   page.setVariable("accountType", accountType);
	   super.doAfterCompose(page,(org.zkoss.zk.ui.Component[]) comps); 
	     
	  }
 }
    