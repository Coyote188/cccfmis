package cccf.ma.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
public interface CommonTreatyInfoService
{
     public void delete(CommonTreatyInfo bean);
     public Serializable create(CommonTreatyInfo bean);
     public void update(CommonTreatyInfo bean);
     public void saveOrUpdate(CommonTreatyInfo bean);
     public CommonTreatyInfo getById(String id);
     public CommonTreatyInfo getByPrimaryKey(String key);
     public List findByQuery(String querystr);
     public List getAll();
     
     
 }
   
    