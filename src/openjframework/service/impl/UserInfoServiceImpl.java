package openjframework.service.impl;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import openjframework.model.*;
import openjframework.service.*;
import com.aidi.core.service.*;
public class  UserInfoServiceImpl implements  UserInfoService
 {
 
   public Serializable create(UserInfo bean)
   {
   		return BaseDAOServcieUtil.save(bean);
   	}
   
   public void delete(UserInfo bean)
   {
   BaseDAOServcieUtil.remove(bean);}
   
   public void update(UserInfo bean)
   {
   BaseDAOServcieUtil.upDate(bean);}
   
   public UserInfo getById(String id)
   {
   return (UserInfo)BaseDAOServcieUtil.findById(UserInfo.class,id);
   }
   
   public UserInfo getByPrimaryKey(String key)
   {
   return (UserInfo)BaseDAOServcieUtil.findByPrimaryKey(UserInfo.class,key);
   }
   
   public List findByQuery(String querystr){
		return BaseDAOServcieUtil.findByQueryString(querystr);
   }
   
   public List getAll(){
		return BaseDAOServcieUtil.findByQueryString("from UserInfo");
	}
	
  public void saveOrUpdate(UserInfo bean){
		BaseDAOServcieUtil.saveOrUpdata(bean);
	}
 }
   