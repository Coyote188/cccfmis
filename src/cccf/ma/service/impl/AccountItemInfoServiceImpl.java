package cccf.ma.service.impl;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
import cccf.ma.service.*;
import com.aidi.core.service.*;
public class  AccountItemInfoServiceImpl implements  AccountItemInfoService
 {
 
   public Serializable create(AccountItemInfo bean)
   {
   		return BaseDAOServcieUtil.save(bean);
   	}
   
   public void delete(AccountItemInfo bean)
   {
   BaseDAOServcieUtil.remove(bean);}
   
   public void update(AccountItemInfo bean)
   {
   BaseDAOServcieUtil.upDate(bean);}
   
   public AccountItemInfo getById(String id)
   {
   return (AccountItemInfo)BaseDAOServcieUtil.findById(AccountItemInfo.class,id);
   }
   
   public AccountItemInfo getByPrimaryKey(String key)
   {
   return (AccountItemInfo)BaseDAOServcieUtil.findByPrimaryKey(AccountItemInfo.class,key);
   }
   
   public List findByQuery(String querystr){
		return BaseDAOServcieUtil.findByQueryString(querystr);
   }
   
   public List getAll(){
		return BaseDAOServcieUtil.findByQueryString("from AccountItemInfo");
	}
	
  public void saveOrUpdate(AccountItemInfo bean){
		BaseDAOServcieUtil.saveOrUpdata(bean);
	}
 }
   