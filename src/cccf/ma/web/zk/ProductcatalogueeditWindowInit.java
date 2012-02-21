package cccf.ma.web.zk;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import cccf.ma.service.*;
import cccf.ma.model.*;
import cccf.myenum.ProductEnabledStatus;
import cccf.myenum.ProductType;

public class ProductcatalogueeditWindowInit extends org.zkoss.zkplus.databind.AnnotateDataBinderInit{
 
	 Map params = Executions.getCurrent().getArg();
	 ProductCatalogueInfo productCatalogue;
	 
	  public void doAfterCompose(Page page,org.zkoss.zk.ui.Component[] comps)throws Exception {
		 productCatalogue=(ProductCatalogueInfo)params.get("productCatalogue");		 
	     String nextCode=(String)params.get("productNo");
		
		 if(productCatalogue==null){
		 	productCatalogue=new ProductCatalogueInfo();
		 	productCatalogue.setProductNo(nextCode); 
		 	page.setVariable("Current_EnabledStatus","");
		 }
		 else
		 {
			 page.setVariable("Current_EnabledStatus", "目前状态:"+productCatalogue.getEnabledStatus());
		 }
	  
		 /*目前用不到
		 /**
	    * 初始化产品类别
	    
		 
	   List<CommonListModel> productLevelList=new ArrayList();
	   CommonListModel model;
	   for(int i=0;i<ProductType.values().length;i++){
		   model=new CommonListModel(i,ProductType.values()[i].toString());
		   productLevelList.add(model);
	   }
	   */
	   /**
	    * 初始化产品启用状态
	    */
	   List<CommonListModel> productEnabledStatusList=new ArrayList();
	   CommonListModel model_ac;
	   for(int i=0;i<ProductEnabledStatus.values().length;i++){
		   model_ac=new CommonListModel(i,ProductEnabledStatus.values()[i].toString());
		   productEnabledStatusList.add(model_ac);
	   }
	   
	   page.setVariable("productEnabledStatusList", productEnabledStatusList);
	  // page.setVariable("productLevelList", productLevelList);
	   page.setVariable("productCatalogue", productCatalogue);
	   super.doAfterCompose(page,(org.zkoss.zk.ui.Component[]) comps); 
	     
	  }
 }
    