package openjframework.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import openjframework.model.*;
public interface PermissionInfoService
{
     public void delete(PermissionInfo bean);
     public Serializable create(PermissionInfo bean);
     public void update(PermissionInfo bean);
     public void saveOrUpdate(PermissionInfo bean);
     public PermissionInfo getById(String id);
     public PermissionInfo getByPrimaryKey(String key);
     public List findByQuery(String querystr);
     public List getAll();
     
     
 }
   
    