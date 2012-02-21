package cccf.ma.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
public interface ApplicationPublicInfoService
{
     public void delete(ApplicationPublicInfo bean);
     public Serializable create(ApplicationPublicInfo bean);
     public void update(ApplicationPublicInfo bean);
     public void saveOrUpdate(ApplicationPublicInfo bean);
     public ApplicationPublicInfo getById(String id);
     public ApplicationPublicInfo getByPrimaryKey(String key);
     public List findByQuery(String querystr);
     public List getAll();
     
     
 }
   
    