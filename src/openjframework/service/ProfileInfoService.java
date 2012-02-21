package openjframework.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import openjframework.model.*;
public interface ProfileInfoService
{
     public void delete(ProfileInfo bean);
     public Serializable create(ProfileInfo bean);
     public void update(ProfileInfo bean);
     public void saveOrUpdate(ProfileInfo bean);
     public ProfileInfo getById(String id);
     public ProfileInfo getByPrimaryKey(String key);
     public List findByQuery(String querystr);
     public List getAll();
     
     
 }
   
    