package cccf.ma.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;

import org.hibernate.Query;

import openjframework.model.UserInfo;
import openjframework.service.UserInfoServiceUtil;
import cccf.ma.model.*;

import com.aidi.core.web.framework.ServiceAdapter;
import com.aidi.core.spring.CustomerContextHolder;
public class ProductCatalogueInfoServiceUtil
{
    public static Serializable  create(ProductCatalogueInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
       ProductCatalogueInfoService service=(ProductCatalogueInfoService)ServiceAdapter.getServiceByName("ProductCatalogueInfoService");
       return service.create(bean);
    }
    public static void delete(ProductCatalogueInfo bean){
    	CustomerContextHolder.setCustomerType("cccf");
        ProductCatalogueInfoService service=(ProductCatalogueInfoService)ServiceAdapter.getServiceByName("ProductCatalogueInfoService");
       service.delete(bean);
       }
    public static void saveOrUpdate(ProductCatalogueInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        ProductCatalogueInfoService service=(ProductCatalogueInfoService)ServiceAdapter.getServiceByName("ProductCatalogueInfoService");
       service.saveOrUpdate(bean);
       }
    public static void update(ProductCatalogueInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        ProductCatalogueInfoService service=(ProductCatalogueInfoService)ServiceAdapter.getServiceByName("ProductCatalogueInfoService");
       service.update(bean);
       }
    public static List getAll(){
       CustomerContextHolder.setCustomerType("cccf");
        ProductCatalogueInfoService service=(ProductCatalogueInfoService)ServiceAdapter.getServiceByName("ProductCatalogueInfoService");
       return service.getAll();
       }
    public static ProductCatalogueInfo getById(String id){
    	CustomerContextHolder.setCustomerType("cccf");
        ProductCatalogueInfoService service=(ProductCatalogueInfoService)ServiceAdapter.getServiceByName("ProductCatalogueInfoService");
       return service.getById(id);
       }
     public static ProductCatalogueInfo getByPrimaryKey(String key){
    	CustomerContextHolder.setCustomerType("cccf");
        ProductCatalogueInfoService service=(ProductCatalogueInfoService)ServiceAdapter.getServiceByName("ProductCatalogueInfoService");
       return service.getByPrimaryKey(key);
       }
    public static List findByQuery(String querystr){
        CustomerContextHolder.setCustomerType("cccf");
        ProductCatalogueInfoService service=(ProductCatalogueInfoService)ServiceAdapter.getServiceByName("ProductCatalogueInfoService");
       return service.findByQuery(querystr);
       }
    /**
	 * 根据产品级别查询
	 * @return
	 */
    public static List<ProductCatalogueInfo> getDataListByLevel(int level)
    {
    	String qstr="from ProductCatalogueInfo where productLevel="+level+" and activateStatus=1 order by productNo asc";
    	List<ProductCatalogueInfo> flpc=ProductCatalogueInfoServiceUtil.findByQuery(qstr);
    	return flpc;
    }
    /**
     * 检索叶子产品
     * @return
     */
    public static List<ProductCatalogueInfo> getLeafData()
    {
    	List<ProductCatalogueInfo> list4 =getDataListByLevel(4);
    	List<ProductCatalogueInfo> list3=getDataListByLevel(3);
    	if(list3!=null&&list3.size()>0)
    	for(ProductCatalogueInfo product:list3)
    	{
    		if(product.getProductChildren().size()==0)
    			list4.add(product);
    	}
    	return list4;


    }
    
    /**
     * 检索叶子产品
     * @return
     */
    public static List<ProductCatalogueInfo> getLeafByPid(String pid,List<ProductCatalogueInfo> plist)
    {
    	 
    	ProductCatalogueInfo production=getById(pid);
    	Set<ProductCatalogueInfo> children=production.getProductChildren();
    	if(children==null||children.size()==0){
    		plist.add(production);
    	}else{
    	for(ProductCatalogueInfo p:children){
    		getLeafByPid(p.getId(),plist);
    	}
    	}
//    	String qstr="from ProductCatalogueInfo where (productLevel=4 or  productLevel=3) and (pid='"+pid+"' " +
//    			"or pid.";
//    	List<ProductCatalogueInfo> listleaf=ProductCatalogueInfoServiceUtil.findByQuery(qstr);
    	
    	return plist;

    }
    /**
     * 检索当前企业已激活的产品
     * @return
     */
    public static List<ProductCatalogueInfo> getCurrentEnterpriseActivatedProduct()
    {
    	List<ProductCatalogueInfo> result=new ArrayList();
    	List<EnterpriseOwnActivatedProductListInfo> list=EnterpriseOwnActivatedProductListInfoServiceUtil.findCurrentEnterprise();
    	if(list!=null)
    	for(EnterpriseOwnActivatedProductListInfo eoap:list)
    	{
    		result.add(eoap.getProduct());
    	}
    	return result;
    }
    /**
     * 根据产品Id和审查人员类别查询
     * 
     * @param proId
     * @param rulType  0-符合性审查1-工厂检查2-评定
     * @return
     */
    public static List<UserInfo> getUserArrayByIdAndType(String proId,int rulType)
    {
    	String qstr="select distinct  pul_.user from ProductCatalogueInfo pro_,Product_User_ListInfo pul_ where pro_.id='"+proId+"' and pul_.type="+rulType;
    	List<UserInfo> userArray=ProductCatalogueInfoServiceUtil.findByQuery(qstr);
    	return userArray;
    }
    
    /**
     * 判断产品product是否在当前企业激活的产品列表中
     * @param product
     * @return
     */
     public static boolean inCurrentEnterpriseOfActivateProductList(ProductCatalogueInfo product)
     {
    	 
    	 List<EnterpriseOwnActivatedProductListInfo> list=EnterpriseOwnActivatedProductListInfoServiceUtil.findCurrentEnterpriseProduct();
    	 if(list!=null&&list.size()>0)
    	 for(EnterpriseOwnActivatedProductListInfo eoap:list)
    	 {
    		 if(eoap.getProduct().getId().equals(product.getId()))
    			 return true;
    	 }
    	 return false;
     }
    
   
}
   