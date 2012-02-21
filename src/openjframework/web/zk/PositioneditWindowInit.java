package openjframework.web.zk;
import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import openjframework.service.*;
import openjframework.model.*;

public class PositioneditWindowInit extends org.zkoss.zkplus.databind.AnnotateDataBinderInit{
 
	 Map params = Executions.getCurrent().getArg();
	 PositionInfo position;
	 
	  public void doAfterCompose(Page page,org.zkoss.zk.ui.Component[] comps)throws Exception {
		 position=(PositionInfo)params.get("position");		 
	
		
		 if(position==null){
		 	position=new PositionInfo();
		 }
		 
		 
	   page.setVariable("position", position);
	   super.doAfterCompose(page,(org.zkoss.zk.ui.Component[]) comps); 
	     
	  }
 }
    