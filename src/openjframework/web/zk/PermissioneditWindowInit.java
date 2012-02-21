package openjframework.web.zk;

import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import openjframework.service.*;
import openjframework.model.*;

public class PermissioneditWindowInit extends
		org.zkoss.zkplus.databind.AnnotateDataBinderInit {

	Map params = Executions.getCurrent().getArg();
	PermissionInfo permission;

	public void doAfterCompose(Page page, org.zkoss.zk.ui.Component[] comps)
			throws Exception {

		ResourceInfo resource = (ResourceInfo) params.get("resource");
		if (resource != null) {
			permission = PermissionInfoServiceUtil.getByResourceId(resource
					.getId());			
		}

		if (permission == null) {
			permission = new PermissionInfo();
			permission.setResource(resource);
		}

		List<RoleInfo> roleListList = RoleInfoServiceUtil.getAll();
		page.setVariable("roleListList", roleListList);

		page.setVariable("permission", permission);
		super.doAfterCompose(page, (org.zkoss.zk.ui.Component[]) comps);

	}
}
