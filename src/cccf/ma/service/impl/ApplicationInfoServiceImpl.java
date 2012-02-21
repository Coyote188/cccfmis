package cccf.ma.service.impl;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
import cccf.ma.service.*;
import com.aidi.core.service.*;
public class  ApplicationInfoServiceImpl implements  ApplicationInfoService
 {
 
   public Serializable create(ApplicationInfo bean)
   {
   		return BaseDAOServcieUtil.save(bean);
   	}
   
   public void delete(ApplicationInfo bean)
   {
   BaseDAOServcieUtil.remove(bean);}
   
   public void update(ApplicationInfo bean)
   {
   BaseDAOServcieUtil.upDate(bean);}
   
   public ApplicationInfo getById(String id)
   {
   return (ApplicationInfo)BaseDAOServcieUtil.findById(ApplicationInfo.class,id);
   }
   
   public ApplicationInfo getByPrimaryKey(String key)
   {
   return (ApplicationInfo)BaseDAOServcieUtil.findByPrimaryKey(ApplicationInfo.class,key);
   }
   
   public List findByQuery(String querystr){
		return BaseDAOServcieUtil.findByQueryString(querystr);
   }
   
   public List getAll(){
		return BaseDAOServcieUtil.findByQueryString("from ApplicationInfo");
	}
	
  public void saveOrUpdate(ApplicationInfo bean){
		BaseDAOServcieUtil.saveOrUpdata(bean);
	}
 }
   