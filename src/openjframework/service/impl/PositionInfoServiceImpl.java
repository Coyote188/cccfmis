package openjframework.service.impl;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import openjframework.model.*;
import openjframework.service.*;
import com.aidi.core.service.*;
public class  PositionInfoServiceImpl implements  PositionInfoService
 {
 
   public Serializable create(PositionInfo bean)
   {
   		return BaseDAOServcieUtil.save(bean);
   	}
   
   public void delete(PositionInfo bean)
   {
   BaseDAOServcieUtil.remove(bean);}
   
   public void update(PositionInfo bean)
   {
   BaseDAOServcieUtil.upDate(bean);}
   
   public PositionInfo getById(String id)
   {
   return (PositionInfo)BaseDAOServcieUtil.findById(PositionInfo.class,id);
   }
   
   public PositionInfo getByPrimaryKey(String key)
   {
   return (PositionInfo)BaseDAOServcieUtil.findByPrimaryKey(PositionInfo.class,key);
   }
   
   public List findByQuery(String querystr){
		return BaseDAOServcieUtil.findByQueryString(querystr);
   }
   
   public List getAll(){
		return BaseDAOServcieUtil.findByQueryString("from PositionInfo");
	}
	
  public void saveOrUpdate(PositionInfo bean){
		BaseDAOServcieUtil.saveOrUpdata(bean);
	}
 }
   