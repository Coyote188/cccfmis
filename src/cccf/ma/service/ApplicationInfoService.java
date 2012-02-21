package cccf.ma.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
public interface ApplicationInfoService
{
     public void delete(ApplicationInfo bean);
     public Serializable create(ApplicationInfo bean);
     public void update(ApplicationInfo bean);
     public void saveOrUpdate(ApplicationInfo bean);
     public ApplicationInfo getById(String id);
     public ApplicationInfo getByPrimaryKey(String key);
     public List findByQuery(String querystr);
     public List getAll();
     
     
 }
   
    