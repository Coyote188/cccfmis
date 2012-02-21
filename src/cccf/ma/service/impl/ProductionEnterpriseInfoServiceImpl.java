package cccf.ma.service.impl;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
import cccf.ma.service.*;
import com.aidi.core.service.*;
public class  ProductionEnterpriseInfoServiceImpl implements  ProductionEnterpriseInfoService
 {
 
   public Serializable create(ProductionEnterpriseInfo bean)
   {
   		return BaseDAOServcieUtil.save(bean);
   	}
   
   public void delete(ProductionEnterpriseInfo bean)
   {
   BaseDAOServcieUtil.remove(bean);}
   
   public void update(ProductionEnterpriseInfo bean)
   {
   BaseDAOServcieUtil.upDate(bean);}
   
   public ProductionEnterpriseInfo getById(String id)
   {
   return (ProductionEnterpriseInfo)BaseDAOServcieUtil.findById(ProductionEnterpriseInfo.class,id);
   }
   
   public ProductionEnterpriseInfo getByPrimaryKey(String key)
   {
   return (ProductionEnterpriseInfo)BaseDAOServcieUtil.findByPrimaryKey(ProductionEnterpriseInfo.class,key);
   }
   
   public List findByQuery(String querystr){
		return BaseDAOServcieUtil.findByQueryString(querystr);
   }
   
   public List getAll(){
		return BaseDAOServcieUtil.findByQueryString("from ProductionEnterpriseInfo");
	}
	
  public void saveOrUpdate(ProductionEnterpriseInfo bean){
		BaseDAOServcieUtil.saveOrUpdata(bean);
	}
 }
   