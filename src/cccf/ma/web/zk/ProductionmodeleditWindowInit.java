package cccf.ma.web.zk;
import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import cccf.ma.service.*;
import cccf.ma.model.*;

public class ProductionmodeleditWindowInit extends org.zkoss.zkplus.databind.AnnotateDataBinderInit{
 
	 Map params = Executions.getCurrent().getArg();
	 ProductionModelInfo productionModel;
	 EnterpriseProductModel productModel;
	 
	  public void doAfterCompose(Page page,org.zkoss.zk.ui.Component[] comps)throws Exception {
		 productionModel=(ProductionModelInfo)params.get("productionModel");		 
		 productModel = (EnterpriseProductModel) params.get("productModel2");
		 if(null == productModel){
			 productModel = new EnterpriseProductModel();
		 }
		 if(productionModel==null){
		 	productionModel=new ProductionModelInfo();
		 }
		 
		 page.setVariable("productModel", productModel);
	   page.setVariable("productionModel", productionModel);
	   super.doAfterCompose(page,(org.zkoss.zk.ui.Component[]) comps); 
	     
	  }
 }
    