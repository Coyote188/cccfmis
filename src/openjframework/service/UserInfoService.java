package openjframework.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import openjframework.model.*;
public interface UserInfoService
{
     public void delete(UserInfo bean);
     public Serializable create(UserInfo bean);
     public void update(UserInfo bean);
     public void saveOrUpdate(UserInfo bean);
     public UserInfo getById(String id);
     public UserInfo getByPrimaryKey(String key);
     public List findByQuery(String querystr);
     public List getAll();
     
     
 }
   
    