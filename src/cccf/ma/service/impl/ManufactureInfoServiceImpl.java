package cccf.ma.service.impl;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
import cccf.ma.service.*;
import com.aidi.core.service.*;
public class  ManufactureInfoServiceImpl implements  ManufactureInfoService
 {
 
   public Serializable create(ManufactureInfo bean)
   {
   		return BaseDAOServcieUtil.save(bean);
   	}
   
   public void delete(ManufactureInfo bean)
   {
   BaseDAOServcieUtil.remove(bean);}
   
   public void update(ManufactureInfo bean)
   {
   BaseDAOServcieUtil.upDate(bean);}
   
   public ManufactureInfo getById(String id)
   {
   return (ManufactureInfo)BaseDAOServcieUtil.findById(ManufactureInfo.class,id);
   }
   
   public ManufactureInfo getByPrimaryKey(String key)
   {
   return (ManufactureInfo)BaseDAOServcieUtil.findByPrimaryKey(ManufactureInfo.class,key);
   }
   
   public List findByQuery(String querystr){
		return BaseDAOServcieUtil.findByQueryString(querystr);
   }
   
   public List getAll(){
		return BaseDAOServcieUtil.findByQueryString("from ManufactureInfo");
	}
	
  public void saveOrUpdate(ManufactureInfo bean){
		BaseDAOServcieUtil.saveOrUpdata(bean);
	}
 }
   