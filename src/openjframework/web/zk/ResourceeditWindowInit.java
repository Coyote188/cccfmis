package openjframework.web.zk;
import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import openjframework.service.*;
import openjframework.model.*;

public class ResourceeditWindowInit extends org.zkoss.zkplus.databind.AnnotateDataBinderInit{
 
	 Map params = Executions.getCurrent().getArg();
	 ResourceInfo resource;
	 
	  public void doAfterCompose(Page page,org.zkoss.zk.ui.Component[] comps)throws Exception {
		 resource=(ResourceInfo)params.get("resource");		 
	
		
		 if(resource==null){
		 	resource=new ResourceInfo();
		 }
		 
		 	  					
		 List<ResourceTypeInfo> typeList=ResourceTypeInfoServiceUtil.getAll();
		 page.setVariable("typeList",typeList);		 
	   
	   page.setVariable("resource", resource);
	   super.doAfterCompose(page,(org.zkoss.zk.ui.Component[]) comps); 
	     
	  }
 }
    