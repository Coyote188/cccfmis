package openjframework.web.zk;

import java.util.HashMap;
import java.util.Map;

import openjframework.model.OrganizationInfo;
import openjframework.service.OrganizationInfoServiceUtil;
import openjframework.util.NextCodeCalculate;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Center;
import org.zkoss.zul.Div;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;

public class OrganizationTreeitemRenderer implements TreeitemRenderer {
	
	HashMap params = new HashMap();
	OrganizationInfo _organization;
	Menupopup pupop1 = new Menupopup();// 定义一个右键菜单
	Menuitem menu, menu1,menu2,menu3;
	
	Div treeDiv;
	
	public OrganizationTreeitemRenderer()
	{
		menu = new Menuitem("新增同级单位");
		menu.setImage("/image/common/sharing.png");
		menu.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				params.clear();
				params.put("organization_parent",_organization.getOrganizationParent());
				params.put("cmd", "tree_add");
				String nextCode = NextCodeCalculate.nextCode(_organization);
				params.put("organizationNo", nextCode);
				openFreeWindow(params);

			}

		});

		menu1 = new Menuitem("增加子级单位");
		menu1.setImage("/image/common/signal_instance.png");
		menu1.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				params.clear();
				params.put("organization_parent", _organization);
				params.put("cmd", "tree_add");
				String nextCode = NextCodeCalculate.nextCodeForChild(_organization);
				params.put("organizationNo", nextCode);
				openFreeWindow(params);

			}

		});

		menu2 = new Menuitem("查看/修改单位信息");
		menu2.setImage("/image/common/edit.png");
		menu2.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				params.clear();
				params.put("organization",_organization);
				params.put("cmd", "tree_edit");
				openFreeWindow(params);

			}

		});
		
		menu3 = new Menuitem("删除该单位");
		menu3.setImage("/image/common/close.png");
		menu3.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				if (Messagebox.show("是否要删除["
						+_organization.getOrganizationName(),"] 删除?",
						Messagebox.OK | Messagebox.NO, Messagebox.QUESTION) == Messagebox.OK) {
					OrganizationInfoServiceUtil.delete(_organization);
					// 刷新树
					Tree departTre=(Tree)menu.getFellow("orgTre");
					departTre.setModel(null);
					departTre.setTreeitemRenderer(null);
					departTre.setModel(new OrganizationTreeModel());   
					departTre.setTreeitemRenderer(new OrganizationTreeitemRenderer());
				}
			}

		});
		pupop1.appendChild(menu);// 设置右键菜单项
		pupop1.appendChild(menu1);
		pupop1.appendChild(menu2);
		pupop1.appendChild(menu3);
	}
	/**
	 * 打开窗口
	 * 
	 * @param params
	 */
	public void openFreeWindow(Map params) {
		Window objWindow = (Window) Executions.createComponents(
				"/SysForm/SystemAdministrator/organizationInfo.zul", null, params);
		Center c = (Center) menu.getFellow("orgCtr");
		if (c.getChildren().size() > 0) {
			c.getFirstChild().detach();
		}
		objWindow.setContentStyle("overflow:auto");
		objWindow.setSizable(false);
		objWindow.setParent(c);
		objWindow.doEmbedded();
	}
	
	@Override
	public void render(final Treeitem item, Object data) throws Exception {
		final OrganizationInfo organization = (OrganizationInfo) data;
		treeDiv = (Div) item.getFellow("treeDiv");
		pupop1.setParent(treeDiv);
		item.setContext(pupop1);
		
		item.setValue(organization);

		Treecell tc = new Treecell(organization.getOrganizationName());
		tc.setTooltiptext(organization.getOrganizationName());
		Treerow tr = null;
		if (item.getTreerow() == null) {
			tr = new Treerow();
			tr.setParent(item);
		} else {
			tr = item.getTreerow();
			tr.getChildren().clear();
		}
		tc.setParent(tr);
		item.addEventListener(Events.ON_RIGHT_CLICK, new EventListener() {

			@Override
			public void onEvent(Event arg0) throws Exception {

				_organization = organization;
			}

		});
		item.setOpen(true);
	}
}