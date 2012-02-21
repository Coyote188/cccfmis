package cccf.ma.web.zk;
import java.util.*;

import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.*;

import cccf.ma.service.*;
import cccf.ma.model.*;
import cccf.myenum.ProductEnabledStatus;


public class ProductcatalogueeditWindow extends Window{

     public ProductCatalogueInfo productCatalogue; 
     Map params = Executions.getCurrent().getArg();
     String userId;
	 String processId = "0";
	 String rowId;
	 private int id;//记录修改状态
	 Checkbox modelCbx;
	 Checkbox fireCbx; 
	 
	 public void onSelectBusinessType(ForwardEvent event){
		 Combobox businessType = (Combobox) event.getOrigin().getTarget();
		 if(businessType.getSelectedItem()==null)return;
		 productCatalogue.setBusinessType((String) businessType.getSelectedItem().getValue()) ; 
	 }
	 
     public void onCreate()
     {
    	 productCatalogue=(ProductCatalogueInfo)this.getPage().getVariable("productCatalogue");
    	 if(params.get("cmd")!=null)
    	 {
    		 modelCbx=(Checkbox) getFellow("modelCbx");
        	 fireCbx=(Checkbox) getFellow("fireCbx");
        	 //初始化是否消防车和是否允许分型号
        	 modelCbx.setChecked(productCatalogue.isModelStatus());
        	 fireCbx.setChecked(productCatalogue.isFireEngineStatus());
    	 } 
     }
     
     public void onSave(){	
        validateData();	
		String cmd="add";
		String pcmd=(String) params.get("cmd");
		if(pcmd!=null) cmd=pcmd;
		productCatalogue.setFireEngineStatus(fireCbx.isChecked()?true:false);
		productCatalogue.setModelStatus(modelCbx.isChecked()?true:false);
		//如果是右键菜单打开的编辑页面
		if(cmd.equals("tree_add"))
		{
			
			ProductCatalogueInfo _parent=(ProductCatalogueInfo)params.get("product_parent");
			if(_parent==null)
			{
				productCatalogue.setProductParent(null);
				productCatalogue.setProductLevel(1);
			}
			else{
				_parent.addChildProduct(productCatalogue);
				//四级产品目录已过滤增加菜单
				productCatalogue.setProductLevel(_parent.getProductLevel()+1);
			}
			
			
			rowId =ProductCatalogueInfoServiceUtil.create(productCatalogue).toString();
			
		}	
		else
		{	
			ProductCatalogueInfoServiceUtil.update(productCatalogue);
			//递归产品子产品修改子产品的启用状态
			traversal(productCatalogue);
			rowId = productCatalogue.getId();
		}
		
		//刷新树
		Center c=(Center)this.getParent();
		Tree procTre=(Tree)c.getFellow("procTre");
		procTre.setModel(null);
		procTre.setTreeitemRenderer(null);
		procTre.setModel(new ProductCatalogueTree());   
		procTre.setTreeitemRenderer(new ProductCatalogueTreeitemRenderer());
		this.detach();
	}			


	/**
	 * Cancel this screen
	 * 
	 */
	public void onCancel() {
		this.detach();
	}
	/**
	 * 该方法在企业查看已激活产品窗口中调用
	 */
	public void onEnterCancel()
	{
		Center c=(Center)this.getParent();
		((Window)c.getFellow("enteprodCataWin")).detach();
	}


	public void onproductParentSelect(org.zkoss.zk.ui.event.SelectEvent evt) {
		Iterator items = evt.getSelectedItems().iterator();
		while (items.hasNext()) {
			Comboitem item = (Comboitem) items.next();
			String id = item.getValue().toString();
			String qstr="from ProductCatalogueInfo where id='"+id+"'";
			List list=ProductCatalogueInfoServiceUtil.findByQuery(qstr);			
			ProductCatalogueInfo productParent = new ProductCatalogueInfo();
			if(list.size()>0)
			    productParent=(ProductCatalogueInfo)list.get(0);
			productCatalogue.setProductParent(productParent);
		}
	}
	//设置是否启用产品	
	
	public void onproductActivateSelect(org.zkoss.zk.ui.event.SelectEvent evt) {	
		Iterator items = evt.getSelectedItems().iterator();
		while (items.hasNext()) {
			Comboitem item = (Comboitem) items.next();
			id = Integer.parseInt(String.valueOf(item.getValue()));
			productCatalogue.setActivateStatus(id);
			Label uas=(Label)this.getFellow("uas");
			uas.setValue("修改状态:"+ProductEnabledStatus.values()[id].toString());
		}
		
	}
	public void traversal(ProductCatalogueInfo product)
	{
		List<ProductCatalogueInfo> chlist=new ArrayList(product.getProductChildren());
		if(chlist.size()>0)
		{
			
			for(ProductCatalogueInfo _product:chlist)
			{
				_product.setActivateStatus(id);
				ProductCatalogueInfoServiceUtil.update(_product);
				traversal(_product);
			}
			
		}
		return;
	}
    
		
	
	void validateData()
	{
	  	Textbox productNo=(Textbox)getFellow("productNo");
	  	if(productNo.getValue().length()==0)
	  		throw new WrongValueException("严重:产品编号发生错误!");
	  	Textbox productName=(Textbox)getFellow("productName");
	  	productName.getValue();
	  	Textbox observedStandard=(Textbox)getFellow("observedStandard");
	  	observedStandard.getValue();
	  	Textbox technicalRequirement=(Textbox)getFellow("technicalRequirement");
	  	technicalRequirement.getValue();
	  	Textbox productDescribe=(Textbox)getFellow("productDescribe");
	  	productDescribe.getValue();
	  	Combobox productActivate=(Combobox)getFellow("productActivate");
	  	if(productActivate.getValue().contains("—"))
	  		throw new WrongValueException(productActivate,"请选择是否启用该产品");
	  	
	}
		

}
		
		