package cccf.ma.service;
import java.math.*;
import openjframework.model.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
public interface Product_User_ListInfoService
{
     public void delete(Product_User_ListInfo bean);
     public Serializable create(Product_User_ListInfo bean);
     public void update(Product_User_ListInfo bean);
     public void saveOrUpdate(Product_User_ListInfo bean);
     public Product_User_ListInfo getById(String id);
     public Product_User_ListInfo getByPrimaryKey(String key);
     public List findByQuery(String querystr);
     public List getAll();
     
     
 }
   
    