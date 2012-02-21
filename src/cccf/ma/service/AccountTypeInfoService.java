package cccf.ma.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
public interface AccountTypeInfoService
{
     public void delete(AccountTypeInfo bean);
     public Serializable create(AccountTypeInfo bean);
     public void update(AccountTypeInfo bean);
     public void saveOrUpdate(AccountTypeInfo bean);
     public AccountTypeInfo getById(String id);
     public AccountTypeInfo getByPrimaryKey(String key);
     public List findByQuery(String querystr);
     public List getAll();
     
     
 }
   
    