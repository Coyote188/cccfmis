package openjframework.service.impl;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import openjframework.model.*;
import openjframework.service.*;
import com.aidi.core.service.*;
public class  UserLogInfoServiceImpl implements  UserLogInfoService
 {
 
   public Serializable create(UserLogInfo bean)
   {
   		return BaseDAOServcieUtil.save(bean);
   	}
   
   public void delete(UserLogInfo bean)
   {
   BaseDAOServcieUtil.remove(bean);}
   
   public void update(UserLogInfo bean)
   {
   BaseDAOServcieUtil.upDate(bean);}
   
   public UserLogInfo getById(String id)
   {
   return (UserLogInfo)BaseDAOServcieUtil.findById(UserLogInfo.class,id);
   }
   
   public UserLogInfo getByPrimaryKey(String key)
   {
   return (UserLogInfo)BaseDAOServcieUtil.findByPrimaryKey(UserLogInfo.class,key);
   }
   
   public List findByQuery(String querystr){
		return BaseDAOServcieUtil.findByQueryString(querystr);
   }
   
   public List getAll(){
		return BaseDAOServcieUtil.findByQueryString("from UserLogInfo");
	}
	
  public void saveOrUpdate(UserLogInfo bean){
		BaseDAOServcieUtil.saveOrUpdata(bean);
	}
 }
   