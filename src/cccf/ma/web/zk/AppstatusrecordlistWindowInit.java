package cccf.ma.web.zk;

import java.math.*;
import java.util.*;
import java.sql.*;
import java.util.List;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import cccf.ma.service.*;
import cccf.ma.model.*;

public class AppstatusrecordlistWindowInit extends
		org.zkoss.zkplus.databind.AnnotateDataBinderInit {
	
	Map params = Executions.getCurrent().getArg();
	
	public void doAfterCompose(Page page, org.zkoss.zk.ui.Component[] comps)
			throws Exception {

		String rowId = params.get("rowId").toString();
		
		List list = AppStatusRecordInfoServiceUtil.getListByApplication(rowId);
		page.setVariable("appStatusRecordInfoList", list);

		super.doAfterCompose(page, (org.zkoss.zk.ui.Component[]) comps);
	}

}