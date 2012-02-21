package cccf.ma.service.impl;
import java.math.*;
import openjframework.model.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
import cccf.ma.service.*;
import com.aidi.core.service.*;
public class  CommentItemInfoServiceImpl implements  CommentItemInfoService
 {
 
   public Serializable create(CommentItemInfo bean)
   {
   		return BaseDAOServcieUtil.save(bean);
   	}
   
   public void delete(CommentItemInfo bean)
   {
   BaseDAOServcieUtil.remove(bean);}
   
   public void update(CommentItemInfo bean)
   {
   BaseDAOServcieUtil.upDate(bean);}
   
   public CommentItemInfo getById(String id)
   {
   return (CommentItemInfo)BaseDAOServcieUtil.findById(CommentItemInfo.class,id);
   }
   
   public CommentItemInfo getByPrimaryKey(String key)
   {
   return (CommentItemInfo)BaseDAOServcieUtil.findByPrimaryKey(CommentItemInfo.class,key);
   }
   
   public List findByQuery(String querystr){
		return BaseDAOServcieUtil.findByQueryString(querystr);
   }
   
   public List getAll(){
		return BaseDAOServcieUtil.findByQueryString("from CommentItemInfo");
	}
	
  public void saveOrUpdate(CommentItemInfo bean){
		BaseDAOServcieUtil.saveOrUpdata(bean);
	}
 }
   