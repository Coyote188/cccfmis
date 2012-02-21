package cccf.ma.service.impl;
import java.math.*;
import openjframework.model.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
import cccf.ma.service.*;
import com.aidi.core.service.*;
public class  Product_User_ListInfoServiceImpl implements  Product_User_ListInfoService
 {
 
   public Serializable create(Product_User_ListInfo bean)
   {
   		return BaseDAOServcieUtil.save(bean);
   	}
   
   public void delete(Product_User_ListInfo bean)
   {
   BaseDAOServcieUtil.remove(bean);}
   
   public void update(Product_User_ListInfo bean)
   {
   BaseDAOServcieUtil.upDate(bean);}
   
   public Product_User_ListInfo getById(String id)
   {
   return (Product_User_ListInfo)BaseDAOServcieUtil.findById(Product_User_ListInfo.class,id);
   }
   
   public Product_User_ListInfo getByPrimaryKey(String key)
   {
   return (Product_User_ListInfo)BaseDAOServcieUtil.findByPrimaryKey(Product_User_ListInfo.class,key);
   }
   
   public List findByQuery(String querystr){
		return BaseDAOServcieUtil.findByQueryString(querystr);
   }
   
   public List getAll(){
		return BaseDAOServcieUtil.findByQueryString("from Product_User_ListInfo");
	}
	
  public void saveOrUpdate(Product_User_ListInfo bean){
		BaseDAOServcieUtil.saveOrUpdata(bean);
	}
 }
   