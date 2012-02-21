package cccf.ma.web.zk;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.util.List;
import org.zkoss.zk.ui.Page;
import cccf.ma.service.*;
import cccf.ma.model.*;

public class ApplicationenterpriseWindowInit extends org.zkoss.zkplus.databind.AnnotateDataBinderInit {
@SuppressWarnings("unchecked")
public void doAfterCompose(Page page,org.zkoss.zk.ui.Component[] comps)throws Exception {
	
	//当前企业
	EnterpriseInfo enterprise=EnterpriseInfoServiceUtil.getCurrentEnterprise();
 	List list=ApplicationInfoServiceUtil.getApplicationByEnterprise(enterprise.getId());
  	page.setVariable("applicationInfoList",list);
	super.doAfterCompose(page,(org.zkoss.zk.ui.Component[]) comps); 
   }

}