package cccf.ma.service.impl;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
import cccf.ma.service.*;
import com.aidi.core.service.*;
public class  FeeDetailInfoServiceImpl implements  FeeDetailInfoService
 {
 
   public Serializable create(FeeDetailInfo bean)
   {
   		return BaseDAOServcieUtil.save(bean);
   	}
   
   public void delete(FeeDetailInfo bean)
   {
   BaseDAOServcieUtil.remove(bean);}
   
   public void update(FeeDetailInfo bean)
   {
   BaseDAOServcieUtil.upDate(bean);}
   
   public FeeDetailInfo getById(String id)
   {
   return (FeeDetailInfo)BaseDAOServcieUtil.findById(FeeDetailInfo.class,id);
   }
   
   public FeeDetailInfo getByPrimaryKey(String key)
   {
   return (FeeDetailInfo)BaseDAOServcieUtil.findByPrimaryKey(FeeDetailInfo.class,key);
   }
   
   public List findByQuery(String querystr){
		return BaseDAOServcieUtil.findByQueryString(querystr);
   }
   
   public List getAll(){
		return BaseDAOServcieUtil.findByQueryString("from FeeDetailInfo");
	}
	
  public void saveOrUpdate(FeeDetailInfo bean){
		BaseDAOServcieUtil.saveOrUpdata(bean);
	}
 }
   