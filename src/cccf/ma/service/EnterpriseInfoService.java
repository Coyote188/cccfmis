package cccf.ma.service;
import java.math.*;
import openjframework.model.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
public interface EnterpriseInfoService
{
     public void delete(EnterpriseInfo bean);
     public Serializable create(EnterpriseInfo bean);
     public void update(EnterpriseInfo bean);
     public void saveOrUpdate(EnterpriseInfo bean);
     public EnterpriseInfo getById(String id);
     public EnterpriseInfo getByPrimaryKey(String key);
     public List findByQuery(String querystr);
     public List getAll();
     
     
 }
   
    