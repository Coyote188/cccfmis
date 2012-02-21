package openjframework.service.impl;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import openjframework.model.*;
import openjframework.service.*;
import com.aidi.core.service.*;
public class  NoticeInfoServiceImpl implements  NoticeInfoService
 {
 
   public Serializable create(NoticeInfo bean)
   {
   		return BaseDAOServcieUtil.save(bean);
   	}
   
   public void delete(NoticeInfo bean)
   {
   BaseDAOServcieUtil.remove(bean);}
   
   public void update(NoticeInfo bean)
   {
   BaseDAOServcieUtil.upDate(bean);}
   
   public NoticeInfo getById(String id)
   {
   return (NoticeInfo)BaseDAOServcieUtil.findById(NoticeInfo.class,id);
   }
   
   public NoticeInfo getByPrimaryKey(String key)
   {
   return (NoticeInfo)BaseDAOServcieUtil.findByPrimaryKey(NoticeInfo.class,key);
   }
   
   public List findByQuery(String querystr){
		return BaseDAOServcieUtil.findByQueryString(querystr);
   }
   
   public List getAll(){
		return BaseDAOServcieUtil.findByQueryString("from NoticeInfo");
	}
	
  public void saveOrUpdate(NoticeInfo bean){
		BaseDAOServcieUtil.saveOrUpdata(bean);
	}
 }
   