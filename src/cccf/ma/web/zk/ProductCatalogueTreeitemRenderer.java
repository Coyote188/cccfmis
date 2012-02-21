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

public class ProductCatalogueTreeitemRenderer implements TreeitemRenderer {

	private boolean addDoubleClick = false;
	HashMap params = new HashMap();
	ProductCatalogueInfo _product;

	Menupopup pupop1 = new Menupopup();// 定义一个右键菜单
	Menupopup pupop2 = new Menupopup();
	Menuitem menu, menu_;
	Menuitem menu1;
	Menuitem menu2, menu2_;
	Menuitem menu3, menu3_;
	Menuitem menu4,menu4_;
	
	Menuseparator ms = new Menuseparator();

	Div treeDiv;

	public ProductCatalogueTreeitemRenderer() {
		this(true);
		// 设置增加产品菜单项
		menu = new Menuitem("增加同级产品");
		menu.setImage("/image/common/sharing.png");
		menu.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				params.clear();
				params.put("product_parent", _product.getProductParent());
				params.put("cmd", "tree_add");
				String nextCode = "";//NextCodeCalculate.nextCode(_product);
				params.put("productNo", nextCode);
				openFreeWindow("/SysForm/productCatalogue_Add.zul",params);

			}

		});
		menu_ = (Menuitem) menu.clone();

		menu1 = new Menuitem("增加子级产品");
		menu1.setImage("/image/common/signal_instance.png");
		menu1.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				params.clear();
				params.put("product_parent", _product);
				params.put("cmd", "tree_add");
				String nextCode = NextCodeCalculate.nextCodeForChild(_product);
				params.put("productNo", nextCode);
				openFreeWindow("/SysForm/productCatalogue_Add.zul",params);

			}

		});

		menu2 = new Menuitem("查看/修改本级产品");
		menu2.setImage("/image/common/edit.png");
		menu2.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				params.clear();
				params.put("productCatalogue", _product);
				params.put("cmd", "tree_edit");
				openFreeWindow("/SysForm/productCatalogue_Add.zul",params);

			}

		});
		menu2_ = (Menuitem) menu2.clone();

		menu3 = new Menuitem("删除本级产品");
		menu3.setImage("/image/common/close.png");
		menu3.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				if (Messagebox.show("是否要删除本级?删除本级子级也将全部删除", "删除?",
						Messagebox.OK | Messagebox.NO, Messagebox.QUESTION) == Messagebox.OK) {
					ProductCatalogueInfoServiceUtil.delete(_product);
					// 刷新树

					Tree procTre = (Tree) pupop1.getFellow("procTre");
					procTre.setModel(null);
					procTre.setTreeitemRenderer(null);
					procTre.setModel(new ProductCatalogueTree());
					procTre
							.setTreeitemRenderer(new ProductCatalogueTreeitemRenderer());

				}
			}

		});
		menu3_ = (Menuitem) menu3.clone();

		menu4=new Menuitem("分配管辖人员");
		menu4.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				
				params.clear();
				params.put("productCatalogue", _product);
				openFreeWindow("/SysForm/SystemAdministrator/productuserslist.zul",params);
			}
		});
		menu4_=(Menuitem)menu4.clone();
		
		pupop1.appendChild(menu);// 设置右键菜单
		pupop1.appendChild(menu1);
		pupop1.appendChild(ms);
		pupop1.appendChild(menu2);
		pupop1.appendChild(ms);
		pupop1.appendChild(menu3);
		pupop1.appendChild(menu4);
		
		pupop2.appendChild(menu_);// 设置右键菜单
		pupop2.appendChild(menu2_);
		pupop2.appendChild(menu3_);
		pupop2.appendChild(menu4_);

	}

	/**
	 * 打开窗口
	 * 
	 * @param params
	 */
	public void openFreeWindow(String url,Map params) {
		Window objWindow = (Window) Executions.createComponents(
				url, null, params);
		Center c = (Center) menu1.getFellow("procCtr");
		if (c.getChildren().size() > 0) {
			c.getFirstChild().detach();
		}
		objWindow.setWidth("100%");
		objWindow.setHeight("100%");
		objWindow.setContentStyle("overflow:auto");
		objWindow.setSizable(false);
		objWindow.setParent(c);
		objWindow.doEmbedded();
	}

	/**
	 * 
	 * @param addDoubleClick
	 * @param params
	 * @param pupop1
	 * @param menu1
	 */
	public ProductCatalogueTreeitemRenderer(boolean addDoubleClick,
			HashMap params, Menupopup pupop1, Menuitem menu1) {
		super();
		this.addDoubleClick = addDoubleClick;
		this.params = params;
		this.pupop1 = pupop1;
		this.menu1 = menu1;
	}

	public ProductCatalogueTreeitemRenderer(boolean addDoubleClick) {
		this.addDoubleClick = addDoubleClick;
	}

	@Override
	public void render(final Treeitem item, Object data) throws Exception {

		final ProductCatalogueInfo product = (ProductCatalogueInfo) data;
		// 为右键菜单设置父容器
		treeDiv = (Div) item.getFellow("treeDiv");
		pupop1.setParent(treeDiv);
		item.setContext(pupop1);
		item.setId(product.getId());
		
		Treecell tc = new Treecell(product.getProductName());
		tc.setTooltiptext(product.getProductName());
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
			}break;
			case 2: {
				tr.setDroppable("true");
				tr.setDraggable("false");
			}break;
			case 3: {
				tr.setDroppable("true");
				tr.setDraggable("false");
				tc.setTooltiptext("证书编码:"+product.getCertificatesNo());
			}break;
			default: {
				tr.setDroppable("false");
				tr.setDraggable("true");
				pupop2.setParent(treeDiv);
				item.setContext(pupop2);
			}

		}

		tr.addEventListener(Events.ON_DROP,new EventListener(){

			@Override
			public void onEvent(Event event) throws Exception {
				DropEvent dorpev=(DropEvent)event;
				Component dragged=dorpev.getDragged();
				Treerow target=(Treerow)dorpev.getTarget();
				if(dragged instanceof Treerow)
				{
					Treeitem ti = new Treeitem();
					ti.setId(((Treerow)dragged).getParent().getId());
					((Treerow)dragged).getParent().setId("");
					dragged.setParent(ti);
					if(target.getLinkedTreechildren()==null)
					{
						Treechildren tcr=new Treechildren();
						tcr.appendChild(ti);
						tcr.setParent(target.getParent());
					}
					else
					{
						target.getLinkedTreechildren().appendChild(ti);
					}
					ProductCatalogueInfo target_pro=ProductCatalogueInfoServiceUtil
								.getById(target.getParent().getId());
					ProductCatalogueInfo dragged_pro=ProductCatalogueInfoServiceUtil
										.getById(dragged.getParent().getId());
					dragged_pro.setProductParent(target_pro);
					ProductCatalogueInfoServiceUtil.update(dragged_pro);
					Tree procTre=target.getTree();
					procTre.setModel(null);
					procTre.setTreeitemRenderer(null);
					procTre.setModel(new ProductCatalogueTree());   
					procTre.setTreeitemRenderer(new ProductCatalogueTreeitemRenderer());
				}
				
			}
			
		});
		tc.setParent(tr);

		item.addEventListener(Events.ON_RIGHT_CLICK, new EventListener() {

			@Override
			public void onEvent(Event arg0) throws Exception {

				_product = product;
			}

		});
		if (!ProductCatalogueTreeService.checkIsLeaf(product)) {
			
		} else {

		}

		 item.setOpen(true);

	}

}
