package cccf.ma.web.zk;

import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import cccf.ma.service.*;
import cccf.ma.model.*;

public class AppstatusRecordApproveWindowInit extends
		org.zkoss.zkplus.databind.AnnotateDataBinderInit {

	Map params = Executions.getCurrent().getArg();
	AppStatusRecordInfo appStatusRecord;

	public void doAfterCompose(Page page, org.zkoss.zk.ui.Component[] comps)
			throws Exception {
		

		if (params.get("rowId") != null) {
			String rowId = params.get("rowId").toString();
			appStatusRecord = AppStatusRecordInfoServiceUtil.getById(rowId);			
		}
		
		if(appStatusRecord!=null){
			ApplicationInfo application=appStatusRecord.getApplication();
			page.setVariable("inspectionApplication", application);
		}

		page.setVariable("appStatusRecord", appStatusRecord);
		super.doAfterCompose(page, (org.zkoss.zk.ui.Component[]) comps);

	}
}
