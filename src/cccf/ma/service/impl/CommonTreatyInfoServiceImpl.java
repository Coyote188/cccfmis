package cccf.ma.service.impl;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
import cccf.ma.service.*;
import com.aidi.core.service.*;
public class  CommonTreatyInfoServiceImpl implements  CommonTreatyInfoService
 {
 
   public Serializable create(CommonTreatyInfo bean)
   {
   		return BaseDAOServcieUtil.save(bean);
   	}
   
   public void delete(CommonTreatyInfo bean)
   {
   BaseDAOServcieUtil.remove(bean);}
   
   public void update(CommonTreatyInfo bean)
   {
   BaseDAOServcieUtil.upDate(bean);}
   
   public CommonTreatyInfo getById(String id)
   {
   return (CommonTreatyInfo)BaseDAOServcieUtil.findById(CommonTreatyInfo.class,id);
   }
   
   public CommonTreatyInfo getByPrimaryKey(String key)
   {
   return (CommonTreatyInfo)BaseDAOServcieUtil.findByPrimaryKey(CommonTreatyInfo.class,key);
   }
   
   public List findByQuery(String querystr){
		return BaseDAOServcieUtil.findByQueryString(querystr);
   }
   
   public List getAll(){
		return BaseDAOServcieUtil.findByQueryString("from CommonTreatyInfo");
	}
	
  public void saveOrUpdate(CommonTreatyInfo bean){
		BaseDAOServcieUtil.saveOrUpdata(bean);
	}
 }
   