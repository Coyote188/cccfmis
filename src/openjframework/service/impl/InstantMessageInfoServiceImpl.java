package openjframework.service.impl;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import openjframework.model.*;
import openjframework.service.*;
import com.aidi.core.service.*;
public class  InstantMessageInfoServiceImpl implements  InstantMessageInfoService
 {
 
   public Serializable create(InstantMessageInfo bean)
   {
   		return BaseDAOServcieUtil.save(bean);
   	}
   
   public void delete(InstantMessageInfo bean)
   {
   BaseDAOServcieUtil.remove(bean);}
   
   public void update(InstantMessageInfo bean)
   {
   BaseDAOServcieUtil.upDate(bean);}
   
   public InstantMessageInfo getById(String id)
   {
   return (InstantMessageInfo)BaseDAOServcieUtil.findById(InstantMessageInfo.class,id);
   }
   
   public InstantMessageInfo getByPrimaryKey(String key)
   {
   return (InstantMessageInfo)BaseDAOServcieUtil.findByPrimaryKey(InstantMessageInfo.class,key);
   }
   
   public List findByQuery(String querystr){
		return BaseDAOServcieUtil.findByQueryString(querystr);
   }
   
   public List getAll(){
		return BaseDAOServcieUtil.findByQueryString("from InstantMessageInfo");
	}
	
  public void saveOrUpdate(InstantMessageInfo bean){
		BaseDAOServcieUtil.saveOrUpdata(bean);
	}
 }
   