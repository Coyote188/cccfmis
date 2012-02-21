package cccf.ma.web.zk;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.util.List;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import cccf.ma.service.*;
import cccf.ma.model.*;

public class ProductionmodelListViewWindowInit extends org.zkoss.zkplus.databind.AnnotateDataBinderInit {
public void doAfterCompose(Page page,org.zkoss.zk.ui.Component[] comps)throws Exception {

	Map appParams = Executions.getCurrent().getArg();
	ApplicationInfo application = (ApplicationInfo) appParams.get("application");
	List list=new ArrayList();
	list.addAll(application.getProductionModel());
  	page.setVariable("productionModelInfoList",list);
			  	
	super.doAfterCompose(page,(org.zkoss.zk.ui.Component[]) comps); 
   }

}