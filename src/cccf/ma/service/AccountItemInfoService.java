package cccf.ma.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
public interface AccountItemInfoService
{
     public void delete(AccountItemInfo bean);
     public Serializable create(AccountItemInfo bean);
     public void update(AccountItemInfo bean);
     public void saveOrUpdate(AccountItemInfo bean);
     public AccountItemInfo getById(String id);
     public AccountItemInfo getByPrimaryKey(String key);
     public List findByQuery(String querystr);
     public List getAll();
     
     
 }
   
    