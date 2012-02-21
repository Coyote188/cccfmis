package cccfmis.bpm.zk;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zkplus.databind.AnnotateDataBinderInit;

import cccf.ma.model.ApplicationInfo;
import cccf.ma.service.ApplicationInfoServiceUtil;

public class FactoryAppListInit extends AnnotateDataBinderInit {
	List<ApplicationInfo> appList;

	public void doAfterCompose(Page page, Component[] comps) throws Exception {
		appList = ApplicationInfoServiceUtil.getAll();
		page.setAttribute("appList", appList);
		super.doAfterCompose(page, comps);
	}
}