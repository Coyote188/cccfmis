package openjframework.web.zk;
import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import openjframework.service.*;
import openjframework.model.*;

public class ResourceTypeeditWindowInit extends org.zkoss.zkplus.databind.AnnotateDataBinderInit{
 
	 Map params = Executions.getCurrent().getArg();
	 ResourceTypeInfo resourceType;
	 
	  public void doAfterCompose(Page page,org.zkoss.zk.ui.Component[] comps)throws Exception {
		 resourceType=(ResourceTypeInfo)params.get("resourceType");		 
	
		
		 if(resourceType==null){
		 	resourceType=new ResourceTypeInfo();
		 }
		 
		 
	   page.setVariable("resourceType", resourceType);
	   super.doAfterCompose(page,(org.zkoss.zk.ui.Component[]) comps); 
	     
	  }
 }
    