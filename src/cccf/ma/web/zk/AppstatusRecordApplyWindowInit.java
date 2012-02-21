package cccf.ma.web.zk;

import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import cccf.ma.service.*;
import cccf.ma.model.*;

public class AppstatusRecordApplyWindowInit extends
		org.zkoss.zkplus.databind.AnnotateDataBinderInit {

	Map params = Executions.getCurrent().getArg();
	AppStatusRecordInfo appStatusRecord;

	public void doAfterCompose(Page page, org.zkoss.zk.ui.Component[] comps)
			throws Exception {

		appStatusRecord = (AppStatusRecordInfo) params.get("appStatusRecord");

		if (appStatusRecord == null) {
			appStatusRecord = new AppStatusRecordInfo();
		}

		page.setVariable("appStatusRecord", appStatusRecord);
		super.doAfterCompose(page, (org.zkoss.zk.ui.Component[]) comps);

	}
}
