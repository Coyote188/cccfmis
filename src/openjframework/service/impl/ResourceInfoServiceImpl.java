package openjframework.service.impl;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import openjframework.model.*;
import openjframework.service.*;
import com.aidi.core.service.*;
public class  ResourceInfoServiceImpl implements  ResourceInfoService
 {
 
   public Serializable create(ResourceInfo bean)
   {
   		return BaseDAOServcieUtil.save(bean);
   	}
   
   public void delete(ResourceInfo bean)
   {
   BaseDAOServcieUtil.remove(bean);}
   
   public void update(ResourceInfo bean)
   {
   BaseDAOServcieUtil.upDate(bean);}
   
   public ResourceInfo getById(String id)
   {
   return (ResourceInfo)BaseDAOServcieUtil.findById(ResourceInfo.class,id);
   }
   
   public ResourceInfo getByPrimaryKey(String key)
   {
   return (ResourceInfo)BaseDAOServcieUtil.findByPrimaryKey(ResourceInfo.class,key);
   }
   
   public List findByQuery(String querystr){
		return BaseDAOServcieUtil.findByQueryString(querystr);
   }
   
   public List getAll(){
		return BaseDAOServcieUtil.findByQueryString("from ResourceInfo");
	}
	
  public void saveOrUpdate(ResourceInfo bean){
		BaseDAOServcieUtil.saveOrUpdata(bean);
	}
 }
   