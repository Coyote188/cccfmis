package openjframework.web.zk;
import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import openjframework.service.*;
import openjframework.model.*;

public class OrganizationeditWindowInit extends org.zkoss.zkplus.databind.AnnotateDataBinderInit{
 
	 Map params = Executions.getCurrent().getArg();
	 OrganizationInfo organization;
	 
	  public void doAfterCompose(Page page,org.zkoss.zk.ui.Component[] comps)throws Exception {
		  organization=(OrganizationInfo)params.get("organization");		 
	
		
		 if(organization==null){
		 	organization=new OrganizationInfo();
		 	String organizationNo=(String)params.get("organizationNo");
		 	organization.setOrganizationNo(organizationNo);
		 }
		
	   page.setVariable("organization", organization);
	   super.doAfterCompose(page,(org.zkoss.zk.ui.Component[]) comps); 
	     
	  }
 }
    