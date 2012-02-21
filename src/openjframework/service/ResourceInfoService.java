package openjframework.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import openjframework.model.*;
public interface ResourceInfoService
{
     public void delete(ResourceInfo bean);
     public Serializable create(ResourceInfo bean);
     public void update(ResourceInfo bean);
     public void saveOrUpdate(ResourceInfo bean);
     public ResourceInfo getById(String id);
     public ResourceInfo getByPrimaryKey(String key);
     public List findByQuery(String querystr);
     public List getAll();
     
     
 }
   
    