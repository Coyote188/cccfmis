package openjframework.service.impl;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import openjframework.model.*;
import openjframework.service.*;
import com.aidi.core.service.*;
public class  PermissionInfoServiceImpl implements  PermissionInfoService
 {
 
   public Serializable create(PermissionInfo bean)
   {
   		return BaseDAOServcieUtil.save(bean);
   	}
   
   public void delete(PermissionInfo bean)
   {
   BaseDAOServcieUtil.remove(bean);}
   
   public void update(PermissionInfo bean)
   {
   BaseDAOServcieUtil.upDate(bean);}
   
   public PermissionInfo getById(String id)
   {
   return (PermissionInfo)BaseDAOServcieUtil.findById(PermissionInfo.class,id);
   }
   
   public PermissionInfo getByPrimaryKey(String key)
   {
   return (PermissionInfo)BaseDAOServcieUtil.findByPrimaryKey(PermissionInfo.class,key);
   }
   
   public List findByQuery(String querystr){
		return BaseDAOServcieUtil.findByQueryString(querystr);
   }
   
   public List getAll(){
		return BaseDAOServcieUtil.findByQueryString("from PermissionInfo");
	}
	
  public void saveOrUpdate(PermissionInfo bean){
		BaseDAOServcieUtil.saveOrUpdata(bean);
	}
 }
   