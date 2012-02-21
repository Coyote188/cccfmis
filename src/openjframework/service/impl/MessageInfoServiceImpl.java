package openjframework.service.impl;
import java.math.*;
import java.util.*;
import java.sql.*;
import openjframework.*;
import java.io.Serializable;
import openjframework.model.*;
import openjframework.service.*;
import com.aidi.core.service.*;
public class  MessageInfoServiceImpl implements  MessageInfoService
 {
 
   public Serializable create(MessageInfo bean)
   {
   		return BaseDAOServcieUtil.save(bean);
   	}
   
   public void delete(MessageInfo bean)
   {
   BaseDAOServcieUtil.remove(bean);}
   
   public void update(MessageInfo bean)
   {
   BaseDAOServcieUtil.upDate(bean);}
   
   public MessageInfo getById(String id)
   {
   return (MessageInfo)BaseDAOServcieUtil.findById(MessageInfo.class,id);
   }
   
   public MessageInfo getByPrimaryKey(String key)
   {
   return (MessageInfo)BaseDAOServcieUtil.findByPrimaryKey(MessageInfo.class,key);
   }
   
   public List findByQuery(String querystr){
		return BaseDAOServcieUtil.findByQueryString(querystr);
   }
   
   public List getAll(){
		return BaseDAOServcieUtil.findByQueryString("from MessageInfo");
	}
	
  public void saveOrUpdate(MessageInfo bean){
		BaseDAOServcieUtil.saveOrUpdata(bean);
	}
 }
   