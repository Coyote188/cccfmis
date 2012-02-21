package openjframework.service.impl;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import openjframework.model.*;
import openjframework.service.*;
import com.aidi.core.service.*;
public class  OrganizationInfoServiceImpl implements  OrganizationInfoService
 {
 
   public Serializable create(OrganizationInfo bean)
   {
   		return BaseDAOServcieUtil.save(bean);
   	}
   
   public void delete(OrganizationInfo bean)
   {
   BaseDAOServcieUtil.remove(bean);}
   
   public void update(OrganizationInfo bean)
   {
   BaseDAOServcieUtil.upDate(bean);}
   
   public OrganizationInfo getById(String id)
   {
   return (OrganizationInfo)BaseDAOServcieUtil.findById(OrganizationInfo.class,id);
   }
   
   public OrganizationInfo getByPrimaryKey(String key)
   {
   return (OrganizationInfo)BaseDAOServcieUtil.findByPrimaryKey(OrganizationInfo.class,key);
   }
   
   public List findByQuery(String querystr){
		return BaseDAOServcieUtil.findByQueryString(querystr);
   }
   
   public List getAll(){
		return BaseDAOServcieUtil.findByQueryString("from OrganizationInfo");
	}
	
  public void saveOrUpdate(OrganizationInfo bean){
		BaseDAOServcieUtil.saveOrUpdata(bean);
	}
 }
   