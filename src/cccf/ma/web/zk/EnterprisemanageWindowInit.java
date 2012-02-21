package cccf.ma.web.zk;

import java.util.List;
import java.util.Map;

import openjframework.model.UserInfo;
import openjframework.service.UserInfoServiceUtil;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import cccf.ma.service.*;
import cccf.ma.model.*;

@SuppressWarnings( { "deprecation", "unchecked" })
public class EnterprisemanageWindowInit extends
		org.zkoss.zkplus.databind.AnnotateDataBinderInit {

	Map params = Executions.getCurrent().getArg();
	private EnterpriseInfo enterprise;
	private UserInfo user;

	public void doAfterCompose(Page page, org.zkoss.zk.ui.Component[] comps)
			throws Exception {
		user = UserInfoServiceUtil.getCurrentLoginUser();
		String queryString = "from EnterpriseInfo e where e.account = '"
				+ user.getId() + "'";
		enterprise = (EnterpriseInfo) EnterpriseInfoServiceUtil.findByQuery(
				queryString).get(0);
		page.setVariable("enterprise", enterprise);
		page.setVariable("user", user);
		super.doAfterCompose(page, (org.zkoss.zk.ui.Component[]) comps);

	}
}
