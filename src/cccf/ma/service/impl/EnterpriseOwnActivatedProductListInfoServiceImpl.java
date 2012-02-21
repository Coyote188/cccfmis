package cccf.ma.service.impl;
import java.math.*;
import openjframework.model.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
import cccf.ma.service.*;
import com.aidi.core.service.*;
public class  EnterpriseOwnActivatedProductListInfoServiceImpl implements  EnterpriseOwnActivatedProductListInfoService
 {
 
   public Serializable create(EnterpriseOwnActivatedProductListInfo bean)
   {
   		return BaseDAOServcieUtil.save(bean);
   	}
   
   public void delete(EnterpriseOwnActivatedProductListInfo bean)
   {
   BaseDAOServcieUtil.remove(bean);}
   
   public void update(EnterpriseOwnActivatedProductListInfo bean)
   {
   BaseDAOServcieUtil.upDate(bean);}
   
   public EnterpriseOwnActivatedProductListInfo getById(String id)
   {
   return (EnterpriseOwnActivatedProductListInfo)BaseDAOServcieUtil.findById(EnterpriseOwnActivatedProductListInfo.class,id);
   }
   
   public EnterpriseOwnActivatedProductListInfo getByPrimaryKey(String key)
   {
   return (EnterpriseOwnActivatedProductListInfo)BaseDAOServcieUtil.findByPrimaryKey(EnterpriseOwnActivatedProductListInfo.class,key);
   }
   
   public List findByQuery(String querystr){
		return BaseDAOServcieUtil.findByQueryString(querystr);
   }
   
   public List getAll(){
		return BaseDAOServcieUtil.findByQueryString("from EnterpriseOwnActivatedProductListInfo");
	}
	
  public void saveOrUpdate(EnterpriseOwnActivatedProductListInfo bean){
		BaseDAOServcieUtil.saveOrUpdata(bean);
	}
 }
   