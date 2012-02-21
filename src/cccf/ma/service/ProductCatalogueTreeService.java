package cccf.ma.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import openjframework.service.UserInfoServiceUtil;
import cccf.ma.model.EnterpriseInfo;
import cccf.ma.model.EnterpriseOwnActivatedProductListInfo;
import cccf.ma.model.ProductCatalogueInfo;

public class ProductCatalogueTreeService {
	/**
	 * 根据产品级别查询评定中心已启用的产品
	 * @return
	 */
    public static List<ProductCatalogueInfo> getDataListByLevelYes(int level)
    {
    	String qstr="from ProductCatalogueInfo where productLevel="+level+"and activateStatus=1 order by productNo asc";
    	List<ProductCatalogueInfo> flpc=ProductCatalogueInfoServiceUtil.findByQuery(qstr);
    	return flpc;
    }
    /**
	 * 根据产品级别查询产品
	 * @return
	 */
    public static List<ProductCatalogueInfo> getDataListByLevelAll(int level)
    {
    	String qstr="from ProductCatalogueInfo where productLevel="+level+"order by productNo asc";
    	List<ProductCatalogueInfo> flpc=ProductCatalogueInfoServiceUtil.findByQuery(qstr);
    	return flpc;
    }
    /**
     * 取得所有产品项
     * @return
     */
    public static List<ProductCatalogueInfo> getAllProductDataList()
    {
    	List<ProductCatalogueInfo> flpc=ProductCatalogueInfoServiceUtil.getAll();
    	return flpc;
    }
    /**
     * 取得当前产品的子产品
     * @param parent
     * @return
     */
   
    public static List<ProductCatalogueInfo> getProductChildren(ProductCatalogueInfo parent) {
		
        Set pcset=parent.getProductChildren();
        List<ProductCatalogueInfo> searchResult=new ArrayList(pcset);
        return searchResult;   

		
	}
    /**
     * 取得孩子总数
     * @param parent
     * @return
     */
    public static int getProductChildrenCount(ProductCatalogueInfo parent)
    {
    	return getProductChildren(parent).size();
    }
    /**
     * 判断节点是否为叶子节点
     * @param node
     * @return
     */
    public static boolean checkIsLeaf(ProductCatalogueInfo node) {
		
		return getProductChildrenCount(node)==0?true:false;
		
	}
    /*
     * 过滤的产品树
     */
    /**
     * 取得当前产品的子产品(过滤的)
     * @param parent
     * @return
     */
    public static List<ProductCatalogueInfo> getProductChildrenFilterable(ProductCatalogueInfo parent) {
		
        Set pcset=parent.getProductChildren();
        List<ProductCatalogueInfo> searchResult=new ArrayList(pcset);
        for(Iterator it=searchResult.iterator();it.hasNext();)
		{
        	ProductCatalogueInfo _prod=(ProductCatalogueInfo)it.next();
        	if(ProductCatalogueInfoServiceUtil.inCurrentEnterpriseOfActivateProductList(_prod)||_prod.getActivateStatus()==0)
    		{
    			it.remove();
    		}
		}
        /*这里是第二次遇到list.remove(对象);若使用下面的这段代码移除对象,就报错.
        for(ProductCatalogueInfo prod:searchResult)
    	{
    		if(ProductCatalogueInfoServiceUtil.inCurrentEnterpriseOfActivateProductList(prod))
    		{
    			searchResult.remove(prod);
    		}
    	}*/
        return searchResult;   

		
	}
    /**
     * 取得孩子总数(过滤的)
     * @param parent
     * @return
     */
    public static int getProductChildrenCountFilterable(ProductCatalogueInfo parent)
    {
    	return getProductChildrenFilterable(parent).size();
    }
    /**
     * 判断节点是否为叶子节点(过滤的)
     * @param node
     * @return
     */
    public static boolean checkIsLeafFilterable(ProductCatalogueInfo node) {
		
		return getProductChildrenCountFilterable(node)==0?true:false;
		
	}
    
    /*
     * 企业查看
     */
    
    /**
     * 查看该企业已激活/待激活的产品(包括根目录)
     */
    private static List<ProductCatalogueInfo> lists=new ArrayList();
    public static List<ProductCatalogueInfo> getEnterpriseProductCataData()
    {
    	lists.clear();
    	List<EnterpriseOwnActivatedProductListInfo> list=EnterpriseOwnActivatedProductListInfoServiceUtil.findCurrentEnterpriseProduct();
    	List<ProductCatalogueInfo> productCata = new ArrayList();
    	for(EnterpriseOwnActivatedProductListInfo eoap:list)
    	{
    		ProductCatalogueInfo product=eoap.getProduct();
    		product.setActivateStatus(eoap.getActivateStatus());
    		boolean isExists=false;
    		while((product!=null)&&(isExists==false))
    		{
    			if(productCata.size()>0)
	    		{
    				for(ProductCatalogueInfo _product:productCata)
	    			{
	    				
	    					if(_product.getId().equals(product.getId()))
		    				{
		    					isExists=true;
		    					break;
		    				}
	    				
		    			}
    				if(isExists==false)
    				{
    					productCata.add(product);
    					product=product.getProductParent();
    					isExists=false;
    				}
    					
    				}
	    			else
	    			{
	    				productCata.add(product);
	    				product=product.getProductParent();
	    				}
    			
    			
    		  }
    		
    	 }
    	 lists.addAll(productCata);
    	 return productCata;
    }
    /**
     * 取得激活子产品
     * @param parent
     * @return
     */
    public static  List<ProductCatalogueInfo> getChildrenForEnterprise(ProductCatalogueInfo parent)
    {
    	List<ProductCatalogueInfo> childrenList=new ArrayList();
    	for(ProductCatalogueInfo product:lists)
    	{
    		if(product.getProductParent()!=null)
    		if(product.getProductParent().getId().equals(parent.getId()))
    			childrenList.add(product);
    		
    		
    	}
    	return childrenList;
    }
    /**
     * 孩子数
     * @param parent
     * @return
     */
    public static int getChildrenCountForEnterprise(ProductCatalogueInfo parent)
    {
    	return getChildrenForEnterprise(parent).size();
    }
    /**
     * 判断是否为叶子节点
     * @param node
     * @return
     */
    public static boolean checkIsLeafForEnterprise(ProductCatalogueInfo node) {
		
		return getChildrenCountForEnterprise(node)==0?true:false;
		
	}
}
