package cccf.ma.service.impl;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
import cccf.ma.service.*;
import com.aidi.core.service.*;
public class  ProductCatalogueInfoServiceImpl implements  ProductCatalogueInfoService
 {
 
   public Serializable create(ProductCatalogueInfo bean)
   {
   		return BaseDAOServcieUtil.save(bean);
   	}
   
   public void delete(ProductCatalogueInfo bean)
   {
   BaseDAOServcieUtil.remove(bean);}
   
   public void update(ProductCatalogueInfo bean)
   {
   BaseDAOServcieUtil.upDate(bean);}
   
   public ProductCatalogueInfo getById(String id)
   {
   return (ProductCatalogueInfo)BaseDAOServcieUtil.findById(ProductCatalogueInfo.class,id);
   }
   
   public ProductCatalogueInfo getByPrimaryKey(String key)
   {
   return (ProductCatalogueInfo)BaseDAOServcieUtil.findByPrimaryKey(ProductCatalogueInfo.class,key);
   }
   
   public List findByQuery(String querystr){
		return BaseDAOServcieUtil.findByQueryString(querystr);
   }
   
   public List getAll(){
		return BaseDAOServcieUtil.findByQueryString("from ProductCatalogueInfo");
	}
	
  public void saveOrUpdate(ProductCatalogueInfo bean){
		BaseDAOServcieUtil.saveOrUpdata(bean);
	}
 }
   