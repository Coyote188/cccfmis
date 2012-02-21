package cccf.ma.service.impl;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
import cccf.ma.service.*;
import com.aidi.core.service.*;
public class  ProductionModelInfoServiceImpl implements  ProductionModelInfoService
 {
 
   public Serializable create(ProductionModelInfo bean)
   {
   		return BaseDAOServcieUtil.save(bean);
   	}
   
   public void delete(ProductionModelInfo bean)
   {
   BaseDAOServcieUtil.remove(bean);}
   
   public void update(ProductionModelInfo bean)
   {
   BaseDAOServcieUtil.upDate(bean);}
   
   public ProductionModelInfo getById(String id)
   {
   return (ProductionModelInfo)BaseDAOServcieUtil.findById(ProductionModelInfo.class,id);
   }
   
   public ProductionModelInfo getByPrimaryKey(String key)
   {
   return (ProductionModelInfo)BaseDAOServcieUtil.findByPrimaryKey(ProductionModelInfo.class,key);
   }
   
   public List findByQuery(String querystr){
		return BaseDAOServcieUtil.findByQueryString(querystr);
   }
   
   public List getAll(){
		return BaseDAOServcieUtil.findByQueryString("from ProductionModelInfo");
	}
	
  public void saveOrUpdate(ProductionModelInfo bean){
		BaseDAOServcieUtil.saveOrUpdata(bean);
	}
 }
   