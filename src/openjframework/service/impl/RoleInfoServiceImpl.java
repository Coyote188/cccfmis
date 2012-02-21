package openjframework.service.impl;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import openjframework.model.*;
import openjframework.service.*;
import com.aidi.core.service.*;
public class  RoleInfoServiceImpl implements  RoleInfoService
 {
 
   public Serializable create(RoleInfo bean)
   {
   		return BaseDAOServcieUtil.save(bean);
   	}
   
   public void delete(RoleInfo bean)
   {
   BaseDAOServcieUtil.remove(bean);}
   
   public void update(RoleInfo bean)
   {
   BaseDAOServcieUtil.upDate(bean);}
   
   public RoleInfo getById(String id)
   {
   return (RoleInfo)BaseDAOServcieUtil.findById(RoleInfo.class,id);
   }
   
   public RoleInfo getByPrimaryKey(String key)
   {
   return (RoleInfo)BaseDAOServcieUtil.findByPrimaryKey(RoleInfo.class,key);
   }
   
   public List findByQuery(String querystr){
		return BaseDAOServcieUtil.findByQueryString(querystr);
   }
   
   public List getAll(){
		return BaseDAOServcieUtil.findByQueryString("from RoleInfo");
	}
	
  public void saveOrUpdate(RoleInfo bean){
		BaseDAOServcieUtil.saveOrUpdata(bean);
	}
 }
   