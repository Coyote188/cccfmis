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

public class ApplicationapplyWindowInit extends
		org.zkoss.zkplus.databind.AnnotateDataBinderInit {

	Map params = Executions.getCurrent().getArg();
	ApplicationInfo application;

	public void doAfterCompose(Page page, org.zkoss.zk.ui.Component[] comps)
			throws Exception {
		application = (ApplicationInfo) params.get("application");

		if (application == null) {
			application = new ApplicationInfo();
			application.setStauts(0);// 初始状态为暂存
		} else {
			Long processInstanceId = application.getProcessInstanceId();
			if (processInstanceId != null)
				if (processInstanceId > 0) {
					List<TaskInstanceInfo> taskInstanceInfoList = TaskInstanceInfoServiceUtil
							.getEndedTaskInstanceInfoList(application.getId(),
									processInstanceId);
					page.setAttribute("taskInstanceInfoList",
							taskInstanceInfoList);
				}
		}

		// 本企业已激活的产品
		List<ProductCatalogueInfo> productionList = ProductCatalogueInfoServiceUtil
				.getCurrentEnterpriseActivatedProduct();
		page.setVariable("productionList", productionList);
		// 本企业已激活的生产企业
		UserInfo user = UserInfoServiceUtil.getCurrentLoginUser();
		List<ProductionEnterpriseInfo> productionEnterpriseList = ProductionEnterpriseInfoServiceUtil
				.getActivedProductionEnterpriseByUserId(user.getId());
		page.setVariable("productionEnterpriseList", productionEnterpriseList);
		// 本企业已激活的制造商
		List<ManufactureInfo> manufactureList = ManufactureInfoServiceUtil
				.getActivedManufactureByUserId(user.getId());
		page.setVariable("manufactureList", manufactureList);

		page.setAttribute("inspectionApplication", application);
		super.doAfterCompose(page, (org.zkoss.zk.ui.Component[]) comps);

	}
}
