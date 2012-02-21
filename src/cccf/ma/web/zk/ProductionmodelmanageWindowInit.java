package cccf.ma.web.zk;

import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;

import cccf.ma.model.ApplicationInfo;
import cccf.ma.model.EnterpriseProductModel;
import cccf.ma.service.EnterpriseProductModelServiceUtil;

public class ProductionmodelmanageWindowInit extends org.zkoss.zkplus.databind.AnnotateDataBinderInit {
	public void doAfterCompose(Page page, org.zkoss.zk.ui.Component[] comps) throws Exception {
		Map appParams = Executions.getCurrent().getArg();
		ApplicationInfo application = (ApplicationInfo) appParams.get("application");
		if (application != null) {
			@SuppressWarnings("unchecked")
			List<EnterpriseProductModel> list = EnterpriseProductModelServiceUtil.findByQuery("from EnterpriseProductModel e where e.enterprise='"
					+ application.getEnterprise().getId() + "' and e.product='" + application.getProduction().getId() + "'");
			page.setAttribute("productionModelList", list);
		}
		super.doAfterCompose(page, (org.zkoss.zk.ui.Component[]) comps);
	}

}