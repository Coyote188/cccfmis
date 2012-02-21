package cccf.ma.web.zk;

import java.util.HashMap;
import java.util.Map;

import openjframework.util.NextCodeCalculate;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Center;
import org.zkoss.zul.Div;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Menuseparator;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;

import cccf.ma.model.ProductCatalogueInfo;
import cccf.ma.service.ProductCatalogueInfoServiceUtil;
import cccf.ma.service.ProductCatalogueTreeService;

public class ApplicationTreeitemRenderer implements TreeitemRenderer {

	ProductCatalogueInfo _product;

	Div treeDiv;
	
	Window applicationTreeViewWindow;

	public ApplicationTreeitemRenderer(Window win){
		applicationTreeViewWindow=win;
	}
	@Override
	public void render(final Treeitem item, Object data) throws Exception {

		final ProductCatalogueInfo product = (ProductCatalogueInfo) data;
		// 为右键菜单设置父容器
		
		Treecell tc = new Treecell(product.getProductName());
		tc.setId(product.getId());
		tc.addForward("onClick", applicationTreeViewWindow, "onClickTree",product.getId());
		Treerow tr = null;
		if (item.getTreerow() == null) {
			tr = new Treerow();
			tr.setParent(item);
		} else {
			tr = item.getTreerow();
			tr.getChildren().clear();
		}

		switch (product.getProductLevel()) {
		case 1: {
			tr.setDroppable("false");
			tr.setDraggable("false");
		}
			break;
		case 2: {
			tr.setDroppable("true");
			tr.setDraggable("false");
		}
			break;
		case 3: {
			tr.setDroppable("true");
			tr.setDraggable("false");
			tc.setTooltiptext(product.getCertificatesNo());
		}
			break;
		default: {
			tr.setDroppable("false");
			tr.setDraggable("true");
		}

		}
		tc.setParent(tr);

	}

}
