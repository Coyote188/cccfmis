package cccf.ma.service.impl;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
import cccf.ma.service.*;
import com.aidi.core.service.*;
public class  StateInfoServiceImpl implements  StateInfoService
 {
 
   public Serializable create(StateInfo bean)
   {
   		return BaseDAOServcieUtil.save(bean);
   	}
   
   public void delete(StateInfo bean)
   {
   BaseDAOServcieUtil.remove(bean);}
   
   public void update(StateInfo bean)
   {
   BaseDAOServcieUtil.upDate(bean);}
   
   public StateInfo getById(String id)
   {
   return (StateInfo)BaseDAOServcieUtil.findById(StateInfo.class,id);
   }
   
   public StateInfo getByPrimaryKey(String key)
   {
   return (StateInfo)BaseDAOServcieUtil.findByPrimaryKey(StateInfo.class,key);
   }
   
   public List findByQuery(String querystr){
		return BaseDAOServcieUtil.findByQueryString(querystr);
   }
   
   public List getAll(){
		return BaseDAOServcieUtil.findByQueryString("from StateInfo");
	}
	
  public void saveOrUpdate(StateInfo bean){
		BaseDAOServcieUtil.saveOrUpdata(bean);
	}
 }
   