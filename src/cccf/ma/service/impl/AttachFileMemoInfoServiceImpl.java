package cccf.ma.service.impl;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
import cccf.ma.service.*;
import com.aidi.core.service.*;
public class  AttachFileMemoInfoServiceImpl implements  AttachFileMemoInfoService
 {
 
   public Serializable create(AttachFileMemoInfo bean)
   {
   		return BaseDAOServcieUtil.save(bean);
   	}
   
   public void delete(AttachFileMemoInfo bean)
   {
   BaseDAOServcieUtil.remove(bean);}
   
   public void update(AttachFileMemoInfo bean)
   {
   BaseDAOServcieUtil.upDate(bean);}
   
   public AttachFileMemoInfo getById(String id)
   {
   return (AttachFileMemoInfo)BaseDAOServcieUtil.findById(AttachFileMemoInfo.class,id);
   }
   
   public AttachFileMemoInfo getByPrimaryKey(String key)
   {
   return (AttachFileMemoInfo)BaseDAOServcieUtil.findByPrimaryKey(AttachFileMemoInfo.class,key);
   }
   
   public List findByQuery(String querystr){
		return BaseDAOServcieUtil.findByQueryString(querystr);
   }
   
   public List getAll(){
		return BaseDAOServcieUtil.findByQueryString("from AttachFileMemoInfo");
	}
	
  public void saveOrUpdate(AttachFileMemoInfo bean){
		BaseDAOServcieUtil.saveOrUpdata(bean);
	}
 }
   