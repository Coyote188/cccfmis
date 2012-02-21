package cccf.ma.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
public interface LocationInfoService
{
     public void delete(LocationInfo bean);
     public Serializable create(LocationInfo bean);
     public void update(LocationInfo bean);
     public void saveOrUpdate(LocationInfo bean);
     public LocationInfo getById(String id);
     public LocationInfo getByPrimaryKey(String key);
     public List findByQuery(String querystr);
     public List getAll();
     
     
 }
   
    