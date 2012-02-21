package cccf.ma.service.impl;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
import cccf.ma.service.*;
import com.aidi.core.service.*;
public class  PumperDocumentsInfoServiceImpl implements  PumperDocumentsInfoService
 {
 
   public Serializable create(PumperDocumentsInfo bean)
   {
   		return BaseDAOServcieUtil.save(bean);
   	}
   
   public void delete(PumperDocumentsInfo bean)
   {
   BaseDAOServcieUtil.remove(bean);}
   
   public void update(PumperDocumentsInfo bean)
   {
   BaseDAOServcieUtil.upDate(bean);}
   
   public PumperDocumentsInfo getById(String id)
   {
   return (PumperDocumentsInfo)BaseDAOServcieUtil.findById(PumperDocumentsInfo.class,id);
   }
   
   public PumperDocumentsInfo getByPrimaryKey(String key)
   {
   return (PumperDocumentsInfo)BaseDAOServcieUtil.findByPrimaryKey(PumperDocumentsInfo.class,key);
   }
   
   public List findByQuery(String querystr){
		return BaseDAOServcieUtil.findByQueryString(querystr);
   }
   
   public List getAll(){
		return BaseDAOServcieUtil.findByQueryString("from PumperDocumentsInfo");
	}
	
  public void saveOrUpdate(PumperDocumentsInfo bean){
		BaseDAOServcieUtil.saveOrUpdata(bean);
	}
 }
   