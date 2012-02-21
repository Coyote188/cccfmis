package cccf.ma.web.zk;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Center;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;

import cccf.ma.model.ProductCatalogueInfo;
import cccf.ma.service.ProductCatalogueTreeService;

@SuppressWarnings("serial")
public class EnterpriseProductCatalogueController  extends GenericForwardComposer{

	/**
	 * 
	 */
	private Window enteprodCataWin;
	private Tree procTre;
	private Center procCtr;
	
    HashMap<String, ProductCatalogueInfo> params = new HashMap();
	 
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp); 
		procTre.setModel(new EnterpriseProductCataModel());
		procTre.setTreeitemRenderer(new EnterpriseProductCataTreeitemRenderer());
		
	}
	/**
	 * 内部类的使用,如果放在上面的方法中应放到最后一句前
	 * @author Administrator
	 *
	 */
	
	private class EnterpriseProductCataTreeitemRenderer implements TreeitemRenderer
	{

		@Override
		public void render(final Treeitem item, Object data) throws Exception {
			final ProductCatalogueInfo product = (ProductCatalogueInfo)data;
			final Treecell tc=new Treecell();
			tc.setId(product.getId());
			
			Treerow tr = null;
			if(item.getTreerow()==null){
				tr = new Treerow();
				tr.setParent(item);
			}else{
				tr = item.getTreerow(); 
				tr.getChildren().clear();
			}
			tc.setParent(tr);
			if(!ProductCatalogueTreeService.checkIsLeafForEnterprise(product))
			{
				tc.setLabel(product.getProductName());
				
			}
			else
			{
				tc.setLabel(product.getProductName()+"["+product.getActivateStatusName()+"]");
				tc.addEventListener(Events.ON_CLICK, new EventListener(){

					@Override
					public void onEvent(Event arg0) throws Exception {
						params.clear();
						params.put("productCatalogue",product);
						openFreeWindow(item,params);
					}
					
				});
			}
			item.setOpen(true);
			
		}
		/**
		  * 打开窗口
		  * @param params
		  */
		 @SuppressWarnings("unchecked")
		public void openFreeWindow(Treeitem item,Map params)
		 {
			 final Window objWindow = (Window) Executions.createComponents("/SysForm/productcatainfo.zul",null,params);
	         final Center c=(Center)item.getFellow("procCtr");
	         if(c.getChildren().size()>0){ c.getFirstChild().detach();}
	         objWindow.setWidth("100%");
	     	 objWindow.setHeight("100%");
	         objWindow.setContentStyle("overflow:auto");
	         objWindow.setParent(c);
	         objWindow.doEmbedded();
		 }
		
	}
}
