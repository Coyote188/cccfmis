package openjframework.web.zk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import openjframework.model.OrganizationInfo;
import openjframework.util.NextCodeCalculate;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Center;
import org.zkoss.zul.Div;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;

public class OrganizationTreeViewController extends GenericForwardComposer {

	private static final long serialVersionUID = -8456707370023511517L;
	Window orgWin;
	private Tree orgTre;
	private Div treeDiv;
	Center orgCtr;
	Menupopup pupop;
	Menuitem menu;
	HashMap params = new HashMap();

	public OrganizationTreeViewController() {
		pupop = new Menupopup();
		menu = new Menuitem("新增单位");
		menu.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				params.put("cmd", "tree_add");
				String nextCode = NextCodeCalculate.nextCode_department();
				params.put("organizationNo", nextCode);
				openFreeWindow(params);
			}
		});
		pupop.appendChild(menu);
	}

	/**
	 * 打开窗口
	 * 
	 * @param params
	 */
	public void openFreeWindow(Map params) {
		Window objWindow = (Window) Executions.createComponents(
				"/SysForm/SystemAdministrator/organizationInfo.zul", null, params);
		if (orgCtr.getChildren().size() > 0) {
			orgCtr.getFirstChild().detach();
		}
		objWindow.setContentStyle("overflow:auto");
		objWindow.setSizable(false);
		objWindow.setParent(orgCtr);
		objWindow.doEmbedded();
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		orgTre.setModel(new OrganizationTreeModel());
		orgTre.setTreeitemRenderer(new OrganizationTreeitemRenderer());
		pupop.setParent(treeDiv);
		orgTre.setContext(pupop);
	}
}
