package openjframework.web.zk;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.util.List;
import org.zkoss.zk.ui.Page;
import openjframework.service.*;
import openjframework.model.*;

public class PermissionmanageWindowInit extends org.zkoss.zkplus.databind.AnnotateDataBinderInit {
public void doAfterCompose(Page page,org.zkoss.zk.ui.Component[] comps)throws Exception {
 	List list=PermissionInfoServiceUtil.getAll();
  	page.setVariable("permissionInfoList",list);
			  	
	super.doAfterCompose(page,(org.zkoss.zk.ui.Component[]) comps); 
   }

}