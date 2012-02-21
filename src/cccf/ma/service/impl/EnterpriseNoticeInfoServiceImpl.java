package cccf.ma.service.impl;
import java.math.*;
import openjframework.model.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
import cccf.ma.service.*;
import com.aidi.core.service.*;
public class  EnterpriseNoticeInfoServiceImpl implements  EnterpriseNoticeInfoService
 {
 
   public Serializable create(EnterpriseNoticeInfo bean)
   {
   		return BaseDAOServcieUtil.save(bean);
   	}
   
   public void delete(EnterpriseNoticeInfo bean)
   {
   BaseDAOServcieUtil.remove(bean);}
   
   public void update(EnterpriseNoticeInfo bean)
   {
   BaseDAOServcieUtil.upDate(bean);}
   
   public EnterpriseNoticeInfo getById(String id)
   {
   return (EnterpriseNoticeInfo)BaseDAOServcieUtil.findById(EnterpriseNoticeInfo.class,id);
   }
   
   public EnterpriseNoticeInfo getByPrimaryKey(String key)
   {
   return (EnterpriseNoticeInfo)BaseDAOServcieUtil.findByPrimaryKey(EnterpriseNoticeInfo.class,key);
   }
   
   public List findByQuery(String querystr){
		return BaseDAOServcieUtil.findByQueryString(querystr);
   }
   
   public List getAll(){
		return BaseDAOServcieUtil.findByQueryString("from EnterpriseNoticeInfo");
	}
	
  public void saveOrUpdate(EnterpriseNoticeInfo bean){
		BaseDAOServcieUtil.saveOrUpdata(bean);
	}
 }
   