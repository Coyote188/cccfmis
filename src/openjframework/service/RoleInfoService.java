package openjframework.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import openjframework.model.*;
public interface RoleInfoService
{
     public void delete(RoleInfo bean);
     public Serializable create(RoleInfo bean);
     public void update(RoleInfo bean);
     public void saveOrUpdate(RoleInfo bean);
     public RoleInfo getById(String id);
     public RoleInfo getByPrimaryKey(String key);
     public List findByQuery(String querystr);
     public List getAll();
     
     
 }
   
    