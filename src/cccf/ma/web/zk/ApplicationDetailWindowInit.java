package cccf.ma.web.zk;

import java.util.List;
import java.util.Map;

import openjframework.bpm.model.TaskInstanceInfo;
import openjframework.bpm.service.TaskInstanceInfoServiceUtil;
import openjframework.model.UserInfo;
import openjframework.service.UserInfoServiceUtil;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import cccf.ma.service.*;
import cccf.ma.model.*;

public class ApplicationDetailWindowInit extends
		org.zkoss.zkplus.databind.AnnotateDataBinderInit {

	Map params = Executions.getCurrent().getArg();
	ApplicationInfo application;

	public void doAfterCompose(Page page, org.zkoss.zk.ui.Component[] comps)
			throws Exception {

		String pid = "";

		if (params.get("rowId") != null) {
			String rowId = params.get("rowId").toString();
			application = ApplicationInfoServiceUtil.getById(rowId);
			if (application != null)
				pid = application.getProduction().getId();

			List<TaskInstanceInfo> taskInstanceInfoList = TaskInstanceInfoServiceUtil
					.getTaskInstanceInfoList("ApplicationInfo", rowId);
			page.setVariable("taskInstanceInfoList", taskInstanceInfoList);

		}

		page.setVariable("inspectionApplication", application);

		super.doAfterCompose(page, (org.zkoss.zk.ui.Component[]) comps);

	}
}
