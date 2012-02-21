package openjframework.service.impl;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import openjframework.model.*;
import openjframework.service.*;
import com.aidi.core.service.*;
public class  PoliticalStatusInfoServiceImpl implements  PoliticalStatusInfoService
 {
 
   public Serializable create(PoliticalStatusInfo bean)
   {
   		return BaseDAOServcieUtil.save(bean);
   	}
   
   public void delete(PoliticalStatusInfo bean)
   {
   BaseDAOServcieUtil.remove(bean);}
   
   public void update(PoliticalStatusInfo bean)
   {
   BaseDAOServcieUtil.upDate(bean);}
   
   public PoliticalStatusInfo getById(String id)
   {
   return (PoliticalStatusInfo)BaseDAOServcieUtil.findById(PoliticalStatusInfo.class,id);
   }
   
   public PoliticalStatusInfo getByPrimaryKey(String key)
   {
   return (PoliticalStatusInfo)BaseDAOServcieUtil.findByPrimaryKey(PoliticalStatusInfo.class,key);
   }
   
   public List findByQuery(String querystr){
		return BaseDAOServcieUtil.findByQueryString(querystr);
   }
   
   public List getAll(){
		return BaseDAOServcieUtil.findByQueryString("from PoliticalStatusInfo");
	}
	
  public void saveOrUpdate(PoliticalStatusInfo bean){
		BaseDAOServcieUtil.saveOrUpdata(bean);
	}
 }
   