package openjframework.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import openjframework.*;
import java.io.Serializable;
import openjframework.model.*;
public interface MessageInfoService
{
     public void delete(MessageInfo bean);
     public Serializable create(MessageInfo bean);
     public void update(MessageInfo bean);
     public void saveOrUpdate(MessageInfo bean);
     public MessageInfo getById(String id);
     public MessageInfo getByPrimaryKey(String key);
     public List findByQuery(String querystr);
     public List getAll();
     
     
 }
   
    