package openjframework.service.impl;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import openjframework.model.*;
import openjframework.service.*;
import com.aidi.core.service.*;
public class  ResourceTypeInfoServiceImpl implements  ResourceTypeInfoService
 {
 
   public Serializable create(ResourceTypeInfo bean)
   {
   		return BaseDAOServcieUtil.save(bean);
   	}
   
   public void delete(ResourceTypeInfo bean)
   {
   BaseDAOServcieUtil.remove(bean);}
   
   public void update(ResourceTypeInfo bean)
   {
   BaseDAOServcieUtil.upDate(bean);}
   
   public ResourceTypeInfo getById(String id)
   {
   return (ResourceTypeInfo)BaseDAOServcieUtil.findById(ResourceTypeInfo.class,id);
   }
   
   public ResourceTypeInfo getByPrimaryKey(String key)
   {
   return (ResourceTypeInfo)BaseDAOServcieUtil.findByPrimaryKey(ResourceTypeInfo.class,key);
   }
   
   public List findByQuery(String querystr){
		return BaseDAOServcieUtil.findByQueryString(querystr);
   }
   
   public List getAll(){
		return BaseDAOServcieUtil.findByQueryString("from ResourceTypeInfo");
	}
	
  public void saveOrUpdate(ResourceTypeInfo bean){
		BaseDAOServcieUtil.saveOrUpdata(bean);
	}
 }
   