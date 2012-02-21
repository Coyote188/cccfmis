package cccf.ma.web.zk;
import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import cccf.ma.service.*;
import cccf.ma.model.*;

public class CommontreatyeditWindowInit extends org.zkoss.zkplus.databind.AnnotateDataBinderInit{
 
	 Map params = Executions.getCurrent().getArg();
	 CommonTreatyInfo commonTreaty;
	 
	  public void doAfterCompose(Page page,org.zkoss.zk.ui.Component[] comps)throws Exception {
		 commonTreaty=(CommonTreatyInfo)params.get("commonTreaty");		 
	
		
		 if(commonTreaty==null){
		 	commonTreaty=new CommonTreatyInfo();
		 }
		 
		 
	   page.setVariable("commonTreaty", commonTreaty);
	   super.doAfterCompose(page,(org.zkoss.zk.ui.Component[]) comps); 
	     
	  }
 }
    