package openjframework.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import openjframework.model.*;
public interface ResourceTypeInfoService
{
     public void delete(ResourceTypeInfo bean);
     public Serializable create(ResourceTypeInfo bean);
     public void update(ResourceTypeInfo bean);
     public void saveOrUpdate(ResourceTypeInfo bean);
     public ResourceTypeInfo getById(String id);
     public ResourceTypeInfo getByPrimaryKey(String key);
     public List findByQuery(String querystr);
     public List getAll();
     
     
 }
   
    