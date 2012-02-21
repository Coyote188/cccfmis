package cccf.ma.service;
import java.math.*;
import openjframework.model.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
public interface EnterpriseOwnActivatedProductListInfoService
{
     public void delete(EnterpriseOwnActivatedProductListInfo bean);
     public Serializable create(EnterpriseOwnActivatedProductListInfo bean);
     public void update(EnterpriseOwnActivatedProductListInfo bean);
     public void saveOrUpdate(EnterpriseOwnActivatedProductListInfo bean);
     public EnterpriseOwnActivatedProductListInfo getById(String id);
     public EnterpriseOwnActivatedProductListInfo getByPrimaryKey(String key);
     public List findByQuery(String querystr);
     public List getAll();
     
     
 }
   
    