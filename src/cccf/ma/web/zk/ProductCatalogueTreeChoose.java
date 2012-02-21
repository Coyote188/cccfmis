package cccf.ma.web.zk;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.TreeModel;
import org.zkoss.zul.event.TreeDataListener;

import cccf.ma.model.ProductCatalogueInfo;
import cccf.ma.service.ProductCatalogueTreeService;

public class ProductCatalogueTreeChoose implements TreeModel{
	
	private List<ProductCatalogueInfo> root;
	/**
	 * 初始化根目录
	 */
	public ProductCatalogueTreeChoose()
	{
		root=ProductCatalogueTreeService.getDataListByLevelYes(1);
		
	}
	@Override
	public void addTreeDataListener(final TreeDataListener treeData) {
	
	}

	@Override
	public Object getChild(Object parent, int index) {
		if(parent instanceof List)
		{
            return ((List)parent).get(index); 
		}
        else if(parent instanceof ProductCatalogueInfo){   
                List<ProductCatalogueInfo> productList = 
                ProductCatalogueTreeService.getProductChildrenFilterable((ProductCatalogueInfo)parent); 
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
        
        	 return (int)ProductCatalogueTreeService.getProductChildrenCountFilterable((ProductCatalogueInfo)parent);  
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
		
         return this.root; 
	}

	@Override
	public boolean isLeaf(Object node) {
		return ProductCatalogueTreeService.checkIsLeafFilterable((ProductCatalogueInfo)node); 
	}

	@Override
	public void removeTreeDataListener(TreeDataListener arg0) {
		// TODO Auto-generated method stub
		
	}

}
