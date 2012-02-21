package openjframework.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import openjframework.model.*;
public interface NoticeInfoService
{
     public void delete(NoticeInfo bean);
     public Serializable create(NoticeInfo bean);
     public void update(NoticeInfo bean);
     public void saveOrUpdate(NoticeInfo bean);
     public NoticeInfo getById(String id);
     public NoticeInfo getByPrimaryKey(String key);
     public List findByQuery(String querystr);
     public List getAll();
     
     
 }
   
    