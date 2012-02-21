package cccf.ma.service.impl;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
import cccf.ma.service.*;
import com.aidi.core.service.*;
public class  AccountTypeInfoServiceImpl implements  AccountTypeInfoService
 {
 
   public Serializable create(AccountTypeInfo bean)
   {
   		return BaseDAOServcieUtil.save(bean);
   	}
   
   public void delete(AccountTypeInfo bean)
   {
   BaseDAOServcieUtil.remove(bean);}
   
   public void update(AccountTypeInfo bean)
   {
   BaseDAOServcieUtil.upDate(bean);}
   
   public AccountTypeInfo getById(String id)
   {
   return (AccountTypeInfo)BaseDAOServcieUtil.findById(AccountTypeInfo.class,id);
   }
   
   public AccountTypeInfo getByPrimaryKey(String key)
   {
   return (AccountTypeInfo)BaseDAOServcieUtil.findByPrimaryKey(AccountTypeInfo.class,key);
   }
   
   public List findByQuery(String querystr){
		return BaseDAOServcieUtil.findByQueryString(querystr);
   }
   
   public List getAll(){
		return BaseDAOServcieUtil.findByQueryString("from AccountTypeInfo");
	}
	
  public void saveOrUpdate(AccountTypeInfo bean){
		BaseDAOServcieUtil.saveOrUpdata(bean);
	}
 }
   