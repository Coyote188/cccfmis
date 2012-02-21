package cccf.ma.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
public interface ProductionModelInfoService
{
     public void delete(ProductionModelInfo bean);
     public Serializable create(ProductionModelInfo bean);
     public void update(ProductionModelInfo bean);
     public void saveOrUpdate(ProductionModelInfo bean);
     public ProductionModelInfo getById(String id);
     public ProductionModelInfo getByPrimaryKey(String key);
     public List findByQuery(String querystr);
     public List getAll();
     
     
 }
   
    