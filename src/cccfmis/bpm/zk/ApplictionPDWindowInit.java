package cccfmis.bpm.zk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import openjframework.bpm.model.TaskInstanceInfo;
import openjframework.bpm.service.TaskInstanceInfoServiceUtil;
import openjframework.model.UserInfo;
import openjframework.service.UserInfoServiceUtil;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zul.Messagebox;

import cccf.ma.model.ApplicationInfo;
import cccf.ma.service.ApplicationInfoServiceUtil;
import cccf.ma.service.ProductCatalogueInfoServiceUtil;

public class ApplictionPDWindowInit extends org.zkoss.zkplus.databind.AnnotateDataBinderInit {

	Map params = Executions.getCurrent().getArg();
	ApplicationInfo application;

	public void doAfterCompose(Page page, org.zkoss.zk.ui.Component[] comps) throws Exception {

		String pid = "";

		if (params.get("rowId") != null) {
			String rowId = params.get("rowId").toString();
			application = ApplicationInfoServiceUtil.getById(rowId);
			if (application != null)
				pid = application.getProduction().getId();
			if (params.get("taskInstanceId") != null) {
				long tid = Long.valueOf(params.get("taskInstanceId").toString());
				List<TaskInstanceInfo> taskInstanceInfoList = TaskInstanceInfoServiceUtil.getTaskInstanceInfoList(rowId, tid);
				page.setVariable("taskInstanceInfoList", taskInstanceInfoList);
			}

		}

		List reviewUserList = ProductCatalogueInfoServiceUtil.getUserArrayByIdAndType(pid, 0);
		page.setAttribute("reviewUserList", reviewUserList);

		List reportUsers = ProductCatalogueInfoServiceUtil.getUserArrayByIdAndType(pid, 0);
		page.setAttribute("reportUsers", reviewUserList);
		List<UserInfo> userList = new ArrayList<UserInfo>();
		if (application.getReportUser() != null) {
			String userid[] = application.getReportUser().split(",");
			for (int m = 0; m < userid.length; m++) {
				UserInfo userInfo = UserInfoServiceUtil.getById(userid[m]);
				if (userInfo != null)
					userList.add(userInfo);
			}
		}
		page.setAttribute("reportUsers", userList);

		page.setVariable("inspectionApplication", application);

		// 前一任务
		if (params.get("taskInstanceId") != null) {
			Long taskInstanceId = Long.parseLong(params.get("taskInstanceId").toString());
			TaskInstanceInfo preTaskInstanceInfo = TaskInstanceInfoServiceUtil.getPreTaskInstanceInfo(taskInstanceId);
			page.setVariable("preTaskInstanceInfo", preTaskInstanceInfo);
		}
		super.doAfterCompose(page, (org.zkoss.zk.ui.Component[]) comps);

	}
}
