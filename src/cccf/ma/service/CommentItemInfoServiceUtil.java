package cccf.ma.service;
import java.math.*;
import openjframework.model.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
import com.aidi.core.web.framework.ServiceAdapter;
import com.aidi.core.spring.CustomerContextHolder;
public class CommentItemInfoServiceUtil
{
    public static Serializable  create(CommentItemInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
       CommentItemInfoService service=(CommentItemInfoService)ServiceAdapter.getServiceByName("CommentItemInfoService");
       return service.create(bean);
    }
    public static void delete(CommentItemInfo bean){
    	CustomerContextHolder.setCustomerType("cccf");
        CommentItemInfoService service=(CommentItemInfoService)ServiceAdapter.getServiceByName("CommentItemInfoService");
       service.delete(bean);
       }
    public static void saveOrUpdate(CommentItemInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        CommentItemInfoService service=(CommentItemInfoService)ServiceAdapter.getServiceByName("CommentItemInfoService");
       service.saveOrUpdate(bean);
       }
    public static void update(CommentItemInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        CommentItemInfoService service=(CommentItemInfoService)ServiceAdapter.getServiceByName("CommentItemInfoService");
       service.update(bean);
       }
    public static List getAll(){
       CustomerContextHolder.setCustomerType("cccf");
        CommentItemInfoService service=(CommentItemInfoService)ServiceAdapter.getServiceByName("CommentItemInfoService");
       return service.getAll();
       }
    public static CommentItemInfo getById(String id){
    	CustomerContextHolder.setCustomerType("cccf");
        CommentItemInfoService service=(CommentItemInfoService)ServiceAdapter.getServiceByName("CommentItemInfoService");
       return service.getById(id);
       }
     public static CommentItemInfo getByPrimaryKey(String key){
    	CustomerContextHolder.setCustomerType("cccf");
        CommentItemInfoService service=(CommentItemInfoService)ServiceAdapter.getServiceByName("CommentItemInfoService");
       return service.getByPrimaryKey(key);
       }
    public static List findByQuery(String querystr){
        CustomerContextHolder.setCustomerType("cccf");
        CommentItemInfoService service=(CommentItemInfoService)ServiceAdapter.getServiceByName("CommentItemInfoService");
       return service.findByQuery(querystr);
       }
    public static List findByUserInfo(UserInfo user){
    	String querystr=" from CommentItemInfo where user.id='"+user.getId()+"'  order by createdTime desc ";
        CustomerContextHolder.setCustomerType("cccf");
        CommentItemInfoService service=(CommentItemInfoService)ServiceAdapter.getServiceByName("CommentItemInfoService");
       return service.findByQuery(querystr);
       }
}
   