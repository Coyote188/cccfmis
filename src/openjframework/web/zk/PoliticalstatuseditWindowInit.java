package openjframework.web.zk;
import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import openjframework.service.*;
import openjframework.model.*;

public class PoliticalstatuseditWindowInit extends org.zkoss.zkplus.databind.AnnotateDataBinderInit{
 
	 Map params = Executions.getCurrent().getArg();
	 PoliticalStatusInfo politicalStatus;
	 
	  public void doAfterCompose(Page page,org.zkoss.zk.ui.Component[] comps)throws Exception {
		 politicalStatus=(PoliticalStatusInfo)params.get("politicalStatus");		 
	
		
		 if(politicalStatus==null){
		 	politicalStatus=new PoliticalStatusInfo();
		 }
		 
		 
	   page.setVariable("politicalStatus", politicalStatus);
	   super.doAfterCompose(page,(org.zkoss.zk.ui.Component[]) comps); 
	     
	  }
 }
    