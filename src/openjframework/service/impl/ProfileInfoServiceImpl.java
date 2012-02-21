package openjframework.service.impl;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import openjframework.model.*;
import openjframework.service.*;
import com.aidi.core.service.*;
public class  ProfileInfoServiceImpl implements  ProfileInfoService
 {
 
   public Serializable create(ProfileInfo bean)
   {
   		return BaseDAOServcieUtil.save(bean);
   	}
   
   public void delete(ProfileInfo bean)
   {
   BaseDAOServcieUtil.remove(bean);}
   
   public void update(ProfileInfo bean)
   {
   BaseDAOServcieUtil.upDate(bean);}
   
   public ProfileInfo getById(String id)
   {
   return (ProfileInfo)BaseDAOServcieUtil.findById(ProfileInfo.class,id);
   }
   
   public ProfileInfo getByPrimaryKey(String key)
   {
   return (ProfileInfo)BaseDAOServcieUtil.findByPrimaryKey(ProfileInfo.class,key);
   }
   
   public List findByQuery(String querystr){
		return BaseDAOServcieUtil.findByQueryString(querystr);
   }
   
   public List getAll(){
		return BaseDAOServcieUtil.findByQueryString("from ProfileInfo");
	}
	
  public void saveOrUpdate(ProfileInfo bean){
		BaseDAOServcieUtil.saveOrUpdata(bean);
	}
 }
   