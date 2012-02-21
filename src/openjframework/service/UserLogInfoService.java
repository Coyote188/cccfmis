package openjframework.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import openjframework.model.*;
public interface UserLogInfoService
{
     public void delete(UserLogInfo bean);
     public Serializable create(UserLogInfo bean);
     public void update(UserLogInfo bean);
     public void saveOrUpdate(UserLogInfo bean);
     public UserLogInfo getById(String id);
     public UserLogInfo getByPrimaryKey(String key);
     public List findByQuery(String querystr);
     public List getAll();
     
     
 }
   
    