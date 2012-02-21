package cccf.ma.service.impl;
import java.math.*;
import openjframework.model.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
import cccf.ma.service.*;
import com.aidi.core.service.*;
public class  AppStatusRecordInfoServiceImpl implements  AppStatusRecordInfoService
 {
 
   public Serializable create(AppStatusRecordInfo bean)
   {
   		return BaseDAOServcieUtil.save(bean);
   	}
   
   public void delete(AppStatusRecordInfo bean)
   {
   BaseDAOServcieUtil.remove(bean);}
   
   public void update(AppStatusRecordInfo bean)
   {
   BaseDAOServcieUtil.upDate(bean);}
   
   public AppStatusRecordInfo getById(String id)
   {
   return (AppStatusRecordInfo)BaseDAOServcieUtil.findById(AppStatusRecordInfo.class,id);
   }
   
   public AppStatusRecordInfo getByPrimaryKey(String key)
   {
   return (AppStatusRecordInfo)BaseDAOServcieUtil.findByPrimaryKey(AppStatusRecordInfo.class,key);
   }
   
   public List findByQuery(String querystr){
		return BaseDAOServcieUtil.findByQueryString(querystr);
   }
   
   public List getAll(){
		return BaseDAOServcieUtil.findByQueryString("from AppStatusRecordInfo");
	}
	
  public void saveOrUpdate(AppStatusRecordInfo bean){
		BaseDAOServcieUtil.saveOrUpdata(bean);
	}
 }
   