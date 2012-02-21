package cccf.ma.web.zk;

import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import cccf.ma.service.*;
import cccf.ma.model.*;

public class PumperdocumentsViewWindowInit extends
		org.zkoss.zkplus.databind.AnnotateDataBinderInit {

	Map params = Executions.getCurrent().getArg();

	public void doAfterCompose(Page page, org.zkoss.zk.ui.Component[] comps)
			throws Exception {
		ApplicationInfo application = (ApplicationInfo) params
				.get("application");
		PumperDocumentsInfo pumperDocuments = application.getPumperDocuments();

		if (pumperDocuments == null) {
			pumperDocuments = new PumperDocumentsInfo();
		}

		page.setVariable("pumperDocuments", pumperDocuments);
		super.doAfterCompose(page, (org.zkoss.zk.ui.Component[]) comps);

	}
}
