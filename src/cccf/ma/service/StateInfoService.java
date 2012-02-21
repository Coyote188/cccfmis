package cccf.ma.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
public interface StateInfoService
{
     public void delete(StateInfo bean);
     public Serializable create(StateInfo bean);
     public void update(StateInfo bean);
     public void saveOrUpdate(StateInfo bean);
     public StateInfo getById(String id);
     public StateInfo getByPrimaryKey(String key);
     public List findByQuery(String querystr);
     public List getAll();
     
     
 }
   
    