package cccf.ma.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
public interface ProductCatalogueInfoService
{
     public void delete(ProductCatalogueInfo bean);
     public Serializable create(ProductCatalogueInfo bean);
     public void update(ProductCatalogueInfo bean);
     public void saveOrUpdate(ProductCatalogueInfo bean);
     public ProductCatalogueInfo getById(String id);
     public ProductCatalogueInfo getByPrimaryKey(String key);
     public List findByQuery(String querystr);
     public List getAll();
     
     
 }
   
    