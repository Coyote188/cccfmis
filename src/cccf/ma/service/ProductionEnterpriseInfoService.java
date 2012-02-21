package cccf.ma.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
public interface ProductionEnterpriseInfoService
{
     public void delete(ProductionEnterpriseInfo bean);
     public Serializable create(ProductionEnterpriseInfo bean);
     public void update(ProductionEnterpriseInfo bean);
     public void saveOrUpdate(ProductionEnterpriseInfo bean);
     public ProductionEnterpriseInfo getById(String id);
     public ProductionEnterpriseInfo getByPrimaryKey(String key);
     public List findByQuery(String querystr);
     public List getAll();
     
     
 }
   
    