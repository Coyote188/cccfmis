package cccf.ma.web.zk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import openjframework.service.UserInfoServiceUtil;
import openjframework.util.NextCodeCalculate;

import org.jbpm.taskmgmt.exe.TaskInstance;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Center;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Window;

import com.aidi.bpm.service.BpmUtil;

import cccf.ma.model.ApplicationInfo;
import cccf.ma.model.ProductCatalogueInfo;
import cccf.ma.service.ApplicationInfoServiceUtil;
import cccf.ma.service.ProductCatalogueInfoServiceUtil;

public class ApplicationTreeViewWindow extends GenericForwardComposer {
	private Tree procTre;
	
	Window applicationTreeViewWindow;
	HashMap params = new HashMap();
	Listbox applicationInfoListbox;
	String productionId = "";

	

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		procTre.setModel(new ProductCatalogueTree());
		procTre.setTreeitemRenderer(new ApplicationTreeitemRenderer(
				applicationTreeViewWindow));
	}

	public void onClickTree(ForwardEvent event) {
		productionId = event.getData().toString();

		List plist = ApplicationInfoServiceUtil
				.getApplicationListSubmitByCatalogue(productionId);
		ListModel model = new SimpleListModel(plist);
		applicationInfoListbox.setModel(model);

	}

	public void onAccept() {
		if (applicationInfoListbox.getSelectedItem() == null) {
			try {
				Messagebox.show("请选择一条记录!", "操作提示", Messagebox.OK,
						Messagebox.ERROR);
				return;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String aid = applicationInfoListbox.getSelectedItem().getValue()
				.toString();
		ApplicationInfo app = ApplicationInfoServiceUtil.getById(aid);
		TaskInstance ti = BpmUtil.getCurrentTaskInstance(app
				.getProcessInstanceId());

		Map params = new HashMap();
		params.put("taskInstanceId", ti.getId());
		params.put("entityName", "ApplicationInfo");
		params.put("rowId", app.getId());
		params.put("userId", UserInfoServiceUtil.getCurrentLoginUser().getId());

		openURLFormWindow(params);
	}

	public void openURLFormWindow(Map params) {

		String formUrl = "application-accept.zul";
		Window objWindow = (Window) Executions.createComponents(formUrl, null,
				params);
		try {

			objWindow.doModal();
			// refreshListbox();
			refreshListbox();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void refreshListbox() {
		List plist = null;
		if (productionId != null && !productionId.equals("")) {
			plist = ApplicationInfoServiceUtil
					.getApplicationListSubmitByCatalogue(productionId);
		}else{
			plist = ApplicationInfoServiceUtil.getApplicationListSubmit();
		}
		ListModel model = new SimpleListModel(plist);
		applicationInfoListbox.setModel(model);
	}

}
