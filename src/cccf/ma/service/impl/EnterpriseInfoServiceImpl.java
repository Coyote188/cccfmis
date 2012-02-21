package cccf.ma.service.impl;
import java.math.*;
import openjframework.model.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
import cccf.ma.service.*;
import cccf.myenum.ActivateStatus;

import com.aidi.core.service.*;
public class  EnterpriseInfoServiceImpl implements  EnterpriseInfoService
 {
 
   public Serializable create(EnterpriseInfo bean)
   {
   	return BaseDAOServcieUtil.save(bean);}
   
   public void delete(EnterpriseInfo bean)
   {
   BaseDAOServcieUtil.remove(bean);}
   
   public void update(EnterpriseInfo bean)
   {
   BaseDAOServcieUtil.upDate(bean);}
   
   public EnterpriseInfo getById(String id)
   {
   return (EnterpriseInfo)BaseDAOServcieUtil.findById(EnterpriseInfo.class,id);
   }
   
   public EnterpriseInfo getByPrimaryKey(String key)
   {
   return (EnterpriseInfo)BaseDAOServcieUtil.findByPrimaryKey(EnterpriseInfo.class,key);
   }
   
   public List findByQuery(String querystr){
		return BaseDAOServcieUtil.findByQueryString(querystr);
   }
   
   public List getAll(){
		return BaseDAOServcieUtil.findByQueryString("from EnterpriseInfo");
	}
	
  public void saveOrUpdate(EnterpriseInfo bean){
		BaseDAOServcieUtil.saveOrUpdata(bean);
	}
  
  public void saveImage(){
	  
  }
 }
   