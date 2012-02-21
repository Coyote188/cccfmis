package cccf.ma.web.zk;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.util.List;
import org.zkoss.zk.ui.Page;
import cccf.ma.service.*;
import cccf.ma.model.*;

public class ProductactivateapproveWindowInit extends org.zkoss.zkplus.databind.AnnotateDataBinderInit {
public void doAfterCompose(Page page,org.zkoss.zk.ui.Component[] comps)throws Exception {
 	List list=EnterpriseOwnActivatedProductListInfoServiceUtil.findNotActivateAll();
  	page.setVariable("enterpriseOwnActivatedProductListInfoList",list);
			  	
	super.doAfterCompose(page,(org.zkoss.zk.ui.Component[]) comps); 
   }

}