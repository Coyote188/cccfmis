package cccf.ma.web.zk;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.TreeModel;
import org.zkoss.zul.event.TreeDataListener;

import cccf.ma.model.ProductCatalogueInfo;
import cccf.ma.service.ProductCatalogueTreeService;

public class EnterpriseProductCataModel implements TreeModel{

	private List<ProductCatalogueInfo> model;
	
	public EnterpriseProductCataModel()
	{
		model=ProductCatalogueTreeService.getEnterpriseProductCataData();
	}
	@Override
	public void addTreeDataListener(TreeDataListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getChild(Object parent, int index) {
		if(parent instanceof List)
		{
            return ((List)parent).get(index); 
		}
        else if(parent instanceof ProductCatalogueInfo){   
                List<ProductCatalogueInfo> productList = 
                ProductCatalogueTreeService.getChildrenForEnterprise((ProductCatalogueInfo)parent); 
                return productList.get(index);  
        }   
        else  
            return null;  
	}

	@Override
	public int getChildCount(Object parent) {
		 if(parent instanceof List){
				
			 return ((List)parent).size();   
		 }
            
         else if(parent instanceof ProductCatalogueInfo)
         {
        
        	 return (int)ProductCatalogueTreeService.getChildrenCountForEnterprise((ProductCatalogueInfo)parent);  
         }
         else  
             return 0;  
	}

	@Override
	public int[] getPath(Object arg0, Object arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getRoot() {
		List<ProductCatalogueInfo> root=new ArrayList();
		for(ProductCatalogueInfo product:model)
		{
			if(product.getProductLevel()==1)
				root.add(product);
		}
		return root;
	}

	@Override
	public boolean isLeaf(Object node) {
		return ProductCatalogueTreeService.checkIsLeafForEnterprise((ProductCatalogueInfo)node);
	}

	@Override
	public void removeTreeDataListener(TreeDataListener arg0) {
		// TODO Auto-generated method stub
		
	}

}
