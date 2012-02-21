package openjframework.web.zk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Window;

import cccf.ma.model.EnterpriseInfo;
import cccf.ma.service.EnterpriseInfoServiceUtil;

@SuppressWarnings( { "serial", "unchecked" })
public class MsgEnterpriseListController extends GenericForwardComposer {

	public static final String ON_RELOAD = "onReLoad";
	public static final String ON_CHECKALL = "onCheckAll";

	private List<EnterpriseInfo> eList;
	Map param = Executions.getCurrent().getArg();

	private Listbox lbxEList;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		setEList((List<EnterpriseInfo>) param.get("enterprises"));

	}

	public void onReLoad(Event event) {
		setEList((List<EnterpriseInfo>) event.getData());
		if (!(eList == null)) {
			ListModel model = new SimpleListModel(eList);
			lbxEList.setModel(model);
		}
	}

	private Window frmNoticeEditer;

	public void onClick$btnOk() {
		List<EnterpriseInfo> eList = new ArrayList<EnterpriseInfo>();
		Set<Listitem> items = lbxEList.getSelectedItems();
		if (!items.isEmpty()) {
			for (Listitem item : items) {
				eList.add((EnterpriseInfo) item.getValue());
			}
		}

		Events.postEvent(MsgSenderController.ON_RELOAD_E_LIST, frmNoticeEditer,
				eList);
		try {
			Messagebox.show("企业成功添加到消息发送列表中!");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void onCheckAll() {
		List<Listitem> ltmEn = lbxEList.getItems();
		for (Listitem item : ltmEn) {
			item.setSelected(true);
		}
	}

	public void setEList(List<EnterpriseInfo> eList) {
		this.eList = eList;
	}

	public List<EnterpriseInfo> getEList() {
		return eList;
	}

}
