package cccf.ma.web.zk;

import java.util.HashMap;
import java.util.Map;

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
import org.zkoss.zul.Window;

public class ProductCatalogueTreeView  extends GenericForwardComposer{
	private Tree procTre;
	Menupopup pupop;
	Menuitem menu;
	
	HashMap params = new HashMap();
	public ProductCatalogueTreeView()
	{
		pupop=new Menupopup();
		menu=new Menuitem("增加产品");
		menu.addEventListener(Events.ON_CLICK,new EventListener(){   
	            @Override  
	            public void onEvent(Event event) throws Exception {   
	            	 params.put("cmd", "tree_add");
	            	 String nextCode=NextCodeCalculate.nextCode();
	        		 params.put("productNo",nextCode);
	        		 openFreeWindow(params);
	            }   
	               
	        }); 
		pupop.appendChild(menu);
	}
	/**
	  * 打开窗口
	  * @param params
	  */
	 public void openFreeWindow(Map params)
	 {
		 Window objWindow = (Window) Executions.createComponents("/SysForm/productCatalogue_Add.zul",null,params);
        Center c=(Center)menu.getFellow("procCtr");
        if(c.getChildren().size()>0){ c.getFirstChild().detach();}
        objWindow.setWidth("100%");
    	 objWindow.setHeight("100%");
        objWindow.setContentStyle("overflow:auto");
        objWindow.setSizable(false);
        objWindow.setParent(c);
        objWindow.doEmbedded();
	 }
	 
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp); 
		procTre.setModel(new ProductCatalogueTree());   
		procTre.setTreeitemRenderer(new ProductCatalogueTreeitemRenderer());
		pupop.setParent((Div)procTre.getFellow("treeDiv"));
		procTre.setContext(pupop);
	}

}
