package cccf.ma.service.impl;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
import cccf.ma.service.*;
import com.aidi.core.service.*;
public class  ApplicationPublicInfoServiceImpl implements  ApplicationPublicInfoService
 {
 
   public Serializable create(ApplicationPublicInfo bean)
   {
   		return BaseDAOServcieUtil.save(bean);
   	}
   
   public void delete(ApplicationPublicInfo bean)
   {
   BaseDAOServcieUtil.remove(bean);}
   
   public void update(ApplicationPublicInfo bean)
   {
   BaseDAOServcieUtil.upDate(bean);}
   
   public ApplicationPublicInfo getById(String id)
   {
   return (ApplicationPublicInfo)BaseDAOServcieUtil.findById(ApplicationPublicInfo.class,id);
   }
   
   public ApplicationPublicInfo getByPrimaryKey(String key)
   {
   return (ApplicationPublicInfo)BaseDAOServcieUtil.findByPrimaryKey(ApplicationPublicInfo.class,key);
   }
   
   public List findByQuery(String querystr){
		return BaseDAOServcieUtil.findByQueryString(querystr);
   }
   
   public List getAll(){
		return BaseDAOServcieUtil.findByQueryString("from ApplicationPublicInfo");
	}
	
  public void saveOrUpdate(ApplicationPublicInfo bean){
		BaseDAOServcieUtil.saveOrUpdata(bean);
	}
 }
   