package cccf.ma.service.impl;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
import cccf.ma.service.*;
import com.aidi.core.service.*;
public class  LocationInfoServiceImpl implements  LocationInfoService
 {
 
   public Serializable create(LocationInfo bean)
   {
   		return BaseDAOServcieUtil.save(bean);
   	}
   
   public void delete(LocationInfo bean)
   {
   BaseDAOServcieUtil.remove(bean);}
   
   public void update(LocationInfo bean)
   {
   BaseDAOServcieUtil.upDate(bean);}
   
   public LocationInfo getById(String id)
   {
   return (LocationInfo)BaseDAOServcieUtil.findById(LocationInfo.class,id);
   }
   
   public LocationInfo getByPrimaryKey(String key)
   {
   return (LocationInfo)BaseDAOServcieUtil.findByPrimaryKey(LocationInfo.class,key);
   }
   
   public List findByQuery(String querystr){
		return BaseDAOServcieUtil.findByQueryString(querystr);
   }
   
   public List getAll(){
		return BaseDAOServcieUtil.findByQueryString("from LocationInfo");
	}
	
  public void saveOrUpdate(LocationInfo bean){
		BaseDAOServcieUtil.saveOrUpdata(bean);
	}
 }
   